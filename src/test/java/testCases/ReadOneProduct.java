package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class ReadOneProduct {
	SoftAssert softAssert;

	public ReadOneProduct() {
		softAssert = new SoftAssert();
	}

	@Test
	public void readOneProduct() {
		/*
		 * EndPont_Url:https://techfios.com/api-prod/api/product/read_one.php?id=4078 HTTP
		 * method : GET Authorization:basic auth Query Params:id=3976&category_id=2
		 * Header/headers:Content_Type:application/json Status code:200
		 */
//			baseURI:	https://techfios.com/api-prod/api/product   
//			Endpoint/Resource:/read.php
//  		given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
//			when:  submit api requests(Http method,Endpoint/Resource)
//			then:  validate response(status code, Headers, responseTime, Payload/Body)
		
		
		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
							.header("Content-Type", "application/json").auth().preemptive().basic("demo@techfios.com", "abc123")
							.queryParam("id", "4134").
							when().log().all().get("/read_one.php").
							then().extract().response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		// Assert.assertEquals(actualStatusCode, 200);
		softAssert.assertEquals(actualStatusCode, 200);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		// Assert.assertEquals(actualStatusHeader, "application/json");
		softAssert.assertEquals(actualStatusHeader, "application/json", "Response content-Type not matching");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body :" + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		  String productId = jp.get("id");
		  softAssert.assertEquals(productId, "4134","Product Id not matching!");
		  
		  String productName = jp.get("name");
		  softAssert.assertEquals(productName, "MD's Amazing Pillow 2.0","Product names are not matching!");
		  
		  String productPrice = jp.get("price");
		  softAssert.assertEquals(productPrice, "199","Product Prices are not matching!");
		  System.out.println("product Price: " + productPrice);
		  softAssert.assertAll();
		  
		 }
}
