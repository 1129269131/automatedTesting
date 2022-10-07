package com.koala.automatedtesting;

import com.koala.automatedtesting.common.BaseTest;
import com.koala.automatedtesting.data.Environment;
import com.koala.automatedtesting.pojo.ExcelPojo;
import com.koala.automatedtesting.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * day10：
 * 作业-excel里面补充上添加项目模块测试用例
 * Create by koala on 2022-10-07
 */
public class AddloanTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成三个角色的随机手机号码（借款人+管理员）
        String borrowserPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone);
        //读取用例数据-前面4条
        List<ExcelPojo> list = readSpecifyExcelData(4,0,4);
        for (int i=0 ;i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"addLoan");
            //判断是否要提取响应数据
            if(excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }
        }
    }

    @Test(dataProvider = "getAddLoanDatas")
    public void testAddLoan(ExcelPojo excelPojo){
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"addLoan");
        //断言
        assertResponse(excelPojo,res);
    }

    @DataProvider
    public Object[] getAddLoanDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData(4,4);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

    @AfterTest
    public void teardown(){

    }
}