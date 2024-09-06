package com.project.feedback;

import com.project.book.Book;
import com.project.book.BookRepository;
import com.project.common.PageResponse;
import com.project.exception.OperationNotPermittedException;
import com.project.user.User;
import com.project.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public PageResponse<FeedbackResponse> getBookFeedbacks(Integer bookId, Authentication currentUserAuth, int page, int size) {
        User user = (User) currentUserAuth.getPrincipal() ;
        Pageable pageable = PageRequest.of(page, size) ;
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId , pageable) ;
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feed -> feedbackMapper.toFeedbackResponse(feed , user.getId()))
                .toList() ;
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalPages(),
                feedbacks.getTotalElements(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
