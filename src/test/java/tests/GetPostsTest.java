package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.ApiConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetPostsTest extends ApiConfig {

    // Test 1: Get all posts and verify response
    @Test
    public void getAllPosts_shouldReturn200AndHundredPosts() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", equalTo(100))
            .body("[0].userId", notNullValue())
            .body("[0].id", equalTo(1))
            .body("[0].title", notNullValue());
    }

    // Test 2: Get a single post by ID
    @Test
    public void getPostById_shouldReturnCorrectPost() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue())
            .body("body", notNullValue());
    }

    // Test 3: Get a post with invalid ID - should return 404
    @Test
    public void getPostById_withInvalidId_shouldReturn404() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts/9999")
        .then()
            .statusCode(404);
    }

    // Test 4: Verify response time is within acceptable limit
    @Test
    public void getAllPosts_shouldRespondWithin2Seconds() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .time(lessThan(2000L));
    }

    // Test 5: Get posts by user ID using query parameter
    @Test
    public void getPostsByUserId_shouldReturnFilteredPosts() {
        given()
            .spec(requestSpec)
            .queryParam("userId", 1)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", equalTo(10))
            .body("userId", everyItem(equalTo(1)));
    }

    // Test 6: Verify response headers
    @Test
    public void getAllPosts_shouldHaveCorrectContentTypeHeader() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .header("Content-Type", containsString("application/json"));
    }
}