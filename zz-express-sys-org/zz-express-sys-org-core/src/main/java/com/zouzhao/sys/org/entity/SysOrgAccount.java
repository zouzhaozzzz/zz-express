package com.zouzhao.sys.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zouzhao.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_org_account")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysOrgAccount extends BaseEntity implements UserDetails {
    @Id
    @Column(
            length = 36
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private String orgAccountId;
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgAccountCreateTime;
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgAccountAlterTime;

    @ApiModelProperty("登录名")
    @Column(unique = true)
    private String orgAccountLoginName;
    @ApiModelProperty("密码")
    private String orgAccountPassword;
    @ApiModelProperty("默认人员")
    private String orgAccountDefPersonId;
    @ApiModelProperty("加密方式")
    private String orgAccountEncryption;
    private Boolean orgAccountStatus;
    @ManyToMany
    @JoinTable(
            name = "sys_right_go_rela",
            joinColumns = {@JoinColumn(
                    name = "org_account_id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "right_group_id"
            )}
    )
    @TableField(exist = false)
    @JsonIgnore
    private List<SysRightGroup> sysRightGroups;

    @TableField(exist = false)
    @Transient
    @JsonIgnore
    private List<SysRightRole> authorities;


    public void setAuthorities(List<SysRightRole> authorities) {
        this.authorities = authorities;
    }

    @Override
    public List<SysRightRole> getAuthorities() {
        return this.authorities;
    }

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

    @Override
    public String getId() {
        return this.orgAccountId;
    }
}
