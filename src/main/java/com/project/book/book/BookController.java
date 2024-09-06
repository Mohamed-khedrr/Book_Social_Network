package com.project.book.book;

import com.project.book.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService ;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid BookRequest request,
                                            Authentication currentUserAuth) {
        return ResponseEntity.ok(bookService.save(request , currentUserAuth)) ;
    }


    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Integer bookId){
        return ResponseEntity.ok(bookService.findById(bookId)) ;
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name="page", defaultValue = "0" , required = false)  int page,
            @RequestParam(name="size", defaultValue = "10" , required = false)  int size,
            Authentication currentUserAuth
    ){
        return ResponseEntity.ok(bookService.findAllBooks(page , size , currentUserAuth)) ;

    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name="page", defaultValue = "0" , required = false)  int page,
            @RequestParam(name="size", defaultValue = "10" , required = false)  int size,
            Authentication currentUserAuth
    ){
        return ResponseEntity.ok(bookService.findAllUserBooks(page , size , currentUserAuth)) ;
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllBorrowedBooks(
            @RequestParam(name="page", defaultValue = "0" , required = false)  int page,
            @RequestParam(name="size", defaultValue = "10" , required = false)  int size,
            Authentication currentUserAuth
    ){
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page , size , currentUserAuth)) ;
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllReturnedBooks(
            @RequestParam(name="page", defaultValue = "0" , required = false)  int page,
            @RequestParam(name="size", defaultValue = "10" , required = false)  int size,
            Authentication currentUserAuth
    ){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page , size , currentUserAuth)) ;
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<?> updateShareable(@PathVariable("book-id") Integer bookId ,
                                                   Authentication currentUserAuth ){
        return bookService.updateShareable(bookId , currentUserAuth) ;
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<?> updateArchived(@PathVariable("book-id") Integer bookId ,
                                                   Authentication currentUserAuth ){
        return bookService.updateArchived(bookId , currentUserAuth) ;
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable ("book-id") Integer bookId ,
            Authentication currentUserAuth
    ) {
        return  ResponseEntity.ok(bookService.borrowBook(bookId , currentUserAuth)) ;
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBook(
            @PathVariable("book-id") Integer bookId ,
            Authentication currentUserAuth
    ){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId , currentUserAuth)) ;
    }
}
