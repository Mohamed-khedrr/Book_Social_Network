package com.project.book.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<BookTransactionHistory , Integer> {

    @Query("""
    SELECT trans
    FROM BookTransactionHistory trans
    WHERE trans.user.id = :userId
    """)
    public Page<BookTransactionHistory> findAllBorrowedBooks(Integer userId, Pageable pageable);

    @Query("""
    SELECT trans
    FROM BookTransactionHistory trans
    WHERE trans.book.owner= :userId
    AND trans.returned = true
    """)
    public Page<BookTransactionHistory> findAllReturnedBooks(Integer userId, Pageable pageable);

    @Query("""
    SELECT
    (COUNT(*) > 0) isBorrowed
    FROM BookTransactionHistory trans
    WHERE trans.user.id = :userId
    AND trans.book.id = :bookId
    AND trans.returnApproved = false
    """)
    boolean isBookBorrowed(Integer bookId, Integer userId);

    @Query("""
    SELECT trans
    FROM BookTransactionHistory trans
    WHERE trans.user.id = :userId
    AND trans.book.id = :bookId
    AND trans.returnApproved = false
    AND trans.returned = false
    """)
    Optional<BookTransactionHistory> findBorrowedBooksByBookIdUserId(Integer bookId, Integer userId) ;

    @Query("""
    SELECT trans
    FROM BookTransactionHistory trans
    WHERE trans.book.owner.id = :userId
    AND trans.book.id = :bookId
    AND trans.returnApproved = false
    AND trans.returned = false
    """)
    Optional<BookTransactionHistory> findBorrowedBooksByBookIdOwnerId(Integer bookId, Integer userId) ;








}

