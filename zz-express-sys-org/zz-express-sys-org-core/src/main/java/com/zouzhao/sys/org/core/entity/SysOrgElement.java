package com.zouzhao.sys.org.core.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.core.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 * @DESCRIPTION:
 */
@Entity
@Table(name = "sys_org_element",indexes = {
        @Index(columnList = "orgElementOrgId"),
        @Index(columnList = "orgElementThisLeaderId"),
        @Index(columnList = "orgElementParentId"),

})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysOrgElement extends BaseEntity {
    @Id
    @Column(
            length = 20
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String orgElementId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementCreateTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementAlterTime;
    @Column(length = 200, nullable = false)
    @ApiModelProperty("名称")
    private String orgElementName;
    @Column(length = 19, nullable = false)
    @ApiModelProperty("0|组织 1|人员")
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
    @ApiModelProperty("地址")
    private String orgElementAddress;
    @TableField(exist = false)
    @Transient
    @ApiModelProperty("组织")
    private SysOrgElement orgElementOrg;
    @ApiModelProperty("组织id")
    private String orgElementOrgId;
    @ApiModelProperty("岗位")
    private String orgElementPost;
    @TableField(exist = false)
    @Transient
    @ApiModelProperty("父结点")
    private SysOrgElement orgElementParent;
    @ApiModelProperty("父id")
    private String orgElementParentId;
    @ApiModelProperty("是否可用")
    private Boolean orgElementStatus;
    @Column(length = 200)
    @ApiModelProperty("描述")
    private String orgElementDesc;
    @TableField(exist = false)
    @Transient
    @ApiModelProperty("领导")
    private SysOrgElement orgElementThisLeader;
    @ApiModelProperty("领导id")
    private String orgElementThisLeaderId;
    @ApiModelProperty("排序")
    private Integer orgElementOrder=999999999;



    @Override
    public String getId() {
        return this.orgElementId;
    }
}
