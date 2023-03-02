package com.zouzhao.sys.org.core.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Entity
@Table(name = "sys_right_role",
        indexes = {
                @Index(columnList = "rightRoleModule"),
                @Index(columnList = "rightRoleCode",unique = true)
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRightRole extends BaseEntity implements GrantedAuthority {
    @Id
    @Column(
            length = 20
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String rightRoleId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleCreateTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleAlterTime;

    private String rightRoleName;
    @Column(length = 50)
    @ApiModelProperty("权限Code")
    private String rightRoleCode;
    @Column(length = 30)
    @ApiModelProperty("类别模块")
    private String rightRoleModule = "未分类";
    @Column(length = 200)
    @ApiModelProperty("权限描述")
    private String rightRoleDesc;

    @Override
    public String getAuthority() {
        return this.rightRoleCode;
    }

    @Override
    public String getId() {
        return this.rightRoleId;
    }


    public void setAuthority(String Authority) {
        this.rightRoleCode=Authority;
    }


    public void setId(String id) {
        this.rightRoleId = id;
    }
}
