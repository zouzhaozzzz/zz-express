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
@ApiModel("统计XX个数")
public class OptExpressNumVO {

    private String name;
    private int count;
}
