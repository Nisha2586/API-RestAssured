package testCases;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class EndToEnd2 {
	EndToEnd endToEnd;
	ReadAllProducts readAllProducts;
	DeleteCreatedProduct deleteCreatedProduct;
	ReadDeletedProduct readDeletedProduct;
	String firstProductId;
	SoftAssert softAssert;
	
	public EndToEnd2() {
		softAssert = new SoftAssert();
	}
	
	@Test
	public void entToEnd2() {
		endToEnd=new EndToEnd();
		readAllProducts=new ReadAllProducts();
		deleteCreatedProduct = new DeleteCreatedProduct();
		readDeletedProduct = new ReadDeletedProduct();
		
		endToEnd.createNewProduct();
		firstProductId=readAllProducts.readAllProducts();
		deleteCreatedProduct.deleteOneCreatedProduct(firstProductId);
		readDeletedProduct.readDeletedProduct(firstProductId);
	}

}
