package com.project.history;

import com.project.book.Book;
import com.project.common.AuditingBaseEntity;
import com.project.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
