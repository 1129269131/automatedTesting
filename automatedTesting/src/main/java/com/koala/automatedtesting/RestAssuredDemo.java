package com.koala.automatedtesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.io.File;
import org.testng.annotations.Test;

/**
 * day01：
 *      RestAssured框架基础使用
 * Create by koala on 2022-10-06
 */
public class RestAssuredDemo {

    @Test
    public void firstGetRequest() {
        ((ValidatableResponse)((Response)RestAssured.given().when().get("https://www.baidu.com", new Object[0])).then()).log().body();
    }

    @Test
    public void getDemo01() {
        ((ValidatableResponse)((Response)RestAssured.given().queryParam("mobilephone", new Object[]{"13323234545"}).queryParam("pwd", new Object[]{"123456"}).when().get("http://www.httpbin.org/get", new Object[0])).then()).log().body();
    }

    @Test
    public void postDemo01() {
        ((ValidatableResponse)((Response)RestAssured.given().formParam("mobilephone", new Object[]{"13323234545"}).formParam("pwd", new Object[]{"123456"}).contentType("application/x-www-form-urlencoded").when().post("http://www.httpbin.org/post", new Object[0])).then()).log().body();
    }

    @Test
    public void postDemo02() {
        String jsonData = "{\"mobilephone\":\"13323234545\",\"pwd\":\"123456\"}";
        ((ValidatableResponse)((Response)RestAssured.given().body(jsonData).contentType("application/json").when().post("http://www.httpbin.org/post", new Object[0])).then()).log().body();
    }

    @Test
    public void postDemo03() {
        String xmlData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<suite>\n    <class>测试xml</class>\n</suite>";
        ((ValidatableResponse)((Response)RestAssured.given().body(xmlData).contentType("application/xml").when().post("http://www.httpbin.org/post", new Object[0])).then()).log().body();
    }

    @Test
    public void postDemo04() {
        ((ValidatableResponse)((Response)RestAssured.given().multiPart(new File("./document/text.txt")).when().post("http://www.httpbin.org/post", new Object[0])).then()).log().body();
    }

}