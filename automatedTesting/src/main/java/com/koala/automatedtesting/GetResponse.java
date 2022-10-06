package com.koala.automatedtesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.List;

/**
 * day02：
 *      获取响应
 * Create by koala on 2022-10-06
 */
public class GetResponse {

    @Test
    public void getResponseHeader() {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().when().post("http://www.httpbin.org/post", new Object[0])).then()).log().all()).extract().response();
        System.out.println("接口的响应时间:" + res.time());
        System.out.println(res.getHeader("Content-Type"));
    }

    @Test
    public void getResponseJson01() {
        String json = "{\"mobile_phone\":\"13323231111\",\"pwd\":\"12345678\"}";
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().body(json).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v1", new Object[0]).when().post("http://api.lemonban.com/futureloan/member/login", new Object[0])).then()).log().all()).extract().response();
        System.out.println(res.jsonPath().get("data.id") + "");
    }

    @Test
    public void getResponseJson02() {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().when().get("http://www.httpbin.org/json", new Object[0])).then()).log().all()).extract().response();
        System.out.println(res.jsonPath().get("slideshow.slides.title") + "");
        List<String> list = res.jsonPath().getList("slideshow.slides.title");
        System.out.println((String)list.get(0));
        System.out.println((String)list.get(1));
    }

    @Test
    public void getResponseHtml() {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().when().get("http://www.baidu.com", new Object[0])).then()).log().all()).extract().response();
        System.out.println(res.htmlPath().get("html.head.meta[0].@http-equiv") + "");
        System.out.println(res.htmlPath().get("html.head.meta[0].@content") + "");
        System.out.println(res.htmlPath().getList("html.head.meta"));
    }

    @Test
    public void getResponseXml() {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().when().get("http://www.httpbin.org/xml", new Object[0])).then()).log().all()).extract().response();
        System.out.println(res.xmlPath().get("slideshow.slide[1].title") + "");
        System.out.println(res.xmlPath().get("slideshow.slide[1].@type") + "");
    }

    @Test
    public void loginRecharge() {
        String json = "{\"mobile_phone\":\"13323231111\",\"pwd\":\"12345678\"}";
        Response res = (Response)((ValidatableResponse)((Response) RestAssured.given().body(json).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).when().post("http://api.lemonban.com/futureloan/member/login", new Object[0])).then()).extract().response();
        int memberId = (Integer)res.jsonPath().get("data.id");
        System.out.println(memberId);
        String token = (String)res.jsonPath().get("data.token_info.token");
        System.out.println(token);
        String jsonData = "{\"member_id\":" + memberId + ",\"amount\":100000.00}";
        Response res2 = (Response)((ValidatableResponse)((ValidatableResponse)((Response)RestAssured.given().body(jsonData).header("Content-Type", "application/json", new Object[0]).header("X-Lemonban-Media-Type", "lemonban.v2", new Object[0]).header("Authorization", "Bearer " + token, new Object[0]).when().post("http://api.lemonban.com/futureloan/member/recharge", new Object[0])).then()).log().all()).extract().response();
        System.out.println("当前可用余额:" + res2.jsonPath().get("data.leave_amount"));
    }

}