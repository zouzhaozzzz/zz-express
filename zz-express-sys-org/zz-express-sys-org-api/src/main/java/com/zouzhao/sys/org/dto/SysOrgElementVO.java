package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 * @DESCRIPTION:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysOrgElementVO extends BaseEntity {
    private String orgElementId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orgElementAlterTime;
    private String orgElementName;
    private Integer orgElementType;


    private Integer orgElementNo;
    private String orgElementEmail;
    private String orgElementPhone;
    private String orgElementGender;
    private String orgElement_orgId;
    private String orgElementDeptId;
    private String orgElementPostId;
    private String orgElementParentId;
    private String orgElementDesc;
    private String orgElementThisLeader;
    private Boolean orgElementStatus;

    @Override
    public String getId() {
        return this.orgElementId;
    }
}
