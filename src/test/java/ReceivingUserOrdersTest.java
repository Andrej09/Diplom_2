import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ReceivingUserOrdersTest {

    private final UserClient client = new UserClient();

    private String accessToken;

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
    @DisplayName("Receiving orders from an authorized user")
    public void receivingOrdersFromSpecificAuthorizedUser() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).extract().path("accessToken");
        client.getOrder(accessToken).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Receiving orders from an unauthorized user")
    public void receivingOrdersFromSpecificNotAuthorizedUser() {
        var user = new UserGenerator().random();
        accessToken = client.register(user).extract().path("accessToken");
        client.getOrder(accessToken).statusCode(200).body("success", equalTo(true));
    }
}
