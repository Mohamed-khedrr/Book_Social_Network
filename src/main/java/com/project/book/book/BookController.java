package com.project.book.book;

import com.project.book.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService ;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid BookRequest request,
                                            Authentication currentUser) {
        return ResponseEntity.ok(bookService.save(request , currentUser)) ;
    }


    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Integer bookId){
        return ResponseEntity.ok(bookService.findById(bookId)) ;
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name="page", defaultValue = "0" , required = false)  int page,
            @RequestParam(name="size", defaultValue = "10" , required = false)  int size,
            Authentication currentUser
    ){
        return ResponseEntity.ok(bookService.findAllBooks(page , size , currentUser)) ;

    }
}
