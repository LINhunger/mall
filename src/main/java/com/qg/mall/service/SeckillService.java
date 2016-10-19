package com.qg.mall.service;

import com.qg.mall.dto.Exposer;
import com.qg.mall.dto.SeckillExecution;
import com.qg.mall.entity.Seckill;
import com.qg.mall.exception.RepeatKillException;
import com.qg.mall.exception.SeckillCloseException;
import com.qg.mall.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者的角度去设计接口
 * 三个方面：方法定义粒度，参数，返回类型（return  类型/异常）
 * Created by hunger on 2016/10/10.
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始时秒杀接口地址，
     * 否则输出系统时间以及秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
    throws SeckillCloseException,SeckillException,RepeatKillException;
}
