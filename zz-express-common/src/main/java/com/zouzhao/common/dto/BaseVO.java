package com.zouzhao.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 姚超
 * @DATE: 2023-1-7
 */
@Data
public  class BaseVO {
    @ApiModelProperty(
            value = "记录主键",
            required = false
    )
    private String fdId;
    @ApiModelProperty(
            value = "记录名称",
            required = false
    )
    private String fdName;

    public static BaseVO of(String fdId) {
        BaseVO vo = new BaseVO();
        vo.setFdId(fdId);
        return vo;
    }

    public static BaseVO of(String fdId,String fdName) {
        BaseVO vo = new BaseVO();
        vo.setFdId(fdId);
        vo.setFdName(fdName);
        return vo;
    }
}
