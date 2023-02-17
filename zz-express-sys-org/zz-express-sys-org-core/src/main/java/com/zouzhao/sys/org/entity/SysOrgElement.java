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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 * @DESCRIPTION:
 */
@Entity
@Table(name = "sys_org_element")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysOrgElement extends BaseEntity {
    @Id
    @Column(
            length = 36
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String orgElementId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementCreateTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementAlterTime;
    @Column(length = 200, nullable = false)
    @ApiModelProperty("名称")
    private String orgElementName;
    @Column(length = 19, nullable = false)
    @ApiModelProperty("类型")
    private Integer orgElementType;
    @Column(length = 200)
    @ApiModelProperty("编号")
    private Integer orgElementNo;
    @ApiModelProperty("邮箱")
    private String orgElementEmail;
    @ApiModelProperty("电话")
    private String orgElementPhone;
    @ApiModelProperty("性别")
    private String orgElementGender;
    @ApiModelProperty("组织id")
    private String orgElement_orgId;
    @ApiModelProperty("部门id")
    private String orgElementDeptId;
    @ApiModelProperty("岗位id")
    private String orgElementPostId;
    @ApiModelProperty("父id")
    private String orgElementParentId;
    @ApiModelProperty("是否可用")
    private Boolean orgElementStatus;
    @Column(length = 200)
    @ApiModelProperty("描述")
    private String orgElementDesc;
    @ApiModelProperty("领导")
    private String orgElementThisLeader;

    @Override
    public String getId() {
        return this.orgElementId;
    }
}
