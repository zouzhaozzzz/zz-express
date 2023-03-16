package com.zouzhao.opt.manage.core.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.core.controller.PageController;
import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.IdsDTO;
import com.zouzhao.common.security.utils.RedisManager;
import com.zouzhao.opt.manage.api.IOptExpressApi;
import com.zouzhao.opt.manage.dto.OptExpressMonthFeeVO;
import com.zouzhao.opt.manage.dto.OptExpressMonthNumVO;
import com.zouzhao.opt.manage.dto.OptExpressProvinceVO;
import com.zouzhao.opt.manage.dto.OptExpressVO;
import com.zouzhao.sys.org.client.SysOrgElementClient;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-3-2
 */
@RestController
@RequestMapping("/data/opt-manage/optExpress")
@Api(
        tags = "运单管理"
)
@PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_DEFAULT','OPT_MANAGE_EXPRESS_ADMIN')")
public class OptExpressController extends BaseController<IOptExpressApi, OptExpressVO> implements PageController<IOptExpressApi, OptExpressVO> {

    @Autowired
    private SysOrgElementClient sysOrgElementClient;
    @Autowired
    private RedisManager redisManager;

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_INSERT','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdDTO add(@RequestBody OptExpressVO vo) {
        return PageController.super.add(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public OptExpressVO get(@RequestBody IdDTO vo) {
        return PageController.super.get(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_UPDATE','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdDTO update(@RequestBody OptExpressVO vo) {
        return PageController.super.update(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_UPDATE','OPT_MANAGE_EXPRESS_ADMIN')")
    @PostMapping("/updateStatusBatch")
    public ResponseEntity<String> updateStatusBatch(@RequestBody OptExpressVO vo) {
        List<String> idList = vo.getExpressIdList();
        Integer status = vo.getExpressStatus();
        if (ObjectUtil.isEmpty(idList) || status == null) throw new MyException("未传入ids或状态");
        getApi().updateStatusBatch(idList, status);
        return new ResponseEntity("修改成功", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_DELETE','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdDTO delete(@RequestBody IdDTO vo) {
        return PageController.super.delete(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_DELETE','OPT_MANAGE_EXPRESS_ADMIN')")
    public IdsDTO deleteAll(@RequestBody IdsDTO ids) {
        return PageController.super.deleteAll(ids);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public List<OptExpressVO> list(@RequestBody OptExpressVO request) {
        return PageController.super.list(request);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public Page<OptExpressVO> page(@RequestBody Page<OptExpressVO> page) {
        Page<OptExpressVO> pageResult=getApi().pagePlus(page);
        List<OptExpressVO> list = pageResult.getRecords();
        //填充寄件客户，寄件公司
        //填充
        if (list != null && list.size() > 0) list.forEach(
                expressVO -> {
                    List<String> ids = new ArrayList<>();
                    //寄件客户
                    String sendCustomerId = expressVO.getSendCustomerId();
                    if (ObjectUtil.isNotEmpty(sendCustomerId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(sendCustomerId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setSendCustomer(result);
                    }
                    //所属公司
                    String sendCompanyId = expressVO.getSendCompanyId();
                    if (ObjectUtil.isNotEmpty(sendCompanyId)) {
                        SysOrgElementVO result = sysOrgElementClient.findVOById(IdDTO.of(sendCompanyId));
                        if (ObjectUtil.isNotEmpty(result)) expressVO.setSendCompany(result);
                    }
                }
        );
        return pageResult;
    }

    @PostMapping("/countExpressNum")
    @ApiOperation("统计符合条件的快递数量")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_EXPRESS_LIST','OPT_MANAGE_EXPRESS_ADMIN')")
    public int countExpressNum(@RequestBody OptExpressVO vo) {
        return getApi().countExpressNum(vo);
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_LIST')")
    @ApiOperation("从redis中拿统计数据")
    @PostMapping("/export")
    public List<Map<String, Object>> export(@RequestBody List<String> keys) {
        List<Map<String, Object>> result = new ArrayList<>();
        keys.forEach(key -> {
            Map<String,Object> map=new HashMap<>();
            switch (key) {
                case "countStatus":
                    map.put("countStatus0", redisManager.getHashValue("report-express", "0"));
                    map.put("countStatus1", redisManager.getHashValue("report-express", "1"));
                    map.put("countStatus2", redisManager.getHashValue("report-express", "2"));
                    map.put("countStatus3", redisManager.getHashValue("report-express", "3"));
                    break;
                case "countByProvinces":
                    map.put("province",  redisManager.getHashValue("report-express", "province"));
                    break;
                case "countFlagByMonth":
                    map.put("questionNumByMonth",  redisManager.getHashValue("report-express", "questionNumByMonth"));
                    map.put("bounceByMonth", redisManager.getHashValue("report-express", "bounceByMonth"));
                    map.put("expressNumByMonth", redisManager.getHashValue("report-express", "expressNumByMonth"));
                    break;
                case "countFeeByMonth":
                    map.put("totalCostByMonth",  redisManager.getHashValue("report-express", "totalCostByMonth"));
                    map.put("premiumByMonth",  redisManager.getHashValue("report-express", "premiumByMonth"));
                    map.put("freightByMonth",  redisManager.getHashValue("report-express", "freightByMonth"));
                    map.put("sendFineByMonth",  redisManager.getHashValue("report-express", "sendFineByMonth"));
                    map.put("income",  redisManager.getHashValue("report-express", "income"));
                    break;
            }
            result.add(map);
        });
        return result;
    }

    @PreAuthorize("hasAnyRole('OPT_MANAGE_REPORT_REFRESH')")
    @ApiOperation("统计快递状态" +
            "省份寄件派送个数" +
            "每月的问题件，退货件，每月的总件数统计" +
            "每月的成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本），保费收入，运费，罚款，收入统计")
    @PostMapping("/refreshExport")
    public ResponseEntity<String> refreshExport() {
        getApi().refreshExport();
        return new ResponseEntity<>("刷新成功", HttpStatus.OK);
    }


}
