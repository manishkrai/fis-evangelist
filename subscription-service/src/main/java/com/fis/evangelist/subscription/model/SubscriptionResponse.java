package com.fis.evangelist.subscription.model;

import java.util.Date;

import lombok.Data;

@Data
public class SubscriptionResponse {
	private String subscriberName;
	private String bookId;
	private Date dateSubscribed;
	private Date dateReturned;
}
