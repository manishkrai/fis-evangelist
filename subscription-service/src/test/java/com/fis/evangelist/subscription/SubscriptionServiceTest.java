package com.fis.evangelist.subscription;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fis.evangelist.subscription.VO.Book;
import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.feign.BookRestConsumer;
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
	
	@InjectMocks
    private SubscriptionService subscriptionService;
	
	private Book book;
	
	private Subscription subscription;
	
	@BeforeEach
    public void setup(){
		this.book = new Book();
		this.book.setId(1L);
		this.book.setBookId("B1212");
		this.book.setName("History of Amazon Valley");
		this.book.setAuthor("Ross Suarez");
		this.book.setCopiesAvailable(2);
		this.book.setTotalCopies(2);
		
		this.subscription = new Subscription();
		this.subscription.setSubscriberName("Manish Rai");
		this.subscription.setBookId("B1212");
		this.subscription.setDateSubscribed(new Date());
    }
	
	@Test 
	void addSubscription() {
		Mockito.when(this.restTemplate.getForObject("http://localhost:9001/books/getBook/" + subscription.getBookId() , Book.class))
        .thenReturn(this.book);
		
		
		Mockito.when(restTemplate.postForObject("http://localhost:9001/books/saveBook", book, Book.class))
        .thenReturn(this.book);
		
		Mockito.when(this.subscriptionRepository.save(this.subscription))
        .thenReturn(this.subscription);
		
		try
		{
			Subscription subscription = this.subscriptionService.addSubscription(this.subscription);
			assertThat(subscription).isNotNull();
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
			Subscription subscription = this.subscriptionService.addSubscription(this.subscription);
			assertThat(subscription).isNotNull();
		}
		catch(Exception e)
		{
			assertThat(e.getClass()).isEqualTo(NoBookAvailableException.class);
		}
	}
	
	@Test 
	void addSubscriptionWithFeign() {		
		Mockito.lenient().when(this.consumer.getBook(this.subscription.getBookId()))
        .thenReturn(this.book);
				
		Mockito.lenient().when(this.consumer.saveBook(this.book))
        .thenReturn(new ResponseEntity<Book>(this.book, HttpStatus.CREATED));
		
		Mockito.lenient().when(this.subscriptionRepository.save(this.subscription))
        .thenReturn(this.subscription);
		
		try
		{
			Subscription subscription = this.subscriptionService.addSubscription(this.subscription);
			assertThat(subscription).isNotNull();
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
			Subscription subscription = this.subscriptionService.addSubscription(this.subscription);
			assertThat(subscription).isNotNull();
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
		
		List<Subscription> returnedSubscription = this.subscriptionService.getAllSubscriptions();
		assertThat(returnedSubscription.size()).isGreaterThan(0);
		
	}
	
	@Test 
	void getSubscription() {
		
		Mockito.when(this.subscriptionRepository.getBySubscriberName("Manish Rai"))
        .thenReturn(this.subscription);
		
		Subscription subscription = this.subscriptionService.getSubscription("Manish Rai");
		assertThat(subscription).isNotNull();
		
	}
	
	@Test 
	void getSubscriptionNotExists() {
			
			Mockito.when(this.subscriptionRepository.getBySubscriberName("Test"))
	        .thenReturn(null);
			
			Subscription subscription = this.subscriptionService.getSubscription("Test");
			assertThat(subscription).isNull();
			
		}
	
}
