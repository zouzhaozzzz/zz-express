package com.zouzhao.opt.manage.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 姚超
 * @DATE: 2023-3-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统计每月XX费用")
public class OptExpressMonthFeeVO {

    private String month;
    private BigDecimal fee;
}
