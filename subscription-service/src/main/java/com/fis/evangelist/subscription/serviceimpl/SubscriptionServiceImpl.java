package com.fis.evangelist.subscription.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fis.evangelist.subscription.NoBookAvailableException;
import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.feign.BookRestConsumer;
import com.fis.evangelist.subscription.mapper.BookMapper;
import com.fis.evangelist.subscription.mapper.SubscriptionMapper;
import com.fis.evangelist.subscription.model.BookRequest;
import com.fis.evangelist.subscription.model.BookResponse;
import com.fis.evangelist.subscription.model.SubscriptionRequest;
import com.fis.evangelist.subscription.model.SubscriptionResponse;
import com.fis.evangelist.subscription.repository.SubscriptionRepository;
import com.fis.evangelist.subscription.service.SubscriptionService;
import com.fis.evangelist.subscription.validator.SubscriptionRequestValidator;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

	private static final String RESILIENCE4J_INSTANCE_NAME = "subscriptionService";
	private static final String FALLBACK_METHOD = "fallback";

	@Value("${book-service.url}")
	private String bookServiceBaseUrl;

	private final SubscriptionRepository subscriptionRepository;
	private final RestTemplate restTemplate;
	private final BookRestConsumer consumer;
	private final BookMapper bookMapper;
	private final SubscriptionMapper subscriptionMapper;

	@Transactional(rollbackFor = NoBookAvailableException.class)
	@CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
	public SubscriptionResponse addSubscription(SubscriptionRequest subscriptionRequest)
			throws NoBookAvailableException {
		log.info("Saving subscription for Book ID: {}", subscriptionRequest.getBookId());
		SubscriptionRequestValidator.validate(subscriptionRequest);
		BookResponse bookResponse = restTemplate
				.getForObject(bookServiceBaseUrl + "/books/" + subscriptionRequest.getBookId(), BookResponse.class);
		if (bookResponse != null && bookResponse.getCopiesAvailable() > 0) {
			bookResponse.setCopiesAvailable(bookResponse.getCopiesAvailable() - 1);
			try {
				BookRequest bookRequest = bookMapper.mapToRequest(bookResponse);
				bookResponse = restTemplate.postForObject(bookServiceBaseUrl + "/books/saveBook", bookRequest,
						BookResponse.class);
			} catch (HttpStatusCodeException se) {
				log.error(se.getResponseBodyAsString());
			} catch (ResourceAccessException e) {
				log.error("Error accessing the resource: {}", e.getMessage());
			}
			Subscription updatedSubscription = subscriptionRepository
					.save(subscriptionMapper.mapToEntity(subscriptionRequest));
			return subscriptionMapper.mapToResponse(updatedSubscription);
		} else {
			throw new NoBookAvailableException("Book is not available.");
		}
	}

	@Transactional(rollbackFor = NoBookAvailableException.class)
	@CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
	public SubscriptionResponse addSubscriptionWithFeign(SubscriptionRequest subscriptionRequest)
			throws NoBookAvailableException {
		log.info("Saving subscription for Book ID: {}", subscriptionRequest.getBookId());
		BookResponse bookResponse = consumer.getBook(subscriptionRequest.getBookId());
		if (bookResponse != null && bookResponse.getCopiesAvailable() > 0) {
			bookResponse.setCopiesAvailable(bookResponse.getCopiesAvailable() - 1);
			try {
				ResponseEntity<BookResponse> response = consumer.saveBook(bookMapper.mapToRequest(bookResponse));
				bookResponse = response.getBody();
			} catch (HttpStatusCodeException se) {
				log.error(se.getResponseBodyAsString());
			}
			Subscription updatedSubscription = subscriptionRepository
					.save(subscriptionMapper.mapToEntity(subscriptionRequest));
			return subscriptionMapper.mapToResponse(updatedSubscription);
		} else {
			throw new NoBookAvailableException("Book is not available.");
		}
	}

	public List<SubscriptionResponse> getAllSubscriptions() {
		log.info("Fetch all subscriptions");
		List<Subscription> allSubscriptions = subscriptionRepository.findAll();

		return allSubscriptions.stream().map(subscriptionMapper::mapToResponse)
				.collect(Collectors.toUnmodifiableList());
	}

	public SubscriptionResponse getSubscription(String subscriberName) {
		log.info("Fetching subscription for subscriber : {}", subscriberName);
		Subscription updatedSubscription = subscriptionRepository.getBySubscriberName(subscriberName);
		return subscriptionMapper.mapToResponse(updatedSubscription);
	}

	public SubscriptionResponse fallback(Subscription subscription, Throwable t) throws NoBookAvailableException {
		throw new NoBookAvailableException("Book service is not available at this moment.");
	}
}
