package com.zouzhao.common.dto;

import lombok.Data;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@Data
public class IdVO extends IViewObject{
    public static IdVO of(String fdId) {
        IdVO vo = new IdVO();
        vo.setFdId(fdId);
        return vo;
    }
}
