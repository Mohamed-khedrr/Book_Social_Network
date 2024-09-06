package com.project.feedback;

import com.project.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request , Book book){
        return Feedback.builder()
                .book(book)
                .note(request.note())
                .comment(request.comment())
                .build();
    }
}
