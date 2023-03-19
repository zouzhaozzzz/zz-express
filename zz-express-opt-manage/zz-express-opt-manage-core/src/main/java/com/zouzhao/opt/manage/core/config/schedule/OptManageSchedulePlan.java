package com.zouzhao.opt.manage.core.config.schedule;


import com.zouzhao.opt.manage.core.service.OptExpressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;


@Configuration
@EnableScheduling
public class OptManageSchedulePlan {
    private static final Logger log = LoggerFactory.getLogger(OptManageSchedulePlan.class);
    @Autowired
    private OptExpressService optExpressService;

    // @Scheduled(initialDelay = 15*60*1000,fixedDelay = 60*60*1000 )
    @Scheduled(initialDelay = 60*60*1000,fixedDelay = 20*60*1000 )
    public void refreshExport() {
        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        //a为am/pm的标记
        sdf.applyPattern("yyyy-MM-dd HH:mm:ssa");
        //获取当前时间
        log.debug("定时任务开始刷新报表时间：{}", sdf.format(new Date()));
        optExpressService.refreshExport(true);
        log.debug("定时任务完成刷新报表时间：{}", sdf.format(new Date()));
    }

}
