import client.ApiClient;
import io.restassured.response.Response;
import model.AuthResponse;
import model.User;
import model.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeUserDataTest {
    private String token ="Not null";
    ApiClient apiClient = new ApiClient();

    @Test
    @DisplayName("Проверка возможности изменения данных пользователя")
    public void changeDataTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        User newUserData = new User().setEmail("stariybog@gmail.ru").setName("Влад");
        Response registrationResponse = apiClient.createUser(userRegistration);
        token = registrationResponse.as(AuthResponse.class).getAccessToken();
        Response changeResponse = apiClient.changeUserDataWithAuth(token, newUserData);
        assertEquals(200, changeResponse.getStatusCode(),"Неправильный статус код");
        assertTrue(changeResponse.asString().contains("Влад"),"Данные не изменились");
        assertTrue(changeResponse.asString().contains("stariybog@gmail.ru"),"Данные не изменились");
    }

    @Test
    @DisplayName("Проверка возможности изменения данных пользователя без авторизации")
    public void changeDataWithoutAuthTest(){
        User newUserData = new User().setEmail("stariybog@gmail.ru").setName("Влад");
        Response response = apiClient.changeDataWithoutAuth(newUserData);
        assertEquals(401, response.getStatusCode(),"Неправильный статус код");
        response.then().assertThat().body("message",equalTo("You should be authorised"));
    }

    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }
}
