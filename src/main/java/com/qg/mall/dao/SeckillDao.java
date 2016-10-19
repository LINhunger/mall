package com.qg.mall.dao;

import com.qg.mall.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by hunger on 2016/10/9.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
        int  reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     *
     * @param seckillId
     * @return
     */
        Seckill queryById(long seckillId);

    /**
     *根据id查询SuccessKilled并携带对象实体
     * @param offet
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offet, @Param("limit") int limit);
}
