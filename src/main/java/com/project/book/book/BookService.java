package com.project.book.book;

import com.project.book.user.User;
import com.project.book.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
}
