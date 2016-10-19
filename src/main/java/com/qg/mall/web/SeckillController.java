package com.qg.mall.web;

import com.qg.mall.dto.Exposer;
import com.qg.mall.dto.SeckillExecution;
import com.qg.mall.dto.SeckillResult;
import com.qg.mall.entity.Seckill;
import com.qg.mall.enums.SeckillStatEnum;
import com.qg.mall.exception.RepeatKillException;
import com.qg.mall.exception.SeckillCloseException;
import com.qg.mall.service.SeckillService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by hunger on 2016/10/11.
 */
@Controller
@RequestMapping("/seckill")//url:/模块/资源/{id}/细分/seckill/list
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        //list.jsp+ model= ModelAndView
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list  invoke:"+list);
        model.addAttribute("seckillList",list);
        return  "list";///WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId,Model model){
        if (seckillId == null){
            return  "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null){
            return "forward:/seckill/list";
        }
        logger.info("detail  invoke:"+seckill);
        model.addAttribute("seckill",seckill);
        return "detail";
    }
    /**
     * ajax json
    * */
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST
    ,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer (@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return  result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution",
    method = RequestMethod.POST,
    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public  SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                    @PathVariable("md5") String md5,
                                                    @CookieValue(value = "killPhone",required = false)Long phone ){
        if (phone == null) {
            return  new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;

        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(
                    true,
                    execution
            );

        }catch (RepeatKillException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            System.out.println("execution:"+execution);
            return new SeckillResult<SeckillExecution>(false,execution);
        }catch (SeckillCloseException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            System.out.println("execution:"+execution);
            return new SeckillResult<SeckillExecution>(false,execution);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }
    }
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult(true,now.getTime());
    }
}
