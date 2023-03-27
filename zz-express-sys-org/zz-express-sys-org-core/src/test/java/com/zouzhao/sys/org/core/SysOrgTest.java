package com.zouzhao.sys.org.core;

import com.zouzhao.sys.org.core.entity.SysOrgElement;
import com.zouzhao.sys.org.core.service.SysOrgElementService;
import com.zouzhao.sys.org.core.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-3-13
 */
@SpringBootTest
public class SysOrgTest {

    @Autowired
    private SysOrgElementService sysOrgElementService;

    private final static int BATCH_SIZE=10000;
    private final static int ALL_SIZE =90000;


    @Test
    public void person(){
        List<SysOrgElement> dataList=new ArrayList<>(BATCH_SIZE);
        //新建人员
        int max = ALL_SIZE / BATCH_SIZE;
        for (int j = 0; j <max ; j++) {
            for (int i = 0; i <BATCH_SIZE ; i++) {
                SysOrgElement data = new SysOrgElement();
                data.setOrgElementName(RandomUtils.randomName());
                data.setOrgElementType(1);
                data.setOrgElementNo(RandomUtils.randomNumber(0,999999999));
                data.setOrgElementEmail(RandomUtils.randomNumber(100000000,999999999)+"@zouzhao.com");
                data.setOrgElementPhone(RandomUtils.randomNumber(100000000,199999999)+""+RandomUtils.randomNumber(10,99));
                data.setOrgElementGender(RandomUtils.randomGender());
                data.setOrgElementAddress("");
                data.setOrgElementOrgId(RandomUtils.randomOrgId());
                data.setOrgElementStatus(true);
                data.setOrgElementDesc(RandomUtils.randomString(20));
                // data.setOrgElementThisLeaderId("");
                data.setOrgElementOrder(RandomUtils.randomNumber(1000,999999999));
                dataList.add(data);
            }
            sysOrgElementService.saveBatch(dataList,BATCH_SIZE);
            dataList.clear();
        }
        for (int i = 0; i <5566 ; i++) {
            SysOrgElement data = new SysOrgElement();
            data.setOrgElementName(RandomUtils.randomName());
            data.setOrgElementType(1);
            data.setOrgElementNo(RandomUtils.randomNumber(0,999999999));
            data.setOrgElementEmail(RandomUtils.randomNumber(100000000,999999999)+"@zouzhao.com");
            data.setOrgElementPhone(RandomUtils.randomNumber(100000000,199999999)+""+RandomUtils.randomNumber(10,99));
            data.setOrgElementGender(RandomUtils.randomGender());
            data.setOrgElementAddress("");
            data.setOrgElementOrgId(RandomUtils.randomOrgId());
            data.setOrgElementStatus(true);
            data.setOrgElementDesc(RandomUtils.randomString(20));
            // data.setOrgElementThisLeaderId("");
            data.setOrgElementOrder(RandomUtils.randomNumber(1000,999999999));
            dataList.add(data);
        }
        sysOrgElementService.saveBatch(dataList,5566);
        dataList.clear();
    }


}
