package io.app.excetptions;

public class DuplicateFoundException extends RuntimeException{
    public DuplicateFoundException(String msg){
        super(msg);
    }
}
