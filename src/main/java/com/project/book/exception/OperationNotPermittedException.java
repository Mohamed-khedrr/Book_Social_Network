package com.project.book.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperationNotPermittedException extends RuntimeException{
    public OperationNotPermittedException(String msg){
        super(msg);
    }
}
