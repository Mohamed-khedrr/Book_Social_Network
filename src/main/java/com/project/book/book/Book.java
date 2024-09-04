package com.project.book.book;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id @GeneratedValue
    private int id ;
    private String title ;
    private String author ;
    private int bookNumber ;
    private String about ;
    private String bookCover ;
    private boolean archived ;
    private boolean shareable;
    @CreatedDate
    @Column(nullable = false , updatable = false )
    private LocalDateTime creationDate ;
    @LastModifiedDate
    @Column(insertable = false )
    private LocalDateTime modificationDate ;
    @CreatedBy
    @Column(nullable = false , updatable = false )
    private int createdBy ;
    @LastModifiedBy
    @Column(insertable = false )
    private int modifiedBy ;

}
