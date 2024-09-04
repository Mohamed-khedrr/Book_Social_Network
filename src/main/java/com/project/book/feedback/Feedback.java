package com.project.book.feedback;

import com.project.book.book.Book;
import com.project.book.common.AuditingBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Feedback extends AuditingBaseEntity {
    private double note ;
    private String comment ;

    @ManyToOne()
    private Book book ;
}
