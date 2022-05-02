package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

public class CreateNewProduct {
	SoftAssert softAssert;
	
	public CreateNewProduct() {
		softAssert = new SoftAssert();
	}

	@Test(priority = 0)
	public void createNewProduct() {

//			given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
//			when:  submit api requests(Http method,Endpoint/Resource)
//			then:  validate response(status code, Headers, responseTime, Payload/Body)
		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").body("\\src\\main\\java\\data\\CreatePayload.json").auth().preemptive()
				.basic("demo@techfios.com", "abc123").log().all().when().log().all().post("/create.php").then().extract()
				.response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		// Assert.assertEquals(actualStatusCode, 200);
		softAssert.assertEquals(actualStatusCode, 201);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		// Assert.assertEquals(actualStatusHeader, "application/json");
		softAssert.assertEquals(actualStatusHeader, "application/json; charset=UTF-8",
				"Response content-Type not matching");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product messages are not matching!");

		softAssert.assertAll();
	}

}
