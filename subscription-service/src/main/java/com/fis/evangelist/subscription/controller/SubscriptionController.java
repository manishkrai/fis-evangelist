package com.fis.evangelist.subscription.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fis.evangelist.subscription.NoBookAvailableException;
import com.fis.evangelist.subscription.model.SubscriptionRequest;
import com.fis.evangelist.subscription.model.SubscriptionResponse;
import com.fis.evangelist.subscription.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
	
	private final SubscriptionService subscriptionService;
	
  @Operation(summary = "Add a new subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription added successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity: No book available")
    })
	@PostMapping("save")
	public ResponseEntity<SubscriptionResponse> addSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
	    log.info("Adding subscription for {}", subscriptionRequest.getBookId());
	    HttpStatus status = HttpStatus.CREATED;
	    SubscriptionResponse updatedSubscription = null;
	    try {
	        updatedSubscription = subscriptionService.addSubscription(subscriptionRequest);
	    } catch (NoBookAvailableException e) {
	        log.warn("Failed to add subscription: {}", e.getMessage());
	        status = HttpStatus.UNPROCESSABLE_ENTITY;
	    }

	    return new ResponseEntity<>(updatedSubscription, status);
	}
	
  	@Operation(summary = "Get all subscriptions")
	@GetMapping("")
	public List<SubscriptionResponse> getAllSubscriptions() {
		log.info("Inside getAllSubscription method of SubscriptionController");
		return subscriptionService.getAllSubscriptions();
	}
	
  	@Operation(summary = "Get subscription by subscriber name")
	@GetMapping("/{subscriberName}")
	public SubscriptionResponse getSubscription(@PathVariable("subscriberName") String subscriberName) {
		log.info("Inside getSubscription method of SubscriptionController");
		return subscriptionService.getSubscription(subscriberName);
	}
	
  	@Operation(summary = "Add a new subscription with Feign")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription added successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity: No book available")
    })
	@PostMapping("/feign/add")
	public ResponseEntity<SubscriptionResponse> addSubscriptionWithFeign(@RequestBody SubscriptionRequest subscriptionRequest) {
	    log.info("Inside addSubscriptionWithFeign method of SubscriptionController");
	    HttpStatus status = HttpStatus.CREATED;
	    SubscriptionResponse updatedSubscription = null;
	    try {
	        updatedSubscription = subscriptionService.addSubscriptionWithFeign(subscriptionRequest);
	    } catch (NoBookAvailableException e) {
	        log.warn("Failed to add subscription with Feign: {}", e.getMessage());
	        status = HttpStatus.UNPROCESSABLE_ENTITY;
	    }

	    return new ResponseEntity<>(updatedSubscription, status);
	}
}
