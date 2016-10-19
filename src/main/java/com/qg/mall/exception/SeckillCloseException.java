package com.qg.mall.exception;

/**
 * 秒杀关闭异常
 * Created by hunger on 2016/10/10.
 */
public class SeckillCloseException  extends  SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
