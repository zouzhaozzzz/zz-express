package com.zouzhao.sys.org.api;

import com.zouzhao.common.api.IPageApi;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-2-17
 */
public interface ISysOrgElementApi extends IPageApi<SysOrgElementVO> {

    @PostMapping({"/treeData"})
    List<SysOrgElementVO> treeData(SysOrgElementVO request);
}
