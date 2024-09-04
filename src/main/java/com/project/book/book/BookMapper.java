package com.project.book.book;

import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .author(request.author())
                .about(request.about())
                .shareable(request.shareable())
                .archived(false)
                .build() ;
    }
}
