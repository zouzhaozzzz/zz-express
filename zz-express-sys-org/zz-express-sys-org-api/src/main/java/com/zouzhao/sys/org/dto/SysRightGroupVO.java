package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
@ApiModel("角色VO")
@Data
public class SysRightGroupVO  {
    private String rightGroupId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightGroupCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightGroupAlterTime;

    private String rightGroupName;
    @ApiModelProperty("描述")
    private String fdDesc;
    @ApiModelProperty("类别")
    private SysRightCategoryVO sysRightCategory;
    @ApiModelProperty("类别id")
    private String rightCategoryId;
    @ApiModelProperty("权限")
    private List<SysRightRoleVO> sysRightRoles;



}
