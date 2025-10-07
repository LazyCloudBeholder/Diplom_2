import client.ApiClient;
import io.restassured.response.Response;
import model.AuthResponse;
import model.Ingredients;
import model.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static Consts.IngredientsId.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateOrderTest {
    private String token = "Not null";
    ApiClient apiClient = new ApiClient();

    @Test
    @DisplayName("Проверка возможности создать заказ")
    public void createOrderTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Ingredients ingredients = new Ingredients().setIngredients(new String[]{FLUORESCENT_BUN ,SAUCE_SPICY_X,BEEF_METEORITE,MINERAL_RINGS,FLUORESCENT_BUN });
        Response orderResponse = apiClient.createOrderWithAuth(token, ingredients);
        assertEquals(200, orderResponse.getStatusCode(),"Неправильный статус код");
        orderResponse.then().assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Проверка возможности создать заказ без авторизации")
    public void createOrderWithOutAuthTest(){
        Ingredients ingredients = new Ingredients().setIngredients(new String[]{FLUORESCENT_BUN ,SAUCE_SPICY_X,BEEF_METEORITE,MINERAL_RINGS,FLUORESCENT_BUN });
        Response orderResponse = apiClient.createOrderWithoutAuth(ingredients);
        assertEquals(401, orderResponse.getStatusCode(),"Неправильный статус код");
        orderResponse.then().assertThat().body("message", equalTo("You should be authorised"));

    }

    @Test
    @DisplayName("Проверка возможности создать заказ с неправильным кодом ингредиентов")
    public void createOrderWithWrongIngredientsHashTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Ingredients ingredients = new Ingredients().setIngredients(new String[]{"gafsadaqwodsajkld1241123","sdsahwokadi123","dshjdasjqwoid"});
        Response orderResponse = apiClient.createOrderWithAuth(token, ingredients);
        assertEquals(500, orderResponse.getStatusCode(),"Неправильный статус код");

    }

    @Test
    @DisplayName("Проверка возможности создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Ingredients ingredients = new Ingredients().setIngredients(new String[]{});
        Response orderResponse = apiClient.createOrderWithAuth(token, ingredients);
        assertEquals(400, orderResponse.getStatusCode(),"Неправильный статус код");
        orderResponse.then().assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }


    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }
}
