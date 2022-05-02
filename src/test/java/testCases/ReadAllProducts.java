package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class ReadAllProducts {

	@Test
	public String readAllProducts() {
		// baseURI:
//		https://techfios.com/api-prod/api/product   
//			Endpoint/Resource:
//			/read.php
//
//			given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
//			when:  submit api requests(Http method,Endpoint/Resource)
//			then:  validate response(status code, Headers, responseTime, Payload/Body)
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
		//System.out.println("Actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String firstProductId = jp.get("records[0].id");
		if (firstProductId != null) {
			System.out.println("Product exist.");
		} else {
			System.out.println("Product does not exist!");
		}
		return firstProductId;

	}

}
