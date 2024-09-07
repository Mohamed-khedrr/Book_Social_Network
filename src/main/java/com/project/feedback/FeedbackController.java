package com.project.feedback;

import com.project.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {
    private final FeedbackService feedbackService ;


    @PostMapping()
    public ResponseEntity<Integer> saveFeedback(
            @RequestBody @Valid FeedbackRequest request ,
            Authentication currentUserAuth
    ) {
        return ResponseEntity.ok(feedbackService.save(request , currentUserAuth)) ;
    }


    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getBookFeedback(
            @PathVariable("book-id") Integer bookId ,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication currentUserAuth) {
        return ResponseEntity.ok(feedbackService.getBookFeedbacks(bookId, currentUserAuth , page , size)) ;
    }




}
