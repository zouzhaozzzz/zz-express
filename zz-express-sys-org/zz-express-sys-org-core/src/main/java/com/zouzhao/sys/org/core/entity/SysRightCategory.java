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
 * @DATE: 2023-1-20
 */
@Entity
@Table(
        name = "sys_right_category",
        indexes = {@Index(
                columnList = "rightCategoryName"
        )}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRightCategory extends BaseEntity {
    public static final String DEFAULT_CATEGORY_ID = "0";
    public static final String DEFAULT_CATEGORY_NAME = "未分类";

    @Id
    @Column(
            length = 20
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String rightCategoryId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightCategoryCreateTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false,updatable = false,columnDefinition="DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date rightCategoryAlterTime;

    @ApiModelProperty("类别名字")
    private String rightCategoryName;
    private Boolean rightCategoryStatus;

    public static SysRightCategory getDefault() {
        SysRightCategory category = new SysRightCategory();
        category.setRightCategoryId("0");
        category.setRightCategoryName("未分类");
        return category;
    }

    @Override
    public String getId() {
        return this.rightCategoryId;
    }
}
