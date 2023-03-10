package com.zouzhao.opt.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptFileVO {

    private String fileId;

    private String filePath;

    @ApiModelProperty("上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date fileTime;
}
