package com.qg.mall.service.impl;

import com.qg.mall.dao.SeckillDao;
import com.qg.mall.dao.SuccessKilledDao;
import com.qg.mall.dao.cache.RedisDao;
import com.qg.mall.dto.Exposer;
import com.qg.mall.dto.SeckillExecution;
import com.qg.mall.entity.Seckill;
import com.qg.mall.entity.SuccessKilled;
import com.qg.mall.enums.SeckillStatEnum;
import com.qg.mall.exception.RepeatKillException;
import com.qg.mall.exception.SeckillCloseException;
import com.qg.mall.exception.SeckillException;
import com.qg.mall.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by hunger on 2016/10/10.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired//@Resource
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    //md5盐值字符串
    private  final String slat = "livid";
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化
        // 1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null) {
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3.放入redis
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId);//do
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5 (long seckillId) {
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return  md5;
    }



    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点。
     * 1.开发团队达成一致约定，明确注解事务方法编程风格。
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/http请求/或者剥离到事务方法外部。
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制。
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, SeckillException, RepeatKillException {
        if(md5 == null ||  !md5.equals(getMD5(seckillId))){
            System.out.println(md5);
            throw  new SeckillException("seckill data rewire");
        }
        //执行秒杀逻辑：减库存，+加购买记录
        Date nowTime = new Date();
        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一：seckillId,usePhone
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount <= 0) {
                    //没有更新记录，秒杀结束,rollback
                    throw new SeckillCloseException("close");
                } else {
                    //秒杀成功，commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw  e1;
        } catch (RepeatKillException e2){
            throw  e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw  new SeckillException("seckill inner error: "+e.getMessage());
        }
    }
}

