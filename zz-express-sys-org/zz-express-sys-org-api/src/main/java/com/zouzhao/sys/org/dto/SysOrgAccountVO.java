package com.zouzhao.sys.org.dto;

import com.zouzhao.common.dto.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@ApiModel("用户账号VO")
@Data
public class SysOrgAccountVO extends BaseVO {
    @ApiModelProperty("登录名")
    private String fdLoginName;
    @ApiModelProperty("密码")
    private String fdPassword;
    @ApiModelProperty("默认人员")
    private BaseVO fdDefPersonId;
    @ApiModelProperty("加密方式")
    private String fdEncryption;
    @ApiModelProperty("权限")
    private Collection<? extends GrantedAuthority> authorities;

}
