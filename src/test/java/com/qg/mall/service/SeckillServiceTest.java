package com.qg.mall.service;

import com.qg.mall.dto.Exposer;
import com.qg.mall.dto.SeckillExecution;
import com.qg.mall.entity.Seckill;
import com.qg.mall.enums.SeckillStatEnum;
import com.qg.mall.exception.RepeatKillException;
import com.qg.mall.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hunger on 2016/10/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit Spring 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private  SeckillService seckillService;
    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        System.out.println(list);
        /*logger.info("list=",list);*/
    }

    @Test
    public void getById() throws Exception {
        long id = 1001L;
        Seckill seckill = seckillService.getById(id);
        System.out.println(seckill);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        System.out.println("exposed:  "+exposer);
    /*exposed:  Exposer{exposed=true, ma5='721f43afdace5181a01be3f4754331c0', seckillId=1000, now=0, start=0, end=0}*/
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000L;
        long phone = 13427581250L;
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,"721f43afdace5181a01be3f4754331c0");
            System.out.println(seckillExecution);
        }catch (SeckillCloseException e1){

        } catch (RepeatKillException e2){}


    }
    @Test
    public void text() throws Exception {
        long id = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            System.out.println("exposed:  "+exposer);
            long phone = 13427581253L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,"721f43afdace5181a01be3f4754331c0");
                System.out.println(seckillExecution);
            }catch (SeckillCloseException e1){

            } catch (RepeatKillException e2){}
        }else {
            System.out.println("exposer:  {}" + exposer);
        }
    }

}