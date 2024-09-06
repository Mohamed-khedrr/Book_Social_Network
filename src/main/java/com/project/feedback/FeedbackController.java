package com.project.feedback;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService ;


    @PostMapping()
    public ResponseEntity<Integer> saveFeedback(
            @RequestBody @Valid FeedbackRequest request ,
            Authentication currentUserAuth
    ) {
        return ResponseEntity.ok(feedbackService.save(request , currentUserAuth)) ;
    }




}
