package com.project.book.book;

import com.project.book.common.PageResponse;
import com.project.book.user.User;
import com.project.book.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public Integer save(BookRequest request, Authentication ownerUser) {

        User user =(User) ownerUser.getPrincipal() ;
        Book book = bookMapper.toBook(request) ;
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return (bookRepository.findById(bookId))
                .map(bookMapper::toBookResponse )
                .orElseThrow(()-> new EntityNotFoundException("Book not found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication currentUser) {
        User user = (User) currentUser.getPrincipal() ;
        Pageable pageable = PageRequest.of(page , size , Sort.by("creationDate").descending()) ;
        Page<Book> books = bookRepository.findAllShareableBooks(pageable, user.getId()) ;
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<BookResponse>(
                bookResponses,books.getNumber(),
                books.getSize() ,
                books.getTotalPages() ,
                books.getTotalElements(),
                books.isFirst() ,
                books.isLast()
        );
    }
}
