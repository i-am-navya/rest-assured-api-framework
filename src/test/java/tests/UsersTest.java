package tests;

import org.testng.annotations.Test;
import utils.ApiConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UsersTest extends ApiConfig {

    // Test 1: Get all users
    @Test
    public void getAllUsers_shouldReturn200AndTenUsers() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("size()", equalTo(10))
            .body("[0].id", notNullValue())
            .body("[0].name", notNullValue())
            .body("[0].email", notNullValue());
    }

    // Test 2: Get a single user by ID
    @Test
    public void getUserById_shouldReturnCorrectUser() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", equalTo("Leanne Graham"))
            .body("email", notNullValue())
            .body("address", notNullValue())
            .body("company", notNullValue());
    }

    // Test 3: Verify user email format
    @Test
    public void getAllUsers_shouldHaveValidEmailFormat() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
                .body("email", everyItem(containsString("@")));
    }

    // Test 4: Get user with invalid ID
    @Test
    public void getUserById_withInvalidId_shouldReturn404() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users/9999")
        .then()
            .statusCode(404);
    }

    // Test 5: Verify user has nested address object
    @Test
    public void getUserById_shouldHaveAddressWithCity() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("address.city", notNullValue())
            .body("address.street", notNullValue())
            .body("address.zipcode", notNullValue());
    }

    // Test 6: Get posts for a specific user
    @Test
    public void getPostsByUser_shouldReturnUserPosts() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users/1/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].userId", equalTo(1));
    }
}