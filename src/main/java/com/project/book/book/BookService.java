package com.project.book.book;

import com.project.book.common.PageResponse;
import com.project.book.exception.OperationNotPermittedException;
import com.project.book.history.BookTransactionHistory;
import com.project.book.history.TransactionHistoryRepository;
import com.project.book.user.User;
import com.project.book.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository ;
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

        return new PageResponse<>(
                bookResponses,books.getNumber(),
                books.getSize() ,
                books.getTotalPages() ,
                books.getTotalElements(),
                books.isFirst() ,
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllUserBooks(int page, int size, Authentication currentUserAuth) {
        User user = (User) currentUserAuth.getPrincipal();
        Pageable pageable = PageRequest.of(page , size , Sort.by("creationDate").descending()) ;
        Page<Book> books =bookRepository.findAllByUserId(user.getId() , pageable);

        List<BookResponse> bookResponses= books.stream()
                .map(bookMapper::toBookResponse)
                .toList() ;

        return new PageResponse <>(
                bookResponses,
                books.getNumber(),
                books.getSize() ,
                books.getTotalPages() ,
                books.getTotalElements(),
                books.isFirst() ,
                books.isLast()
        );
    }

    public PageResponse<BorrowedBooksResponse> findAllBorrowedBooks(int page, int size, Authentication currentUserAuth) {
        User user = (User) currentUserAuth.getPrincipal() ;
        Pageable pageable = PageRequest.of(page , size , Sort.by("creationDate").descending()) ;
        Page<BookTransactionHistory> books = transactionHistoryRepository.findAllBorrowedBooks(user.getId() , pageable);
        List<BorrowedBooksResponse> borrowedBooksResponses = books.stream()
                .map(bookMapper::toBorrowedBookResponse).toList() ;
        return new PageResponse<>(
                borrowedBooksResponses ,
                books.getNumber(),
                books.getSize() ,
                books.getTotalPages() ,
                books.getTotalElements(),
                books.isFirst() ,
                books.isLast()
        );
    }

    public PageResponse<BorrowedBooksResponse> findAllReturnedBooks(int page, int size, Authentication currentUserAuth) {

        User user = (User) currentUserAuth.getPrincipal() ;
        Pageable pageable = PageRequest.of(page , size , Sort.by("creationDate").descending()) ;
        Page<BookTransactionHistory> books = transactionHistoryRepository.findAllReturnedBooks(user.getId() , pageable);
        List<BorrowedBooksResponse> borrowedBooksResponses = books.stream()
                .map(bookMapper::toBorrowedBookResponse).toList() ;
        return new PageResponse<>(
                borrowedBooksResponses ,
                books.getNumber(),
                books.getSize() ,
                books.getTotalPages() ,
                books.getTotalElements(),
                books.isFirst() ,
                books.isLast()
        );    }

    public ResponseEntity<?> updateShareable(Integer bookId, Authentication currentUserAuth) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found") ) ;
        User user = (User) currentUserAuth.getPrincipal() ;
        if(!book.getOwner().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body("Not The Owner of The book");

        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return ResponseEntity.ok(book.getId()) ;
    }

    public ResponseEntity<?> updateArchived(Integer bookId, Authentication currentUserAuth) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found") ) ;
        User user = (User) currentUserAuth.getPrincipal() ;

        if(!book.getOwner().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body("Not The Owner of The book");

        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return ResponseEntity.ok(book.getId()) ;    }

    public Integer borrowBook(Integer bookId, Authentication currentUserAuth) {
        User user = (User) currentUserAuth.getPrincipal() ;
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("Book not found") ) ;
        if(book.isArchived() || !book.isShareable() )
            throw new OperationNotPermittedException("Book not available for borrowing") ;
        if(book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You are the owner of the book already");

        final boolean isBookBorrowed = transactionHistoryRepository.isBookBorrowed(bookId , user.getId()) ;
        if (isBookBorrowed)
            throw new OperationNotPermittedException("Your already borrowed the book"); ;

        BookTransactionHistory trans = BookTransactionHistory.builder()
                .book(book)
                .user(user)
                .build();
        return  transactionHistoryRepository.save(trans).getId() ;

    }

    public Integer returnBorrowedBook(Integer bookId, Authentication currentUserAuth) {
        User user = (User) currentUserAuth.getPrincipal() ;

        BookTransactionHistory transaction = transactionHistoryRepository.findBorrowedBooksByBookIdUserId(bookId , user.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Book Is Not Borrowed"));

        transaction.setReturned(true);
        return transactionHistoryRepository.save((transaction)).getId() ;

    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication currentUserAuth) {
        User user = (User) currentUserAuth.getPrincipal() ;
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("Book not found") ) ;
        if(book.isArchived() || !book.isShareable() )
            throw new OperationNotPermittedException("Book not available for borrowing") ;
        if(!book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You are not the owner of the book");

        BookTransactionHistory transaction = transactionHistoryRepository.findBorrowedBooksByBookIdOwnerId(bookId , user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book Is Not Borrowed"));
        transaction.setReturnApproved(true);

        return transactionHistoryRepository.save(transaction).getId();
    }
}
