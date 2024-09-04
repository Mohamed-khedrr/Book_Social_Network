package com.project.book.book;

import com.project.book.common.AuditingBaseEntity;
import com.project.book.feedback.Feedback;
import com.project.book.history.BookTransactionHistory;
import com.project.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends AuditingBaseEntity {
    private String title ;
    private String author ;
    private int bookNumber ;
    private String about ;
    private String bookCover ;
    private boolean archived ;
    private boolean shareable;

    @ManyToOne
    private User owner ;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks ;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories ;



}
