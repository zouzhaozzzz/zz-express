package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@ApiModel("用户账号VO")
@Data
public class SysOrgAccountVO  {
    private String orgAccountId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgAccountCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgAccountAlterTime;

    @ApiModelProperty("登录名")
    private String orgAccountLoginName;
    @ApiModelProperty("密码")
    private String orgAccountPassword;
    @ApiModelProperty("默认人员")
    private String orgAccountDefPersonId;
    @ApiModelProperty("加密方式")
    private String orgAccountEncryption;
    @ApiModelProperty(value = "角色")
    private List<SysRightGroupVO> sysRightGroups;
    @ApiModelProperty("权限")
    private List<SysRightRoleVO> authorities;
    private Boolean orgAccountStatus;

}
