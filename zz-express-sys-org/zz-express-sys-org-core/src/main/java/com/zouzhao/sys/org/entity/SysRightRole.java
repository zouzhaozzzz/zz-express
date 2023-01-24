package com.zouzhao.sys.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Entity
@Table(name = "sys_right_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRightRole implements BaseEntity, GrantedAuthority {
    @Id
    @Column(
            length = 36
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private String rightRoleId;
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleCreateTime;
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightRoleAlterTime;

    private String rightRoleName;
    @Column(length = 50)
    @ApiModelProperty("权限Code")
    private String rightRoleCode;
    @Column(length = 200)
    @ApiModelProperty("权限label")
    private String rightRoleLabel;
    @Column(length = 30)
    @ApiModelProperty("类别模块")
    private String rightRoleModule;
    @Column(length = 200)
    @ApiModelProperty("类别描述")
    private String rightRoleDesc;

    @Override
    public String getAuthority() {
        return this.rightRoleCode;
    }

    @Override
    public String getId() {
        return this.rightRoleId;
    }
}
