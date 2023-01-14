import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangingUserDataTest {

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
    @DisplayName("Changing the email of an authorized user")
    public void changingUserDataWithAuthorizationEmail() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).extract().path("accessToken");
        client.updateEmail(accessToken, user).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Changing the password of an authorized user")
    public void changingUserDataWithAuthorizationPassword() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).extract().path("accessToken");
        client.updatePassword(accessToken, user).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Changing the name of an authorized user")
    public void changingUserDataWithAuthorizationName() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).extract().path("accessToken");
        client.updateName(accessToken, user).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Changing the email of an unauthorized user")
    public void changingUserDataWithNotAuthorizationEmail() {
        var user = new UserGenerator().random();
        client.register(user).extract().path("accessToken");;
        client.updateEmail("", user).statusCode(401).assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Changing the password of an unauthorized user")
    public void changingUserDataWithNotAuthorizationPassword() {
        var user = new UserGenerator().random();
        client.register(user).extract().path("accessToken");;
        client.updatePassword("", user).statusCode(401).assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Changing the name of an unauthorized user ")
    public void changingUserDataWithNotAuthorizationName() {
        var user = new UserGenerator().random();
        client.register(user).extract().path("accessToken");;
        client.updateName("", user).statusCode(401).assertThat()
                .body("message", equalTo("You should be authorised"));
    }


}
