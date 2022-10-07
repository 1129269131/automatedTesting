package com.koala.automatedtesting;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import org.testng.annotations.Test;

import java.math.BigDecimal;

/**
 * day04：
 *      RestAssured响应断言
 * Create by koala on 2022-10-06
 */
public class AssertDemo {

    int memberId;
    String token;

    public AssertDemo() {
    }

    @Test
    public void testLogin() {
        RestAssured.config = RestAssured.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(NumberReturnType.BIG_DECIMAL));
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        String json = "{\"mobile_phone\":\"13323231333\",\"pwd\":\"12345678\"}";
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().body(json).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).when().post("/member/login", new Object[0])).then()).log().all()).extract().response();
        int code = (Integer)res.jsonPath().get("code");
        String msg = (String)res.jsonPath().get("msg");
        Assert.assertEquals(code, 0);
        Assert.assertEquals(msg, "OK");
        BigDecimal actual = (BigDecimal)res.jsonPath().get("data.leave_amount");
        BigDecimal expected = BigDecimal.valueOf(622001.0D);
        Assert.assertEquals(actual, expected);
        this.memberId = (Integer)res.jsonPath().get("data.id");
        this.token = (String)res.jsonPath().get("data.token_info.token");
    }

    @Test(
            dependsOnMethods = {"testLogin"}
    )
    public void testRecharge() {
        String jsonData = "{\"member_id\":" + this.memberId + ",\"amount\":10000.00}";
        Response res2 = (Response)((ValidatableResponse)((ValidatableResponse)((Response) RestAssured.given().body(jsonData).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).header("Authorization", "Bearer " + this.token, new Object[0]).when().post("/member/recharge", new Object[0])).then()).log().all()).extract().response();
        BigDecimal actual2 = (BigDecimal)res2.jsonPath().get("data.leave_amount");
        BigDecimal expected2 = BigDecimal.valueOf(632001.0D);
        Assert.assertEquals(actual2, expected2);
    }

}