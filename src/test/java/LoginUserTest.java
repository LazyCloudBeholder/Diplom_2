import client.ApiClient;
import io.restassured.response.Response;
import model.AuthResponse;
import model.UserLogin;
import model.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUserTest {
    private String token = "Not null";
    ApiClient apiClient = new ApiClient();
    private final String email = "practest@gmail.ru";
    private final String password= "123456";
    private final String name = "Олег";

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUserTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        UserLogin userLogin = new UserLogin().setEmail(email).setPassword(password);
        apiClient.createUser(userRegistration);
        Response response = apiClient.loginUser(userLogin);
        token = response.as(AuthResponse.class).getAccessToken();
        assertEquals(200, response.getStatusCode());
        assertTrue(response.as(AuthResponse.class).isSuccess());
    }

    @Test
    @DisplayName("Проверка авторизации с неверным логином и паролем")
    public void loginUserWithWrongLoginAndPasswordTest(){
        String wrongEmail = "stariybog322@gmail.com";
        String wrongPassword = "123123818";
        UserLogin userLogin = new UserLogin().setEmail(wrongEmail).setPassword(wrongPassword);
        Response response = apiClient.loginUser(userLogin);
        assertEquals(401,response.getStatusCode(),"Неправильный статус код");
        response.then().assertThat().body("message",equalTo("email or password are incorrect"));
    }

    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }
}
