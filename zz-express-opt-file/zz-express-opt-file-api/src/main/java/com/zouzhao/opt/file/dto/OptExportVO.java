package com.zouzhao.opt.file.dto;

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

    @ApiModelProperty("导入0导出1")
    private Integer exportType;

    private String exportDesc;

    private String exportFileId;

    private String exportMsg;

    @ApiModelProperty("导出条件")
    private OptExportConditionVO condition;

    @ApiModelProperty("总数")
    private int exportCount;

    @ApiModelProperty("进度条")
    private double exportSchedule;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date exportStartTime;

    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date exportFinishTime;
}

