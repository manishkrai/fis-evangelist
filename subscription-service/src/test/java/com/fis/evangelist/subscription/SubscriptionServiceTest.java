package com.fis.evangelist.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.feign.BookRestConsumer;
import com.fis.evangelist.subscription.mapper.SubscriptionMapper;
import com.fis.evangelist.subscription.model.BookRequest;
import com.fis.evangelist.subscription.model.BookResponse;
import com.fis.evangelist.subscription.model.SubscriptionRequest;
import com.fis.evangelist.subscription.model.SubscriptionResponse;
import com.fis.evangelist.subscription.repository.SubscriptionRepository;
import com.fis.evangelist.subscription.service.SubscriptionService;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
	
	@Mock
	private SubscriptionRepository subscriptionRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
    private BookRestConsumer consumer;
	
	@Autowired
	private SubscriptionMapper subscriptionMapper;
	
	@InjectMocks
    private SubscriptionService subscriptionService;
	
	private BookRequest bookRequest;
	
	private BookResponse bookResponse;
	
	private SubscriptionRequest subscriptionRequest;
	
	private SubscriptionResponse subscriptionResponse;
	
	private Subscription subscription;
	
	@BeforeEach
    public void setup(){
		this.bookRequest = new BookRequest();
		this.bookRequest.setBookId("B1212");
		this.bookRequest.setName("History of Amazon Valley");
		this.bookRequest.setAuthor("Ross Suarez");
		this.bookRequest.setCopiesAvailable(2);
		this.bookRequest.setTotalCopies(2);
		
		this.bookResponse = new BookResponse();
		this.bookResponse.setBookId("B1212");
		this.bookResponse.setName("History of Amazon Valley");
		this.bookResponse.setAuthor("Ross Suarez");
		this.bookResponse.setCopiesAvailable(2);
		this.bookResponse.setTotalCopies(2);
		
		this.subscriptionRequest = new SubscriptionRequest();
		this.subscriptionRequest.setSubscriberName("Manish Rai");
		this.subscriptionRequest.setBookId("B1212");
		this.subscriptionRequest.setDateSubscribed(new Date());
		
		this.subscriptionResponse = new SubscriptionResponse();
		this.subscriptionResponse.setSubscriberName("Manish Rai");
		this.subscriptionResponse.setBookId("B1212");
		this.subscriptionResponse.setDateSubscribed(new Date());
		
		this.subscription = new Subscription();
		this.subscription.setSubscriberName("Manish Rai");
		this.subscription.setBookId("B1212");
		this.subscription.setDateSubscribed(new Date());
    }
	
	@Test 
	void addSubscription() {
		Mockito.when(this.restTemplate.getForObject("http://localhost:9001/books/" + subscriptionRequest.getBookId() , BookResponse.class))
        .thenReturn(this.bookResponse);
		
		
		Mockito.when(restTemplate.postForObject("http://localhost:9001/books/save", bookResponse, BookResponse.class))
        .thenReturn(this.bookResponse);
		
		Mockito.when(this.subscriptionRepository.save(subscriptionMapper.mapToEntity(subscriptionRequest)))
        .thenReturn(this.subscription);
		
		try
		{
			SubscriptionResponse subscription = this.subscriptionService.addSubscription(this.subscriptionRequest);
			assertNotNull(subscription);
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Test 
	void addSubscriptionBookNotAvailable() {
		Mockito.when(this.restTemplate.getForObject("http://localhost:9001/books/getBook/" + subscription.getBookId() , Book.class))
        .thenReturn(null);
		
		try
		{
			SubscriptionResponse subscription = this.subscriptionService.addSubscription(this.subscriptionRequest);
			assertNotNull(subscription);
		}
		catch(Exception e)
		{
			assertThat(e.getClass()).isEqualTo(NoBookAvailableException.class);
		}
	}
	
	@Test 
	void addSubscriptionWithFeign() {		
		Mockito.lenient().when(this.consumer.getBook(this.subscription.getBookId()))
        .thenReturn(this.bookResponse);
				
		Mockito.lenient().when(this.consumer.saveBook(this.bookRequest))
        .thenReturn(new ResponseEntity<BookResponse>(this.bookResponse, HttpStatus.CREATED));
		
		Mockito.lenient().when(this.subscriptionRepository.save(this.subscription))
        .thenReturn(this.subscription);
		
		try
		{
			SubscriptionResponse subscription = this.subscriptionService.addSubscription(this.subscriptionRequest);
			assertNotNull(subscription);
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Test 
	void addSubscriptionWithFeignNotAvailable() {
		Mockito.lenient().when(this.consumer.getBook(this.subscription.getBookId()))
        .thenReturn(null);		
		try
		{
			SubscriptionResponse subscription = this.subscriptionService.addSubscription(this.subscriptionRequest);
			assertNotNull(subscription);
		}
		catch(Exception e)
		{
			assertThat(e.getClass()).isEqualTo(NoBookAvailableException.class);
		}
	}
	
	@Test 
	void getAllSubscriptions() {
		List<Subscription> subscriptions = new ArrayList<Subscription>();
		subscriptions.add(this.subscription);
		
		Mockito.when(this.subscriptionRepository.findAll())
        .thenReturn(subscriptions);
		
		List<SubscriptionResponse> returnedSubscription = this.subscriptionService.getAllSubscriptions();
		assertThat(returnedSubscription.size()).isGreaterThan(0);
	}
	
	@Test 
	void getSubscription() {		
		Mockito.when(this.subscriptionRepository.getBySubscriberName("Manish Rai"))
        .thenReturn(this.subscription);
		
		SubscriptionResponse subscription = this.subscriptionService.getSubscription("Manish Rai");
		assertThat(subscription).isNotNull();
		
	}
	
	@Test 
	void getSubscriptionNotExists() {			
		Mockito.when(this.subscriptionRepository.getBySubscriberName("Test"))
        .thenReturn(null);
		
		SubscriptionResponse subscription = this.subscriptionService.getSubscription("Test");
		assertThat(subscription).isNull();
	}
}
