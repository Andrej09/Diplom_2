package org.example;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.List;
import static io.restassured.RestAssured.given;

public class UserClient {

    public ValidatableResponse register(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register")
                .then().log().all();
    }

    public ValidatableResponse login(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }

    public ValidatableResponse delete(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .delete("/api/auth/user")
                .then()
                .log().all().statusCode(202);
    }

    public ValidatableResponse updatePassword(String accessToken, User user) {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }

    public ValidatableResponse updateEmail(String accessToken, User user) {
        user.setEmail(RandomStringUtils.randomAlphabetic(10));
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }

    public ValidatableResponse updateName(String accessToken, User user) {
        user.setName(RandomStringUtils.randomAlphabetic(10));
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }

    public ValidatableResponse getOrder(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .get("/api/orders")
                .then().log().all();
    }

    public ValidatableResponse creatingOrder(List<String> ingredients){
        var jason = new Ingredients(ingredients);
        return given()
                .contentType(ContentType.JSON)
                .body(jason)
                .when()
                .post("/api/orders")
                .then().log().all();
    }
}