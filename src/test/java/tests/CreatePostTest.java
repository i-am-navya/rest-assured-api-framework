package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConfig;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreatePostTest extends ApiConfig {

    // Test 1: Create a new post successfully
    @Test
    public void createPost_shouldReturn201AndCreatedPost() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Test Post Title");
        requestBody.put("body", "This is the body of the test post");
        requestBody.put("userId", 1);

        given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("Test Post Title"))
            .body("body", equalTo("This is the body of the test post"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }

    // Test 2: Create post with missing title
    @Test
    public void createPost_withMissingTitle_shouldStillReturn201() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("body", "Body without title");
        requestBody.put("userId", 1);

        given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("id", notNullValue());
    }

    // Test 3: Create post and verify response time
    @Test
    public void createPost_shouldRespondWithin3Seconds() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Performance Test Post");
        requestBody.put("body", "Testing response time for POST request");
        requestBody.put("userId", 1);

        given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .time(lessThan(3000L));
    }

    // Test 4: Create post and extract response values
    @Test
    public void createPost_shouldReturnNewPostId() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Extract ID Test");
        requestBody.put("body", "Testing ID extraction from response");
        requestBody.put("userId", 2);

        Response response = given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .extract()
            .response();

        int newPostId = response.jsonPath().getInt("id");
        System.out.println("New post created with ID: " + newPostId);
        Assert.assertTrue(newPostId > 0, "Post ID should be greater than 0");
    }
}