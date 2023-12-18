package com.fis.evangelist.subscription.mapper;

import org.mapstruct.Mapper;

import com.fis.evangelist.subscription.model.BookRequest;
import com.fis.evangelist.subscription.model.BookResponse;

@Mapper(componentModel = "spring")
public interface BookMapper {    
    BookRequest mapToRequest(BookResponse subscription);
}
