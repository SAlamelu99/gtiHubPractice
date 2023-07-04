package com.basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;


public class Basics {

	public static void main(String[] args) {
		//given - all input details
		//When - submit the request
		//Then - validate the response

		//Post
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace()).when().post("/maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.41 (Ubuntu)").extract().response().body().asString();	
	
		//for parsing the reponse
		JsonPath js = new JsonPath(response);
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		//update
		String newAddress = "70 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}\r\n" + 
				"").when().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//get
		String updateAddress = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId).header("Content-Type","application/json").when().get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract().response().body().asString();
		JsonPath js1 = new JsonPath(updateAddress);
		String actualAddress = js1.getString("address");
		
		Assert.assertTrue(actualAddress.equals(newAddress));
		
		
		
		
	
	
	}

}
