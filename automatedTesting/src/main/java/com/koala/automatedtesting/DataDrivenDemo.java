package com.koala.automatedtesting;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.koala.automatedtesting.pojo.ExcelPojo;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * day05：
 *      数据驱动
 * Create by koala on 2022-10-06
 */
public class DataDrivenDemo {

    @Test(
            dataProvider = "getLoginDatas02"
    )
    public void testLogin(ExcelPojo excelPojo) {
        RestAssured.config = RestAssured.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(NumberReturnType.BIG_DECIMAL));
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        String inputParams = excelPojo.getInputParams();
        String url = excelPojo.getUrl();
        String requestHeader = excelPojo.getRequestHeader();
        Map requestHeaderMap = (Map) JSON.parse(requestHeader);
        String expected = excelPojo.getExpected();
        Map expectedMap = (Map)JSON.parse(expected);
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().body(inputParams).headers(requestHeaderMap).when().post(url, new Object[0])).then()).log().all()).extract().response();
    }

    @DataProvider
    public Object[][] getLoginDatas01() {
        Object[][] datas = new Object[][]{{"13323230000", "123456"}, {"1332323111", "123456"}, {"13323230000", "12345678"}};
        return datas;
    }

    @DataProvider
    public Object[] getLoginDatas02() {
        File file = new File("C:\\Users\\tao_c\\Desktop\\接口文档资料\\api_testcases_futureloan_v1.xls");
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas.toArray();
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\tao_c\\Desktop\\接口文档资料\\api_testcases_futureloan_v1.xls");
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        List<Object> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        Iterator var4 = listDatas.iterator();

        while(var4.hasNext()) {
            Object object = var4.next();
            System.out.println(object);
        }

    }

}