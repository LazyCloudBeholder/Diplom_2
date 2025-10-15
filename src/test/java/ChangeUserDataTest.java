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
    private final String email = "practest@gmail.ru";
    private final String password= "123456";
    private final String name = "Олег";

    @Test
    @DisplayName("Проверка возможности изменения данных пользователя")
    public void changeDataTest(){
        String newName = "Влад";
        String newEmail = "stariybog@gmail.ru";
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        User newUserData = new User().setEmail(newEmail).setName(newName);
        Response registrationResponse = apiClient.createUser(userRegistration);
        token = registrationResponse.as(AuthResponse.class).getAccessToken();
        Response changeResponse = apiClient.changeUserDataWithAuth(token, newUserData);
        assertEquals(200, changeResponse.getStatusCode(),"Неправильный статус код");
        assertTrue(changeResponse.asString().contains(newName),"Данные не изменились");
        assertTrue(changeResponse.asString().contains(newEmail),"Данные не изменились");
    }

    @Test
    @DisplayName("Проверка возможности изменения данных пользователя без авторизации")
    public void changeDataWithoutAuthTest(){
        User newUserData = new User().setEmail(email).setName(name);
        Response response = apiClient.changeDataWithoutAuth(newUserData);
        assertEquals(401, response.getStatusCode(),"Неправильный статус код");
        response.then().assertThat().body("message",equalTo("You should be authorised"));
    }

    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }
}
