package com.fis.evangelist.subscription.service;

import java.util.List;

import com.fis.evangelist.subscription.NoBookAvailableException;
import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.model.SubscriptionRequest;
import com.fis.evangelist.subscription.model.SubscriptionResponse;

public interface SubscriptionService {	
	SubscriptionResponse addSubscription(SubscriptionRequest subscriptionRequest) throws NoBookAvailableException;
	SubscriptionResponse addSubscriptionWithFeign(SubscriptionRequest subscriptionRequest) throws NoBookAvailableException;
	List<SubscriptionResponse> getAllSubscriptions();
	SubscriptionResponse getSubscription(String subscriberName);
	SubscriptionResponse fallback(Subscription subscription, Throwable t) throws NoBookAvailableException;
}
