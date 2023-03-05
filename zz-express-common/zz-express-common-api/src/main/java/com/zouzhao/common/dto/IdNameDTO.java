package com.zouzhao.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@Data
public class IdNameDTO {
    @ApiModelProperty(
            value = "记录主键id",
            required = false
    )
    private String id;

    @ApiModelProperty(
            value = "记录主键name",
            required = false
    )
    private String name;

    public static IdNameDTO of(String id) {
        IdNameDTO result = new IdNameDTO();
        result.setId(id);
        return result;
    }

    public static IdNameDTO of(String id,String name) {
        IdNameDTO result = new IdNameDTO();
        result.setId(id);
        result.setName(name);
        return result;
    }
}
