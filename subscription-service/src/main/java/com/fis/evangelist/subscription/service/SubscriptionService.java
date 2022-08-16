package com.fis.evangelist.subscription.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fis.evangelist.subscription.NoBookAvailableException;
import com.fis.evangelist.subscription.VO.Book;
import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.feign.BookRestConsumer;
import com.fis.evangelist.subscription.repository.SubscriptionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    private BookRestConsumer consumer;
	
	@Transactional(rollbackFor = NoBookAvailableException.class)
	public Subscription addSubscription(Subscription subscription) throws NoBookAvailableException {
		log.info("Inside addSubscription method of SubscriptionService");
		Book book = restTemplate.getForObject("http://localhost:9001/books/getBook/" + subscription.getBookId() , Book.class);
		if(book != null && book.getCopiesAvailable() > 0) {
			book.setCopiesAvailable(book.getCopiesAvailable() - 1);
			book = restTemplate.postForObject("http://localhost:9001/books/saveBook", book, Book.class);
			return subscriptionRepository.save(subscription);
		} else {
			throw new NoBookAvailableException("Book not available.");
		}		
	}
	
	@Transactional(rollbackFor = NoBookAvailableException.class)
	public Subscription addSubscriptionWithFeign(Subscription subscription) throws NoBookAvailableException {
		log.info("Inside addSubscription method of SubscriptionService");
		Book book = consumer.getBook(subscription.getBookId());
		if(book != null && book.getCopiesAvailable() > 0) {
			book.setCopiesAvailable(book.getCopiesAvailable() - 1);
			book = consumer.saveBook(book);
			return subscriptionRepository.save(subscription);
		} else {
			throw new NoBookAvailableException("Book not available.");
		}		
	}

	public List<Subscription> getAllSubscriptions() {
		log.info("Inside getAllSubscriptions method of SubscriptionService");
		return subscriptionRepository.findAll();
	}

	public Subscription getSubscription(String subscriberName) {
		log.info("Inside getAllSubscriptions method of SubscriptionService");
		return subscriptionRepository.getBySubscriberName(subscriberName);
	}

}
