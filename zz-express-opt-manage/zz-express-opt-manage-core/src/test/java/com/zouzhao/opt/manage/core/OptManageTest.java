package com.zouzhao.opt.manage.core;

import com.zouzhao.opt.manage.core.entity.OptExpress;
import com.zouzhao.opt.manage.core.service.OptExpressService;
import com.zouzhao.opt.manage.core.utils.RandomUtils;
import com.zouzhao.sys.org.client.SysOrgElementClient;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-14
 */
@SpringBootTest
public class OptManageTest {

    private final static int BATCH_SIZE = 10000;
    private final static int ALL_SIZE = 23_150_000;

    @Autowired
    private OptExpressService optExpressService;
    @Autowired
    private SysOrgElementClient sysOrgElementClient;

    @Test
    public void express() {
        //构造组织数据
        List<OptExpress> dataList = new ArrayList<>(BATCH_SIZE);
        int max = ALL_SIZE / BATCH_SIZE;
        for (int j = 0; j < max; j++) {
            for (int i = 0; i < BATCH_SIZE; i++) {
                generateData(dataList);
            }
            optExpressService.saveBatch(dataList, BATCH_SIZE);
            dataList.clear();
        }
        for (int i = 0; i < 6689; i++) {
            generateData(dataList);
        }
        optExpressService.saveBatch(dataList, 6689);
        dataList.clear();
    }

    private void generateData(List<OptExpress> dataList) {
        OptExpress data = new OptExpress();
        data.setExpressStatus(RandomUtils.randomNumber(0, 4));
        data.setSendCustomerType(RandomUtils.randomNumber(0, 4));
        data.setSendServiceType(RandomUtils.randomNumber(0, 2));
        //随机人员
        SysOrgElementVO vo=sysOrgElementClient.randomPerson(RandomUtils.randomNumber(0,95569));
        data.setSendCustomerId(vo.getOrgElementId());
        data.setSendCompanyId(vo.getOrgElementOrgId());
        //付款方式
        data.setPayType(RandomUtils.randomNumber(0, 2));
        data.setShipper(RandomUtils.randomName());
        data.setShipperPhone(RandomUtils.randomNumber(100000000, 199999999) + "" + RandomUtils.randomNumber(10, 99));
        //省份
        String[] provinces=RandomUtils.randomProvince();
        //发货时间
        data.setShipTime(RandomUtils.randomTime2022());
        data.setSendProvince(provinces[0]);
        data.setSendCity(provinces[1]);
        data.setSendCounty(provinces[2]);
        data.setShipperAddr(RandomUtils.randomString(20));
        data.setConsignee(RandomUtils.randomName());
        //收货时间
        if (data.getExpressStatus() == 3) {
            data.setConsigneeTime(RandomUtils.randomAddTime(data.getShipTime()));
        }
        data.setConsigneePhone(RandomUtils.randomNumber(100000000, 199999999) + "" + RandomUtils.randomNumber(10, 99));
        String[] provinces2=RandomUtils.randomProvince();
        data.setConsigneeProvince(provinces2[0]);
        data.setConsigneeCity(provinces2[1]);
        data.setConsigneeCounty(provinces2[2]);
        data.setConsigneeAddr(RandomUtils.randomString(20));
        data.setObjMode("");
        //数量 5-3-2
        int numFlag = RandomUtils.randomNumber(0, 10);
        if(numFlag<5) {
            data.setExpressNumber(1);
        }else  if(numFlag<8){
            data.setExpressNumber(RandomUtils.randomNumber(1, 100));
        }else {
            data.setExpressNumber(RandomUtils.randomNumber(100, 1000));
        }

        //重量
        double weight = RandomUtils.randomLessWeight(4);
        BigDecimal w1 = new BigDecimal(weight);
        data.setWeight(w1);
        double volume = RandomUtils.randomLessVolume(4);
        data.setVolume(new BigDecimal(volume));
        double volumeWeight = volume / 1.3;
        BigDecimal w2 = new BigDecimal(volumeWeight);
        data.setVolumeWeight(w2);
        if (w1.compareTo(w2) < 0) {
            data.setFinalWeight(w2);
        } else data.setFinalWeight(w1);

        data.setPackMode(RandomUtils.randomNumber(0, 4));
        data.setExpressDesc(RandomUtils.randomString(20));
        data.setFaceType(RandomUtils.randomNumber(0, 4));
        data.setFaceFee(BigDecimal.valueOf(RandomUtils.randomDouble()));
        data.setBounceFlag(RandomUtils.randomMoreFalseFlag());
        data.setQuestionFlag(RandomUtils.randomMoreFalseFlag());
        double f1 = RandomUtils.randomFee(0, 1000);
        data.setInsuredAmount(new BigDecimal(f1 * 10));
        data.setPremium(new BigDecimal(f1));
        data.setOtherCharges(BigDecimal.valueOf(RandomUtils.randomFee(0, 10)));
        data.setCleanType(RandomUtils.randomNumber(0, 3));
        //运费 小于1kg
        if (data.getFinalWeight().compareTo(new BigDecimal(1)) < 0) {
            data.setCustomerFreight(new BigDecimal(12));
        } else {
            BigDecimal add = (data.getFinalWeight().subtract(new BigDecimal(1))).multiply(new BigDecimal(4)).add(new BigDecimal(12));
            data.setCustomerFreight(add);
        }
        data.setSendCollectionFee(data.getCustomerFreight().multiply(new BigDecimal("0.01")));
        if (data.getPayType() == 1) {
            data.setSendArriveCost(data.getCustomerFreight().multiply(new BigDecimal("0.01")));
        } else data.setSendArriveCost(new BigDecimal(0));
        data.setSendTransitCost(data.getCustomerFreight().multiply(BigDecimal.valueOf(RandomUtils.randomDouble())));
        data.setFaceCost(BigDecimal.valueOf(RandomUtils.randomDouble()));
        if (RandomUtils.randomNumber(0, 10) <= 8) {
            data.setSendFine(new BigDecimal(0));
        } else {
            data.setSendFine(BigDecimal.valueOf(RandomUtils.randomFee(1, 200)));
        }
        //成本费（寄件代收货款手续费、到付手续费成本、中转费成本、面单成本）
        data.setTotalCost(data.getSendCollectionFee().add(data.getSendArriveCost().add(data.getSendTransitCost().add(data.getFaceCost()))));
        dataList.add(data);
    }


}
