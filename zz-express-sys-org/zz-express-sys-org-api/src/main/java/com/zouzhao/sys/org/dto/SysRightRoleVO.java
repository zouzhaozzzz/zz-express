package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.dto.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-22
 */
@Data
public class SysRightRoleVO implements BaseVO {
    private String rightRoleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleAlterTime;
    private Date rightRoleName;
    @ApiModelProperty("权限Code")
    private String rightRoleCode;
    @ApiModelProperty("权限label")
    private String rightRoleLabel;
    @ApiModelProperty("类别模块")
    private String rightRoleModule;
    @ApiModelProperty("类别描述")
    private String rightRoleDesc;
    @Override
    public String getId() {
        return this.rightRoleId;
    }
}
