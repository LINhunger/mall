package com.qg.mall.exception;

/**
 * 重复秒杀异常（运行期异常）
 * Created by hunger on 2016/10/10.
 */
public class RepeatKillException extends  SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
