package com.koala.automatedtesting;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * day03：
 * 作业:
 * 通过RestAssured完成注册->登录->充值->新增项目->项目审核->投资（管理员+投资人+借款人）
 * Create by koala on 2022-10-06
 */
public class HomeWork {
    //全局变量
    String mobilephone = "133232312312";
    String pwd = "123456";
    int type = 1;
    int memberId;
    String token;

    @Test
    public void testRegister() {
        String json = "{\"mobile_phone\":\"" + mobilephone + "\",\"pwd\":\"" + pwd + "\",\"type\":" + type + "}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then()
                        .log().all()
                        .extract().response();
    }

    @Test(dependsOnMethods = "testRegister")
    public void testLogin() {
        String json = "{\"mobile_phone\":\"" + mobilephone + "\",\"pwd\":\"" + pwd + "}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then()
                        .extract().response();
        //1、先来获取id
        memberId = res.jsonPath().get("data.id");
        System.out.println(res.jsonPath().get("data.id") + "");
        //2、获取token
        token = res.jsonPath().get("data.token_info.token");
        System.out.println(token);
    }

    @Test(dependsOnMethods = "testLogin")
    public void testRecharge() {
        //发起“充值”接口请求
        String jsonData = "{\"member_id\":" + memberId + ",\"amount\":100000.00}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " + token).
                        when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                        then().
                        log().all().extract().response();
        System.out.println("当前可用余额:" + res2.jsonPath().get("data.leave_amount"));
    }
}