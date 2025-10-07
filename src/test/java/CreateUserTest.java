import client.ApiClient;
import io.restassured.response.Response;
import model.AuthResponse;
import model.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserTest {
    ApiClient apiClient = new ApiClient();
    private String token = "Not null";

    @Test
    @DisplayName("Проверка возможности создать пользователя")
    public void createUserTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        Response response = apiClient.createUser(userRegistration);
        token = response.as(AuthResponse.class).getAccessToken();
        assertEquals(200, response.getStatusCode(),"Неправильный статус код");
        assertTrue(response.as(AuthResponse.class).isSuccess());
    }

    @Test
    @DisplayName("Проверка создания уже существующего пользователя")
    public void createExistingUserTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        Response response = apiClient.createUser(userRegistration);
        token = response.as(AuthResponse.class).getAccessToken();
        Response response2 = apiClient.createUser(userRegistration);
        assertEquals(403, response2.getStatusCode(),"Неправильный статус код");
        response2.then().assertThat().body("message",equalTo("User already exists"));
    }

    @ParameterizedTest
    @CsvSource({
            " ,password, ",
            "practest@gmail.ru, ,",
            ", , pain",
            " , , "
    })
    @DisplayName("Проверка создания пользователя без обязательный полей")
    public void createUserWithoutFieldsTest(String email, String password, String name){
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        Response response = apiClient.createUser(userRegistration);
        assertEquals(403, response.getStatusCode(),"Неправильный статус код");
        response.then().assertThat().body("message",equalTo("Email, password and name are required fields"));
    }

    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }

}
