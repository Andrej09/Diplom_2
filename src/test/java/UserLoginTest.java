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
        var user = new UserGenerator().random();
    }

    @After
    public void userDelete() {
        if (accessToken != null) {
            client.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Username of the user under the existing user")
    public void loginUnderExistingUser() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).statusCode(200).extract().path("accessToken");
    }

    @Test
    @DisplayName("Username of a user with an invalid password")
    public void loginWithInvalidPassword () {
        var user = new UserGenerator().random();
        accessToken = client.register(user).extract().path("accessToken");
        user.setPassword(null);
        client.login(user).statusCode(401).assertThat()
                .body("message", equalTo("email or password are incorrect"));
        client.updatePassword(accessToken, user);
        accessToken = client.login(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Username of a user with an invalid username")
    public void loginWithInvalidEmail() {
        var user = new UserGenerator().random();
        accessToken = client.register(user).extract().path("accessToken");
        user.setEmail(null);
        client.login(user).statusCode(401).assertThat()
                .body("message", equalTo("email or password are incorrect"));
        client.updateEmail(accessToken, user);
        accessToken = client.login(user).extract().path("accessToken");
    }

}
