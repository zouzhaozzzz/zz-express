package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRightRoleVO  implements GrantedAuthority {
    private String rightRoleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleAlterTime;
    private String rightRoleName;
    @ApiModelProperty("权限Code")
    private String rightRoleCode;
    @ApiModelProperty("类别模块")
    private String rightRoleModule;
    @ApiModelProperty("权限描述")
    private String rightRoleDesc;

    @Override
    public String getAuthority() {
        return this.rightRoleCode;
    }

    public String setAuthority(String authority) {
        return null;
    }
}
