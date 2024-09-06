package com.project.feedback;

import com.project.book.Book;
import com.project.common.AuditingBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
