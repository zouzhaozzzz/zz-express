package com.zouzhao.opt.manage.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 姚超
 * @DATE: 2023-3-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统计每月XX数量")
public class OptExpressMonthNumVO {

    private String name;
    private String month;
    private int count;
}
