package com.zouzhao.sys.org.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zouzhao.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collection;

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
    @Column
    private String fdLoginName;
    @Column
    private String fdPassword;
    @Column
    private String fdDefPersonId;
    @Column
    private String fdEncryption;
    @Transient
    @JsonIgnore
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities; // 使用JSON还原的时候，该集合中的元素无法被实例化出来


    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.fdPassword;
    }

    @Override
    public String getUsername() {
        return this.fdLoginName;
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
