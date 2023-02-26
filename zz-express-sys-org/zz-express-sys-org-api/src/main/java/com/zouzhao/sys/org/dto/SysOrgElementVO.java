package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 * @DESCRIPTION:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysOrgElementVO {
    private String orgElementId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementAlterTime;

    @ApiModelProperty("名称")
    private String orgElementName;
    @ApiModelProperty("0|组织 1|人员")
    private Integer orgElementType;
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
    @ApiModelProperty("组织")
    private SysOrgElementVO orgElementOrg;
    @ApiModelProperty("组织id")
    private String orgElementOrgId;
    @ApiModelProperty("岗位")
    private String orgElementPost;
    @ApiModelProperty("父结点")
    private SysOrgElementVO orgElementParent;
    @ApiModelProperty("父id")
    private String orgElementParentId;
    @ApiModelProperty("是否可用")
    private Boolean orgElementStatus;
    @Column(length = 200)
    @ApiModelProperty("描述")
    private String orgElementDesc;
    @ApiModelProperty("领导")
    private SysOrgElementVO orgElementThisLeader;
    @ApiModelProperty("领导id")
    private String orgElementThisLeaderId;
    @ApiModelProperty("排序")
    private Integer orgElementOrder;

    @ApiModelProperty("登录名")
    private String orgElementLoginName;
    @ApiModelProperty("密码")
    private String orgElementPassword;

    @ApiModelProperty("子节点")
    private List<SysOrgElementVO> children;

}
