package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteCreatedProduct {
	SoftAssert softAssert;
	
	public DeleteCreatedProduct() {
		softAssert=new SoftAssert();	
	}
	
	@Test
	public void deleteOneCreatedProduct(String productID) {
		HashMap deletePayLoadMap = new HashMap<String,String>();
		deletePayLoadMap.put("id" , productID);

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
							.header("Content_Type","application/json;charset=UTF-8")
							.auth().preemptive().basic("demo@techfios.com", "abc123")
							.body(deletePayLoadMap)
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

}
