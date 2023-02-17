package com.zouzhao.sys.org.entity;

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

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Table(name="sys_right_group")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRightGroup extends BaseEntity {
    @Id
    @Column(
            length = 36
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String rightGroupId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightGroupCreateTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightGroupAlterTime;
    private String rightGroupName;
    @ApiModelProperty("描述")
    private String rightGroupDesc;

    @ManyToOne
    @JoinColumn(
            name = "right_category_id"
    )
    @TableField(exist = false)
    @ApiModelProperty("类别")
    private SysRightCategory sysRightCategory;
    @Transient
    @ApiModelProperty("类别id")
    private String rightCategoryId;
    @ManyToMany
    @JoinTable(
            name = "sys_right_gr_rela",
            joinColumns = {@JoinColumn(
                    name = "right_group_id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "right_role_id"
            )}
    )
    @TableField(exist = false)
    @ApiModelProperty("权限")
    private List<SysRightRole> sysRightRoles;

    @Override
    public String getId() {
        return this.rightGroupId;
    }
}
