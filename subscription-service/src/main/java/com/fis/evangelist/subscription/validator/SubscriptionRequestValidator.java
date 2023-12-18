package com.fis.evangelist.subscription.validator;

import org.apache.commons.lang.StringUtils;

import com.fis.evangelist.subscription.model.SubscriptionRequest;

public class SubscriptionRequestValidator {

    public static void validate(SubscriptionRequest subscriptionRequest) {
        if (subscriptionRequest == null) {
            throw new IllegalArgumentException("SubscriptionRequest cannot be null");
        }

        validateRequiredField("subscriberName", subscriptionRequest.getSubscriberName());
        validateRequiredField("bookId", subscriptionRequest.getBookId());
        validateRequiredField("dateSubscribed", subscriptionRequest.getDateSubscribed() == null ?
        		null :  subscriptionRequest.getDateSubscribed().toString());
    }

    private static void validateRequiredField(String fieldName, String fieldValue) {
        if (StringUtils.isEmpty(fieldValue)) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }
}
