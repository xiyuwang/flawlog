package com.i2s.flawlog.controller;

import com.i2s.flawlog.domain.bo.MetalBO;
import com.i2s.flawlog.service.MetalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wxy on 2017/11/26.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("meltal")
public class MetalController {
    private static final Logger log = LoggerFactory.getLogger(MetalService.class);

    @Autowired
    MetalService metalService;

    @RequestMapping("/insertMetal")
    public String insertMetal(MetalBO metalBO, HttpServletRequest req){
        log.info("insertMetal"+metalBO.toString());

        int info = -1;
        try{
            info = this.metalService.insertMetal(metalBO);
        }catch(Exception e){
            return "insert to db fail";
        }

        return "ok";
    }

    @RequestMapping("/updateMetal")
    public String updateMetal(MetalBO metalBO,HttpServletRequest req) {
        log.info("updateMetal"+metalBO.toString());

        int info = -1;
        try {
            info = this.metalService.updateMetal(metalBO);
        } catch (Exception e) {
            return "insert to db fail";
        }

        return "ok";
    }
}
