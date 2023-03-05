package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@ApiModel("用户账号VO")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysOrgAccountVO  implements UserDetails {
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
    @ApiModelProperty(value = "账号状态")
    private Boolean orgAccountStatus;

    @ApiModelProperty(value = "角色")

    @JsonIgnore
    private List<SysRightGroupVO> sysRightGroups;

    @ApiModelProperty("权限")
    // @JsonIgnore
    private List<SysRightRoleVO> authorities;


    public void setAuthorities(List<SysRightRoleVO> authorities) {
        this.authorities = authorities;
    }

    @Override
    public List<SysRightRoleVO> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.orgAccountPassword;
    }

    @Override
    public String getUsername() {
        return this.orgAccountLoginName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
