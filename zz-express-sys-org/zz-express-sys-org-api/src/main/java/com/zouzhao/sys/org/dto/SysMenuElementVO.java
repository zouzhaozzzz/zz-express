package com.zouzhao.sys.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
@ApiModel("菜单元素VO")
@Data
public class  SysMenuElementVO  {
    private String menuElementId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date menuElementCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date menuElementAlterTime;

    private String menuElementName;
    private String menuElementPath;
    private String menuElementParentId;
    private String menuElementIcon;
    private String menuElementType;
    private Integer menuElementOrder;
    private Boolean menuElementStatus;

    private List<SysRightRoleVO> sysRightRoles;
    //分级菜单
    private List<SysMenuElementVO> menuChildren;

}
