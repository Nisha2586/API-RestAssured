package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {
	SoftAssert softAssert;
	String firstProductId;
	HashMap<String, String> updatedpayload;
	String updatedName;
	String updatedprice;
	String updateddescription;

	public UpdateOneProduct() {
		softAssert = new SoftAssert();
	}

	public Map<String, String> updatedPayload() {
		updatedpayload = new HashMap<String, String>();
		updatedpayload.put("id", firstProductId);// this id should be the first product id.
		updatedpayload.put("name", "N's Updated Product.");
		updatedpayload.put("price", "999");
		updatedpayload.put("description", "The updated pillow for amazing programmers.");
		//updatedpayload.put("category_id", "2");
		return updatedpayload;
	}

	@Test(priority =0)
	public void readAllProducts() {

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
				.basic("demo@techfios.com", "abc123").log().all().when().log().all().get("/read.php").then().extract()
				.response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		Assert.assertEquals(actualStatusCode, 200);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		Assert.assertEquals(actualStatusHeader, "application/json; charset=UTF-8");

		String actualResponseBody = response.getBody().asString();
		//System.out.println("Actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		firstProductId = jp.get("records[0].id");
		if (firstProductId != null) {
			System.out.println("Product exist.");
		} else {
			System.out.println("Product does not exist!");
		}
		System.out.println("First Product Id: " + firstProductId);
	}

	@Test
	public void updateOneProduct() {
//		baseURI:https://techfios.com/api-prod/api/product
//		Endpoint/Resource:/update.php
//			HTTP method : PUT
//			Authorization:basic auth
//			Header/headers:Content_Type:application/json;charset=UTF-8
//			Status code:200
//			Body:
//			given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
//			when:  submit api requests(Http method,Endpoint/Resource)
//			then:  validate response(status code, Headers, responseTime, Payload/Body)

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").body(updatedPayload()).auth().preemptive()
				.basic("demo@techfios.com", "abc123").log().all().when().log().all().put("/update.php").then().extract()
				.response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		Assert.assertEquals(actualStatusCode, 200);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		Assert.assertEquals(actualStatusHeader, "application/json; charset=UTF-8");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was updated.", "Product messages are not matching!");

		softAssert.assertAll();

	}

	@Test
	public void readUpdatedProduct() {
		updatedPayload();
		updatedName = updatedpayload.get("name");
		System.out.println("Expected Name : " + updatedName);
		updatedprice = updatedpayload.get("price");
		System.out.println("Expected Price : " + updatedprice);
		updateddescription = updatedpayload.get("description");
		System.out.println("Expected description : " + updateddescription);

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json").auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", firstProductId).when().log().all().get("/read_one.php").then().extract().response();

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
		softAssert.assertEquals(productId, firstProductId, "Product Id not matching!");

		String productName = jp.get("name");
		softAssert.assertEquals(productName, updatedName, "Product names are not matching!");

		String productPrice = jp.get("price");
		softAssert.assertEquals(productPrice, updatedprice, "Product Prices are not matching!");
		
		String productDescription = jp.get("description");
		softAssert.assertEquals(productDescription, updateddescription, "Product Description are not matching!");
		
		softAssert.assertAll();

	}

}
