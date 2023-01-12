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
    @DisplayName("Changing the data of an authorized user")
    public void changingUserDataWithAuthorization() {
        var user = new UserGenerator().random();
        client.register(user);
        accessToken = client.login(user).extract().path("accessToken");
        client.updateEmail(accessToken, user).statusCode(200).body("success", equalTo(true));
        client.updatePassword(accessToken, user).statusCode(200).body("success", equalTo(true));
        client.updateName(accessToken, user).statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Changing the data of an unauthorized user")
    public void changingUserDataWithNotAuthorization() {
        var user = new UserGenerator().random();
        client.register(user).extract().path("accessToken");;
        client.updateEmail("", user).statusCode(401).assertThat()
                .body("message", equalTo("You should be authorised"));
        client.updatePassword("", user).statusCode(401).assertThat()
                .body("message", equalTo("You should be authorised"));
        client.updateName("", user).statusCode(401).assertThat()
                .body("message", equalTo("You should be authorised"));
    }
}
