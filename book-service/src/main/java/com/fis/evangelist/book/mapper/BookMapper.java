package com.fis.evangelist.book.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.model.BookRequest;
import com.fis.evangelist.model.BookResponse;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    Book mapToEntity(BookRequest bookRequest);

    BookResponse mapToResponse(Book book);
}
