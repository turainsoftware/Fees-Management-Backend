package io.app.excetptions;

public class NotAllowedException extends RuntimeException{
    public NotAllowedException(String msg){
        super(msg);
    }
}
