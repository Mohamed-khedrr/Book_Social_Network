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

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .about(book.getAbout())
                .bookNumber(book.getBookNumber())
                .shareable(book.isShareable())
                .archived(book.isArchived())
                .ownerName(book.getOwner().getFullName())
                .rate(book.getRate())
//                .bookCover()
                .build() ;
    }
}
