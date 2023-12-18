package com.fis.evangelist.subscription.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.model.SubscriptionRequest;
import com.fis.evangelist.subscription.model.SubscriptionResponse;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "id", ignore = true)
    Subscription mapToEntity(SubscriptionRequest subscriptionRequest);

    SubscriptionResponse mapToResponse(Subscription subscription);
}
