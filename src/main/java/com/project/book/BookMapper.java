package com.project.book;

import com.project.file.FileUtils;
import com.project.history.BookTransactionHistory;
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
                .bookCover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build() ;
    }

    public BorrowedBooksResponse toBorrowedBookResponse(BookTransactionHistory history) {
        Book book = history.getBook() ;
        return BorrowedBooksResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthor())
                .about(book.getAbout())
                .rate(book.getRate())
                .archived(book.isArchived())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
