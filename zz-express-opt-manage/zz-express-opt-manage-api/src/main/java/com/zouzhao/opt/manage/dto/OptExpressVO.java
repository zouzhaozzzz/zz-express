package com.zouzhao.opt.manage.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptExpressVO {

    @ExcelProperty(index = 0)
    @ApiModelProperty("运单号")
    private String expressId;
    private List<String> expressIdList;

    @ExcelProperty(index = 1)
    @ApiModelProperty("运单状态")
    private Integer expressStatus;
    private List<Integer> expressStatusList;

    @ExcelProperty(index = 2)
    @ApiModelProperty("客户类型:“业务员”、“承包区”、“下级网点”、“直营客户”")
    private Short sendCustomerType;

    @ExcelProperty(index = 3)
    @ApiModelProperty("寄件服务方式:“派送”、“自提")
    private Integer sendServiceType;

    @ExcelProperty(index = 4)
    @ApiModelProperty("寄件客户")
    private String sendCustomerId;

    private SysOrgElementVO sendCustomer;

    @ExcelProperty(index = 5)
    @ApiModelProperty("寄件所属公司")
    private String sendCompanyId;

    private SysOrgElementVO sendCompany;

    @ApiModelProperty("收件客户")
    private String consigneeCustomerId;

    private SysOrgElementVO consigneeCustomer;

    @ApiModelProperty("收件所属公司")
    private String consigneeCompanyId;

    private SysOrgElementVO consigneeCompany;


    @ExcelProperty(index = 6)
    @ApiModelProperty("付款方式:寄付 到付")
    private Integer payType;

    @ExcelProperty(index = 7)
    @ApiModelProperty("发货人")
    private String shipper;

    @ExcelProperty(index = 8)
    @ApiModelProperty("发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date shipTime;
    private List<Date> shipTimeList;


    @ExcelProperty(index = 9)
    @ApiModelProperty("发货人手机号码")
    private String shipperPhone;

    @ExcelProperty(index = 10)
    @ApiModelProperty("发货省份")
    private String sendProvince;

    @ExcelProperty(index = 11)
    @ApiModelProperty("发货城市")
    private String sendCity;

    @ExcelProperty(index = 12)
    @ApiModelProperty("发货区县")
    private String sendCounty;

    @ExcelProperty(index = 13)
    @ApiModelProperty("发货人详细地址")
    private String shipperAddr;

    @ExcelProperty(index = 14)
    @ApiModelProperty("收货人")
    private String consignee;

    @ExcelProperty(index = 15)
    @ApiModelProperty("收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date consigneeTime;
    private List<Date> consigneeTimeList;

    @ExcelProperty(index = 16)
    @ApiModelProperty("收货人手机号码")
    private String consigneePhone;

    @ExcelProperty(index = 17)
    @ApiModelProperty("收货省份")
    private String consigneeProvince;

    @ExcelProperty(index = 18)
    @ApiModelProperty("收货城市")
    private String consigneeCity;

    @ExcelProperty(index = 19)
    @ApiModelProperty("收货区县")
    private String consigneeCounty;

    @ExcelProperty(index = 20)
    @ApiModelProperty("收货人详细地址")
    private String consigneeAddr;


    @ExcelProperty(index = 21)
    @ApiModelProperty("物品类型")
    private String objMode;

    @ExcelProperty(index = 22)
    @ApiModelProperty("件数")
    private Integer expressNumber;

    @ExcelProperty(index = 23)
    @ApiModelProperty("网点重量：必填项，数字，小数点后保留2位小数")
    private BigDecimal weight;

    @ExcelProperty(index = 24)
    @ApiModelProperty("体积：数字，小数点后保留2位小数")
    private BigDecimal volume;

    @ExcelProperty(index = 25)
    @ApiModelProperty("体积重：数字，小数点后保留2为小数")
    private BigDecimal volumeWeight;

    @ExcelProperty(index = 26)
    @ApiModelProperty("结算重量：不可填写，如果实际重量大于体积重量，结算重量为实际重量。实际重量小于体积重量，结算重量为体积重量")
    private BigDecimal finalWeight;

    @ExcelProperty(index = 27)
    @ApiModelProperty("包装类型：下拉选择货物的包装类型")
    private Integer packMode;

    @ExcelProperty(index = 28)
    @ApiModelProperty("备注")
    private String expressDesc;

    @ExcelProperty(index = 29)
    @ApiModelProperty("面单类型:电子面单<默认>、普通面单、星联面单、时效面单")
    private Integer faceType;

    @ExcelProperty(index = 30)
    @ApiModelProperty("面单费")
    private BigDecimal faceFee;

    @ExcelProperty(index = 31)
    @ApiModelProperty("退件标识")
    private Integer bounceFlag;

    @ExcelProperty(index = 32)
    @ApiModelProperty("问题件")
    private Integer questionFlag;

    @ExcelProperty(index = 33)
    @ApiModelProperty("数据来源")
    private String expressDataSource;

    @ExcelProperty(index = 34)
    @ApiModelProperty("保险金额")
    private BigDecimal insuredAmount;

    @ExcelProperty(index = 35)
    @ApiModelProperty("保费")
    private BigDecimal premium;

    @ExcelProperty(index = 36)
    @ApiModelProperty("其他费")
    private BigDecimal otherCharges;

    @ExcelProperty(index = 37)
    @ApiModelProperty("结算方式:必选项，下拉选择“现付”、“到付”、“月结”")
    private Integer cleanType;

    @ExcelProperty(index = 38)
    @ApiModelProperty("运费")
    private BigDecimal customerFreight;

    @ExcelProperty(index = 39)
    @ApiModelProperty("寄件代收货款手续费")
    private BigDecimal sendCollectionFee;

    @ExcelProperty(index = 40)
    @ApiModelProperty("总成本")
    private BigDecimal totalCost;

    @ExcelProperty(index = 41)
    @ApiModelProperty("到付手续费成本")
    private BigDecimal sendArriveCost;

    @ExcelProperty(index = 42)
    @ApiModelProperty("中转费成本")
    private BigDecimal sendTransitCost;

    @ExcelProperty(index = 43)
    @ApiModelProperty("面单成本")
    private BigDecimal faceCost;

    @ExcelProperty(index = 44)
    @ApiModelProperty("罚款")
    private BigDecimal sendFine;
}
