package com.qg.mall.exception;

/**
 * Created by hunger on 2016/10/10.
 */
public class SeckillException extends  RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
