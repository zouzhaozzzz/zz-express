package com.zouzhao.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@Data
public class IdDTO {
    @ApiModelProperty(
            value = "记录主键ids",
            required = false
    )
    private String id;

    public static IdDTO of(String id) {
        IdDTO result = new IdDTO();
        result.setId(id);
        return result;
    }
}
