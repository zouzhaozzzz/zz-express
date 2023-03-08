package com.zouzhao.opt.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
@ApiModel("导入导出记录")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptExportVO {

    private String exportId;

    private String exportName;

    private String exportDesc;

    private String exportFileId;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date exportStartTime;

    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date exportFinishTime;
}

