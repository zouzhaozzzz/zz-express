package com.zouzhao.sys.org.entity;

import com.zouzhao.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 * @DESCRIPTION:
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_org_element")
@Data
public class SysOrgElement extends BaseEntity {
    @Column(length = 1)
    private Integer fdTreeLevel;
    @Column(length = 200, nullable = false)
    private String fdName;
    @Column(length = 19)
    private Integer fdOrgType;
    @Column(length = 200)
    private Integer fdNo;
    @Column
    private String fdEmail;
    @Column
    private String fdPhone;
    @Column
    private String fdGender;
    @Column
    private String fdOrgId;
    @Column
    private String fdDeptId;
    @Column
    private String fdPostId;
    @Column
    private String fdParentId;
    @Column(nullable = false)
    private Boolean fdIsAvailable;
    @Column(length = 200)
    private String fdRemark;
    @Column
    private String fdThisLeader;
}
