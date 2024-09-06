package com.project.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperationNotPermittedException extends RuntimeException{
    public OperationNotPermittedException(String msg){
        super(msg);
    }
}
