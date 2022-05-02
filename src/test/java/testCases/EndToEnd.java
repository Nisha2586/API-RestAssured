package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EndToEnd {
	SoftAssert softAssert;
	HashMap<String,String> payload;
	HashMap<String,String> deletePayLoad;
	String expectedName;
	String expectedprice;
	String expecteddescription;
	String firstProductId;
	
	
	public EndToEnd() {
		softAssert = new SoftAssert();
	}
	
	public Map<String, String> createNewProducts() {
		payload = new HashMap<String, String>();
		payload.put("name", "N's New Product.");
		payload.put("price", "99");
		payload.put("description", "The best pillow for amazing programmers.");
		payload.put("category_id", "2");
		return payload;
	}
	public Map<String,String> deletePayload(){
		deletePayLoad = new HashMap<String,String>();
		deletePayLoad.put("id" , firstProductId);
		return deletePayLoad;
		}
	


	@Test(priority = 0)
	public void createNewProduct() {

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").body(createNewProducts())
				.auth().preemptive()
				.basic("demo@techfios.com", "abc123").log().all().when().log().all().post("/create.php")
				.then().extract().response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		softAssert.assertEquals(actualStatusCode, 201);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		softAssert.assertEquals(actualStatusHeader, "application/json; charset=UTF-8",
				"Response content-Type not matching");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product messages are not matching!");

		softAssert.assertAll();
	}

	@Test(priority=1)
	public void readAllProducts() {

		Response response =
			given().baseUri("https://techfios.com/api-prod/api/product")
				   .header("Content-Type", "application/json; charset=UTF-8")
				   .auth().preemptive().basic("demo@techfios.com", "abc123").log().all().
			when().log().all().get("/read.php").
			then().extract().response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		Assert.assertEquals(actualStatusCode, 200);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		Assert.assertEquals(actualStatusHeader, "application/json; charset=UTF-8");

		String actualResponseBody = response.getBody().asString();
	
		JsonPath jp = new JsonPath(actualResponseBody);
		firstProductId = jp.get("records[0].id");
		if (firstProductId != null) {
			System.out.println("Product exist.");
		} else {
			System.out.println("Product does not exist!");
		}
		System.out.println("First Product Id: " + firstProductId);
		//return firstProductId;
	}

	@Test(priority=2)
	public void deleteOneCreatedProduct() {
//		given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
//		when:  submit api requests(Http method,Endpoint/Resource)
//		then:  validate response(status code, Headers, responseTime, Payload/Body)	
//		EndPont_Url:https://techfios.com/api-prod/api/product/delete.php
//			HTTP method : DEL
//			Authorization:basic auth
//			Header/headers:Content_Type:application/json;charset=UTF-8
//			Status code:200
//			Body:
//			{
//			"id" : "60"
//			}
		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
							.header("Content_Type","application/json;charset=UTF-8")
							.auth().preemptive().basic("demo@techfios.com", "abc123")
							.body(deletePayload())
							.when().log().all().delete("/delete.php")
							.then().extract().response();
		
		int actualResponseCode = response.getStatusCode();
		softAssert.assertEquals(actualResponseCode, 200, "The Status code doesn't match");
		
		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		Assert.assertEquals(actualStatusHeader, "application/json; charset=UTF-8");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody );
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualResponseMessage = jp.get("message");
		softAssert.assertEquals(actualResponseMessage, "Product was deleted.", "Something went wrong");
		softAssert.assertAll();
		
	}
	
	@Test(priority=3)
	public void readDeletedProduct() {
		//createNewProduct.createNewProduct();
//		createNewProduct();
//		firstProductId=readAllProducts();
//		expectedName=payload.get("name");
//		System.out.println("Expected Name : " + expectedName);
//		expectedprice=payload.get("price");
//		System.out.println("Expected Price : " + expectedprice);
//		expecteddescription=payload.get("description");
//		System.out.println("Expected description : " + expecteddescription);
	
		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
							.header("Content-Type", "application/json").auth().preemptive().basic("demo@techfios.com", "abc123")
							.queryParam("id", firstProductId).
							when().log().all().get("/read_one.php").
							then().extract().response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		// Assert.assertEquals(actualStatusCode, 200);
		softAssert.assertEquals(actualStatusCode, 404);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		softAssert.assertEquals(actualStatusHeader, "application/json", "Response content-Type not matching");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body :" + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualResponseMessage = jp.get("message");
		softAssert.assertEquals(actualResponseMessage, "Product does not exist.", "Something went wrong");
		
		
//		  String productId = jp.get("id");
//		  softAssert.assertEquals(productId, firstProductId,"Product Id not matching!");
//		  
//		  String productName = jp.get("name");
//		  softAssert.assertEquals(productName, expectedName,"Product names are not matching!");
//		  
//		  String productPrice = jp.get("price");
//		  softAssert.assertEquals(productPrice, expectedprice,"Product Prices are not matching!");
		  
		  softAssert.assertAll();
		 // deleteOneCreatedProduct();
		  
		 }
	

	

}
