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

import java.io.FileNotFoundException;
import java.util.List;

/**
 * day11：
 *      注册-数据库断言
 * Create by koala on 2022-10-07
 */
public class RegisterTest extends BaseTest {

    @BeforeClass
    public void setup() throws InterruptedException {
        //随机生成没有注册过的手机号码
        String phone1 = PhoneRandomUtil.getUnregisterPhone();
        Thread.sleep(500);
        String phone2 = PhoneRandomUtil.getUnregisterPhone();
        Thread.sleep(500);
        String phone3 = PhoneRandomUtil.getUnregisterPhone();
        Thread.sleep(500);
        //保存到环境变量中
        Environment.envData.put("phone1",phone1);
        Environment.envData.put("phone2",phone2);
        Environment.envData.put("phone3",phone3);
    }

    @Test(dataProvider = "getRegisterDatas")
    public void testRegister(ExcelPojo excelPojo) throws FileNotFoundException {
        excelPojo = casesReplace(excelPojo);
        //发起注册请求
        Response res = request(excelPojo,"register");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);

    }

    @DataProvider
    public Object[] getRegisterDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData(1,0);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

    @AfterTest
    public void teardown(){
        //清空掉环境变量
        Environment.envData.clear();
    }

}