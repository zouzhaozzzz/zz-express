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
    @Column
    private String fdName;

}
