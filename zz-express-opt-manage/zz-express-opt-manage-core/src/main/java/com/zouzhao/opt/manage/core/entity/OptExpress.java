package com.zouzhao.opt.manage.core.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 姚超
 * @DATE: 2023-3-1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "opt_express")
public class OptExpress extends BaseEntity {

    @Id
    @ApiModelProperty("运单号")
    @TableId
    @Column(nullable = false,length = 20)
    private String expressId;

    @ApiModelProperty("运单状态")
    @Column(nullable = false,columnDefinition = "int(1)")
    private Integer expressStatus;

    @ApiModelProperty("客户类型:“业务员”、“承包区”、“下级网点”、“直营客户”")
    @Column(nullable = false,columnDefinition="int(1)")
    private Short sendCustomerType;

    @ApiModelProperty("寄件服务方式:“派送”、“自提")
    @Column(nullable = false,columnDefinition="int(1)")
    private Integer sendServiceType;

    @ApiModelProperty("寄件客户")
    @Column(length = 20,nullable = false)
    private String sendCustomerId;

    @ApiModelProperty("寄件所属公司")
    @Column(length = 20)
    private String sendCompanyId;

    @ApiModelProperty("付款方式:寄付 到付")
    @Column(columnDefinition = "int(1)",nullable = false)
    private Integer payType;

    @ApiModelProperty("发货人")
    @Column(length = 20,nullable = false)
    private String shipper;

    @ApiModelProperty("发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME  DEFAULT CURRENT_TIMESTAMP")
    private Date shipTime;

    @ApiModelProperty("发货人手机号码")
    @Column(length = 11,nullable = false)
    private String shipperPhone;

    @ApiModelProperty("发货省份")
    @Column(length = 20)
    private String sendProvince;

    @ApiModelProperty("发货城市")
    @Column(length = 20)
    private String sendCity;

    @ApiModelProperty("发货区县")
    @Column(length = 20)
    private String sendCounty;

    @ApiModelProperty("发货人详细地址")
    private String shipperAddr;

    @ApiModelProperty("收货人")
    @Column(length = 20,nullable = false)
    private String consignee;

    @ApiModelProperty("收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(columnDefinition = "DATETIME")
    private Date consigneeTime;

    @ApiModelProperty("收货人手机号码")
    @Column(length = 11,nullable = false)
    private String consigneePhone;

    @ApiModelProperty("收货省份")
    @Column(length = 20)
    private String consigneeProvince;

    @ApiModelProperty("收货城市")
    @Column(length = 20)
    private String consigneeCity;

    @ApiModelProperty("收货区县")
    @Column(length = 20)
    private String consigneeCounty;

    @ApiModelProperty("收货人详细地址")
    private String consigneeAddr;


    @ApiModelProperty("物品类型")
    @Column(length = 20,nullable = false)
    private String objMode;

    @ApiModelProperty("件数")
    @Column(nullable = false)
    private Integer expressNumber;

    @ApiModelProperty("网点重量：必填项，数字，小数点后保留2位小数")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal weight;

    @ApiModelProperty("体积：数字，小数点后保留2位小数")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal volume;

    @ApiModelProperty("体积重：数字，小数点后保留2为小数")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal volumeWeight;

    @ApiModelProperty("结算重量：不可填写，如果实际重量大于体积重量，结算重量为实际重量。实际重量小于体积重量，结算重量为体积重量")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal finalWeight;

    @ApiModelProperty("包装类型：下拉选择货物的包装类型")
    @Column(columnDefinition = "int(1)")
    private Integer packMode;

    @ApiModelProperty("备注")
    private String expressDesc;

    @ApiModelProperty("面单类型:电子面单<默认>、普通面单、星联面单、时效面单")
    @Column(columnDefinition = "int(1)",nullable = false)
    private Integer faceType;

    @ApiModelProperty("面单费")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal faceFee;

    @ApiModelProperty("退件标识")
    @Column(columnDefinition = "int(1)")
    private Integer bounceFlag;

    @ApiModelProperty("问题件")
    @Column(columnDefinition = "int(1)")
    private Integer questionFlag;

    @ApiModelProperty("数据来源")
    @Column(length = 20)
    private String expressDataSource;

    @ApiModelProperty("保险金额")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal insuredAmount;

    @ApiModelProperty("保费")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal premium;

    @ApiModelProperty("其他费")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal otherCharges;

    @ApiModelProperty("结算方式:必选项，下拉选择“现付”、“到付”、“月结”")
    @Column(columnDefinition = "int(1)",nullable = false)
    private Integer cleanType;

    @ApiModelProperty("运费")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal customerFreight;

    @ApiModelProperty("寄件代收货款手续费")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal sendCollectionFee;

    @ApiModelProperty("总成本")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal totalCost;

    @ApiModelProperty("到付手续费成本")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal sendArriveCost;

    @ApiModelProperty("中转费成本")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal sendTransitCost;

    @ApiModelProperty("面单成本")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal faceCost;

    @ApiModelProperty("罚款")
    @Column(columnDefinition = "decimal(20,2)")
    private BigDecimal sendFine;

    @Override
    public String getId() {
        return this.expressId;
    }
}
