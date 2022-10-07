package com.koala.automatedtesting;

import com.koala.automatedtesting.common.BaseTest;
import com.koala.automatedtesting.data.Environment;
import com.koala.automatedtesting.pojo.ExcelPojo;
import com.koala.automatedtesting.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * day08：
 * 投资-业务流程测试
 * Create by koala on 2022-10-07
 */
public class InvestFlowTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成三个角色的随机手机号码（投资人+借款人+管理员）
        String borrowserPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        String investPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone);
        Environment.envData.put("invest_phone",investPhone);
        //读取用例数据从第一条~第九条
        List<ExcelPojo> list = readSpecifyExcelData(5,0,9);
        for (int i=0 ;i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"investFlow");
            //判断是否要提取响应数据
            if(excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }
        }
    }

    @Test
    public void testInvest(){
        List<ExcelPojo> list =readSpecifyExcelData(5,9);
        ExcelPojo excelPojo = list.get(0);
        //替换
        excelPojo = casesReplace(excelPojo);
        //发送投资请求
        Response res = request(excelPojo,"investFlow");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);
    }

    @AfterTest
    public void teardown(){

    }
}