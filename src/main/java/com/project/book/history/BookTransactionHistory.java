package com.project.book.history;

import com.project.book.book.Book;
import com.project.book.common.AuditingBaseEntity;
import com.project.book.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class BookTransactionHistory extends AuditingBaseEntity {
    private boolean returned ;
    private boolean returnApproved ;

    @ManyToOne
    private User user ;

    @ManyToOne
    private Book book ;

}
