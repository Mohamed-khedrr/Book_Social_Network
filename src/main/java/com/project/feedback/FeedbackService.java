package com.project.feedback;

import com.project.book.Book;
import com.project.book.BookRepository;
import com.project.exception.OperationNotPermittedException;
import com.project.user.User;
import com.project.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper ;


    public Integer save(FeedbackRequest request, Authentication currentUserAuth) {
        User user = (User) currentUserAuth.getPrincipal() ;
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(()-> new EntityNotFoundException("Book not found"));

        if(book.isArchived() || !book.isShareable())
            throw new OperationNotPermittedException("Feedbacks not available for this book") ;

        if (book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You cannot give a feedback to your book") ;

        Feedback feedback = feedbackMapper.toFeedback(request , book) ;
        return feedbackRepository.save(feedback).getId() ;
    }







}
