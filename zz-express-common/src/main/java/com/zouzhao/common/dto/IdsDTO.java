package com.zouzhao.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@Data
public class IdsDTO {
    @ApiModelProperty(
            value = "记录主键ids",
            required = false
    )
    private List<String> ids;

    public static IdsDTO of(String... fdIds) {
        IdsDTO result = new IdsDTO();
        result.setIds((fdIds == null ? null : Arrays.asList(fdIds)));
        return result;
    }

    public static IdsDTO of(List<String> fdIds) {
        IdsDTO result = new IdsDTO();
        result.setIds(fdIds);
        return result;
    }
}
