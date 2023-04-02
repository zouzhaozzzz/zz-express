package com.zouzhao.opt.file.core.entity;

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
 * @DATE: 2023-3-6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "opt_export")
public class OptExport extends BaseEntity {

    @Id
    @Column(
            length = 20
    )
    @TableId(
            type = IdType.ASSIGN_ID
    )
    private String exportId;

    @Column(nullable = false)
    private String exportName;

    @Column(columnDefinition = " int(1) ")
    private Integer exportType;


    private String exportDesc;

    private String exportFileId;

    private String exportMsg;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    private Date exportStartTime;

    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    // @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(columnDefinition = "DATETIME")
    private Date exportFinishTime;

    @Override
    public String getId() {
        return this.exportId;
    }
}

