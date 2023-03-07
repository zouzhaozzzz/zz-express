package com.zouzhao.opt.manage.core.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "opt_file")
public class OptFile extends BaseEntity {
    @Id
    @TableId(
            type = IdType.ASSIGN_ID
    )
    @Column(nullable = false,length = 20)
    private String fileId;

    private String filePath;

    @ApiModelProperty("上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    private Date fileTime;

    @Override
    public String getId() {
        return this.fileId;
    }
}
