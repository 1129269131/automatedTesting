package com.koala.automatedtesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

/**
 * day03：
 *      作业:
 *          通过RestAssured完成注册->登录->充值->新增项目->项目审核->投资（管理员+投资人+借款人）
 * Create by koala on 2022-10-06
 */
public class HomeWork {

    String mobilephone = "133232312312";
    String pwd = "123456";
    int type = 1;
    int memberId;
    String token;

    public HomeWork() {
    }

    @Test
    public void testRegister() {
        String json = "{\"mobile_phone\":\"" + this.mobilephone + "\",\"pwd\":\"" + this.pwd + "\",\"type\":" + this.type + "}";
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().body(json).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).when().post("http://api.lemonban.com/futureloan/member/register", new Object[0])).then()).log().all()).extract().response();
    }

    @Test(
            dependsOnMethods = {"testRegister"}
    )
    public void testLogin() {
        String json = "{\"mobile_phone\":\"" + this.mobilephone + "\",\"pwd\":\"" + this.pwd + "}";
        Response res = (Response)((ValidatableResponse)((Response) RestAssured.given().body(json).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).when().post("http://api.lemonban.com/futureloan/member/login", new Object[0])).then()).extract().response();
        this.memberId = (Integer)res.jsonPath().get("data.id");
        System.out.println(res.jsonPath().get("data.id") + "");
        this.token = (String)res.jsonPath().get("data.token_info.token");
        System.out.println(this.token);
    }

    @Test(
            dependsOnMethods = {"testLogin"}
    )
    public void testRecharge() {
        String jsonData = "{\"member_id\":" + this.memberId + ",\"amount\":100000.00}";
        Response res2 = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().body(jsonData).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).header("Authorization", "Bearer " + this.token, new Object[0]).when().post("http://api.lemonban.com/futureloan/member/recharge", new Object[0])).then()).log().all()).extract().response();
        System.out.println("当前可用余额:" + res2.jsonPath().get("data.leave_amount"));
    }

}