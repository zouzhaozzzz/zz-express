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
@ApiModel("统计省份寄件派送个数")
public class OptExpressProvinceVO {

    private String province;
    private Integer count;
}
