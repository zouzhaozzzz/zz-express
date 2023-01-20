package com.zouzhao.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@Data
public class IdsDTO extends BaseVO{
    private List<String> fdIds;

    public static IdsDTO of(String... fdIds) {
        IdsDTO result = new IdsDTO();
        result.setFdIds((List)(fdIds == null ? new ArrayList() : Arrays.asList(fdIds)));
        return result;
    }

    public static IdsDTO of(List<String> fdIds) {
        IdsDTO result = new IdsDTO();
        result.setFdIds(fdIds);
        return result;
    }
}
