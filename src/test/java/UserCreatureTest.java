import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    private final UserClient client = new UserClient();

    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @After
    public void userrDelete() {
        if (accessToken != null) {
            client.delete(accessToken).statusCode(202);
        }
    }

    @Test
    public void authorizationVerification() {
    var user = new UserGenerator().random();
    client.register(user).statusCode(200);
    accessToken = client.login(user).statusCode(200).extract().path("accessToken");
    }

    @Test
    public void  userAlreadyRegistered() {
        var user = new UserGenerator().random();
        client.register(user).statusCode(200);
        client.register(user).statusCode(403).assertThat()
                .body("message",equalTo("User already exists"));
        accessToken = client.login(user).statusCode(200).extract().path("accessToken");
    }

    @Test
    public void  oneRequiredFieldsNotFilled() {
        var user = new UserGenerator().random();
        user.setPassword(null);
        client.register(user).statusCode(403).assertThat()
                .body("message",equalTo("Email, password and name are required fields"));
    }

}
