package testCases;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadDeletedProduct {
SoftAssert softAssert;
	
	public ReadDeletedProduct() {
		softAssert=new SoftAssert();	
	}

	@Test
	public void readDeletedProduct(String productID) {
	
		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
							.header("Content-Type", "application/json").auth().preemptive().basic("demo@techfios.com", "abc123")
							.queryParam("id", productID).
							when().log().all().get("/read_one.php").
							then().extract().response();

		int actualStatusCode = response.getStatusCode();
		System.out.println("Actual Status Code:" + actualStatusCode);
		softAssert.assertEquals(actualStatusCode, 404);

		String actualStatusHeader = response.header("Content-Type");
		System.out.println("Actual Status Header:" + actualStatusHeader);
		softAssert.assertEquals(actualStatusHeader, "application/json", "Response content-Type not matching");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body :" + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualResponseMessage = jp.get("message");
		softAssert.assertEquals(actualResponseMessage, "Product does not exist.", "Something went wrong");
		softAssert.assertAll();
				  
		 }

}
