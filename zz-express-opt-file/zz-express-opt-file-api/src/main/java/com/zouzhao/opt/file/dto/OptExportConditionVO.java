package com.zouzhao.opt.file.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-8
 */
@Data
@ApiModel("导出条件VO")
public class OptExportConditionVO {
    private List<String> expressIdList;

    private String shipper;

    private String consignee;

    private Integer bounceFlag;

    private Integer questionFlag;

    private List<Integer> expressStatusList;

    private List<Date> shipTimeList;

    private String sendCustomerId;

    private String sendCompanyId;
}
