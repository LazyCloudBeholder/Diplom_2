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

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUserTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmal.ru").setPassword("12345").setName("Олег");
        UserLogin userLogin = new UserLogin().setEmail("practest@gmal.ru").setPassword("12345");
        apiClient.createUser(userRegistration);
        Response response = apiClient.loginUser(userLogin);
        token = response.as(AuthResponse.class).getAccessToken();
        assertEquals(200, response.getStatusCode());
        assertTrue(response.as(AuthResponse.class).isSuccess());
    }

    @Test
    @DisplayName("Проверка авторизации с неверным логином и паролем")
    public void loginUserWithWrongLoginAndPasswordTest(){
        UserLogin userLogin = new UserLogin().setEmail("practest@gmail.ru").setPassword("12345");
        Response response = apiClient.loginUser(userLogin);
        assertEquals(401,response.getStatusCode(),"Неправильный статус код");
        response.then().assertThat().body("message",equalTo("email or password are incorrect"));
    }

    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }
}
