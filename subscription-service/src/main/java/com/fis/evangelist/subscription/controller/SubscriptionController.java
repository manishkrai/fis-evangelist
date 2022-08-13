package com.fis.evangelist.subscription.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fis.evangelist.subscription.NoBookAvailableException;
import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/subscriptions")
public class SubscriptionController {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@PostMapping("")
	public ResponseEntity<Subscription> addSubscription(@RequestBody Subscription subscription) {
		log.info("Inside addSubscription method of SubscriptionController");
		HttpStatus status = HttpStatus.CREATED;
		Subscription updatedSubscription = null;
		try
		{
			updatedSubscription = subscriptionService.addSubscription(subscription);
		}
		catch(NoBookAvailableException e) {
			status = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return new ResponseEntity<>(updatedSubscription, status);		
	}
	
	@GetMapping("")
	public List<Subscription> getAllSubscriptions() {
		log.info("Inside getAllSubscription method of SubscriptionController");
		return subscriptionService.getAllSubscriptions();
	}
	
	@GetMapping("/{subscriberName}")
	public Subscription getSubscription(@PathVariable("subscriberName") String subscriberName) {
		log.info("Inside getSubscription method of SubscriptionController");
		return subscriptionService.getSubscription(subscriberName);
	}
	
	@PostMapping("/feign")
	public ResponseEntity<Subscription> addSubscriptionWithFeign(@RequestBody Subscription subscription) {
		log.info("Inside addSubscription method of SubscriptionController");
		HttpStatus status = HttpStatus.CREATED;
		Subscription updatedSubscription = null;
		try
		{
			updatedSubscription = subscriptionService.addSubscriptionWithFeign(subscription);
		}
		catch(NoBookAvailableException e) {
			status = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return new ResponseEntity<>(updatedSubscription, status);		
	}
}
