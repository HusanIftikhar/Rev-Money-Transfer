package com.revolut.transfers.integration;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransferRestResourceTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8089";
    }

    @Test
    @DisplayName("should pass when valid available accountId is passed")
    public void testGetAccountByIdValidAvailableAccountIdRequest() {
        if(checkApplicationRunning()) {
            Map<String, Object> response = given()
                    .basePath("transactions/accounts/")

                    //  .pathParam("accountId", 70001)
                    .when()
                    .get("/{accountId}", "70001")
                    .then().assertThat().statusCode(200)
                    // .assertThat().body("", null)
                    .assertThat().extract().body().jsonPath().getMap("result");

            assertEquals(response.get("accountId"), 70001);
            assertNotNull(response.get("availableBalance"));
        }
    }


    @Test
    @DisplayName("should return 400 when in valid  accountId is passed")
    public void testGetAccountByIdByInvalidAccountId() {

        if(checkApplicationRunning()) {
            given().basePath("transactions/accounts/").
                    when().get("/{accountId}", "ssss")
                    .then().statusCode(400);
        }
    }


    @Test
    @DisplayName("should return 404 when valid but un available accountId is passed")
    public void testGetAccountByIdByValidAccountId() {
        if(checkApplicationRunning()) {

            given().basePath("transactions/accounts/").
                    when().get("/{accountId}", "13234").
                    then().statusCode(404);
        }
    }

    @Test
    @DisplayName("should transfer  when requested with valid accountIds and amount")
    public void transferAmountWithValidAccountsAndAmount() {
        if(checkApplicationRunning()) {
            String requestBody = "{\"amount\":20.00,\"currencyCode\":\"USD\"}";
            JsonPath response = given().basePath("transactions/accounts/").
                    pathParam("sourceAccountId", 70001).pathParam("targetAccountId", 70002)
                    .body(requestBody)
                    .when().put("/{sourceAccountId}/{targetAccountId}")
                    .then().statusCode(200).extract().jsonPath();
            String message = response.getString("message");
            String result = response.get("result");
            assertEquals(message, "Balance Transferred Successfully");
            assertEquals(result, "SUCCESS");
        } }

    @Test
    @DisplayName("transfer should fail when requested with invalid accountIds and amount")
    public void transferAmountWithInValidAccountsAndAmount() {
        if (checkApplicationRunning()) {
            String requestBody = "{\"amount\":20.00,\"currencyCode\":\"USD\"}";
            JsonPath response = given().basePath("transactions/accounts/").
                    pathParam("sourceAccountId", 123).pathParam("targetAccountId", 70002)
                    .body(requestBody)
                    .when().put("/{sourceAccountId}/{targetAccountId}")
                    .then().statusCode(404).extract().jsonPath();
            String message = response.getString("message");
            String result = response.get("result");
            assertEquals(message, "Account not found for accountId: 123");
            assertEquals(result, "Failure");
        }
    }
    @Test
    @DisplayName("transfer should fail when requested with valid accountIds and Invalid amount")
    public void transferAmountWithValidAccountsAndInAmount() {
        if(checkApplicationRunning()) {
            String requestBody = "{\"amount\":-20.00,\"currencyCode\":\"USD\"}";
            JsonPath response = given().basePath("transactions/accounts/").
                    pathParam("sourceAccountId", 70001).pathParam("targetAccountId", 70002)
                    .body(requestBody)
                    .when().put("/{sourceAccountId}/{targetAccountId}")
                    .then().statusCode(400).extract().jsonPath();
            String message = response.getString("message");
            String result = response.get("result");
            assertEquals(message, "Invalid amount should be greater than 0");
            assertEquals(result, "Failure");
        }}



    @Test
    @DisplayName("withdrawal should be successful when requested with valid accountIds and  amount")
    public void withdrawalAmountWithValidAccountsAndAmount() {
        if(checkApplicationRunning()) {
            String requestBody = "{\"amount\":20.00,\"currencyCode\":\"USD\"}";
            JsonPath response = given().basePath("transactions/accounts/").
                    pathParam("sourceAccountId", 70001)
                    .body(requestBody)
                    .when().put("/{sourceAccountId}/withdrawals")
                    .then().statusCode(200).extract().jsonPath();
            String message = response.getString("message");
            String result = response.get("result");
            assertEquals( "Withdrawal Successful",message);
            assertEquals(result, "SUCCESS");
        }
    }


    @Test
    @DisplayName("withdrawal should fail when requested with invalid accountIds")
    public void withdrawalAmountWithInValidAccounts() {
        if (checkApplicationRunning()) {
            String requestBody = "{\"amount\":20.00,\"currencyCode\":\"USD\"}";
            JsonPath response = given().basePath("transactions/accounts/").
                    pathParam("sourceAccountId", 0)
                    .body(requestBody)
                    .when().put("/{sourceAccountId}/withdrawals")
                    .then().statusCode(404).extract().jsonPath();
            String message = response.getString("message");
            String result = response.get("result");
            assertEquals("Account not found for accountId: 123", message);
            assertEquals("Failure",result);
        }

    }
        @Test
        @DisplayName("Deposit should be successful when requested with valid accountIds")
        public void depositAmountWithValidAccount() {
            if (checkApplicationRunning()) {
                String requestBody = "{\"amount\":20.00,\"currencyCode\":\"USD\"}";
                JsonPath response = given().basePath("transactions/accounts/").
                        pathParam("sourceAccountId", 70001)
                        .body(requestBody)
                        .when().put("/{sourceAccountId}/deposits")
                        .then().statusCode(200).extract().jsonPath();
                String message = response.getString("message");
                String result = response.get("result");
                assertEquals("Deposit successful", message);
                assertEquals("SUCCESS", result);
            }

        }


            @Test
            @DisplayName("Deposit should fail when requested with invalid amount")
            public void depositAmountWithInValidAmount() {
                if (checkApplicationRunning()) {
                    String requestBody = "{\"amount\":-20.00,\"currencyCode\":\"USD\"}";
                    JsonPath response = given().basePath("transactions/accounts/").
                            pathParam("sourceAccountId", 70001)
                            .body(requestBody)
                            .when().put("/{sourceAccountId}/deposits")
                            .then().statusCode(400).extract().jsonPath();
                    String message = response.getString("message");
                    String result = response.get("result");
                    assertEquals("Invalid amount should be greater than 0", message);
                    assertEquals("Failure", result);
                }
            }

                @Test
                @DisplayName("Deposit should fail when requested with invalid currency code")
                public void depositAmountWithInValidCurrencyCode() {
                    if(checkApplicationRunning()) {
                        String requestBody = "{\"amount\":20.00,\"currencyCode\":\"abca\"}";
                        JsonPath response = given().basePath("transactions/accounts/").
                                pathParam("sourceAccountId", 70001)
                                .body(requestBody)
                                .when().put("/{sourceAccountId}/deposits")
                                .then().statusCode(400).extract().jsonPath();
                        String message = response.getString("message");
                        String result = response.get("result");
                        assertEquals( "Invalid currency code",message);
                        assertEquals("Failure", result);
                    }



    }












  /**Utility method to check of application is running on localhost
     the test will ge skip if the application is not available to run integration tests
     Some time due to incompatibility between different version of maven and junit 5  surefire plugin ExecutionCondition
     do not work for this reason to make sure that build should be successful in any case code of the integration test
     only run if this method returns true.
     method simply ping at localhost:8089 to check application is running
     @see com.revolut.transfers.anotations.EnabledIfReachableCondition
    */
  private boolean checkApplicationRunning(){

      try (Socket socket = new Socket()) {
          socket.connect(new InetSocketAddress("127.0.0.1", 8089), 5000);
          return true;
      } catch (IOException e) {
          return false; // Either timeout or unreachable or failed DNS lookup.
      }

  }




}






















