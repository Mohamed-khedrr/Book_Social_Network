package com.project.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBooksResponse {
    private Integer bookId;
    private String title ;
    private String authorName;
    private String about;
    private double rate;
    private boolean archived;
    private boolean returnApproved;








}
