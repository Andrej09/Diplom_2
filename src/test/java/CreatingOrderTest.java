import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class CreatingOrderTest {

    private final UserClient client = new UserClient();

    private String accessToken;

    private  List<String> ingredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @After
    public void userDelete() {
        if (accessToken != null) {
            client.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Creating an order with authorization and ingredients")
    public void orderCreationWithAuthorizationWithIngredients() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).extract().path("accessToken");
        ingredients = List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f");
        client.creatingOrder(ingredients).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Creating an order without authorization")
    public void orderCreationWithNotAuthorization() {
        var user = new UserGenerator().random();
        accessToken = client.register(user).extract().path("accessToken");
        ingredients = List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f");
        client.creatingOrder(ingredients).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Creating an order without ingredients")
    public void creatingOrderWithoutIngredients() {
        var user = new UserGenerator().random();
        accessToken = client.register(user).extract().path("accessToken");
        ingredients = List.of();
        client.creatingOrder(ingredients).statusCode(400)
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating an order with an incorrect hash of ingredients")
    public void creatingOrderWithIncorrectIngredients() {
        var user = new UserGenerator().random();
        accessToken = client.register(user).extract().path("accessToken");
        ingredients = List.of("61c0c5a71d1f82001bdaaa6","61c0c5a71d1f82001bdaaa6");
        client.creatingOrder(ingredients).statusCode(500);
    }

}
