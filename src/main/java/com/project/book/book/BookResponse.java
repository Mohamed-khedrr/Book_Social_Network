package com.project.book.book;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Integer id ;
    private String title;
    private String author;
    private String about ;
    private Integer bookNumber ;
    private String ownerName ;
    private byte[] bookCover;
    private boolean archived ;
    private boolean shareable ;
    private double rate ;

}
