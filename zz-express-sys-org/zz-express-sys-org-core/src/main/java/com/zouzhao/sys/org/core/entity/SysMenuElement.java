package com.zouzhao.sys.org.core.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="sys_menu_element")
public class SysMenuElement extends BaseEntity {
    @Id
    @Column(
            length = 20
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String menuElementId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date menuElementCreateTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date menuElementAlterTime;

    private String menuElementName;
    private String menuElementPath;
    private String menuElementParentId;
    private String menuElementIcon;
    private String menuElementType;
    private Integer menuElementOrder=999999999;
    private Boolean menuElementStatus;
    @ManyToMany
    @JoinTable(
            name = "sys_menu_mr_rela",
            joinColumns = {@JoinColumn(
                    name = "menu_element_id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "right_role_id"
            )}
    )
    @TableField(exist = false)
    private List<SysRightRole> sysRightRoles;

    @Override
    public String getId() {
        return this.menuElementId;
    }
}
