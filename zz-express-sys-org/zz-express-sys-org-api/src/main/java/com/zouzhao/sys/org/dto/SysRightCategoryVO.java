package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.dto.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-22
 */
@ApiModel("权限类别VO")
@Data
public class SysRightCategoryVO implements BaseVO {
    private String rightCategoryId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightCategoryCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightCategoryAlterTime;

    @ApiModelProperty("类别名字")
    private String rightCategoryName;

    @Override
    public String getId() {
        return this.rightCategoryId;
    }
}
