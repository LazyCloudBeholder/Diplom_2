import client.ApiClient;
import io.restassured.response.Response;
import model.AuthResponse;
import model.Ingredients;
import model.IngredientsGetResponse;
import model.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateOrderTest {
    private String token = "Not null";
    ApiClient apiClient = new ApiClient();
    private final String email = "practest@gmail.ru";
    private final String password= "123456";
    private final String name = "Олег";

    @Test
    @DisplayName("Проверка возможности создать заказ")
    public void createOrderTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Response getIngredients = apiClient.getIngredients();
        Ingredients ingredients = new Ingredients();
        ingredients.setAllIngredients(getIngredients.as(IngredientsGetResponse.class));
        Response orderResponse = apiClient.createOrderWithAuth(token, ingredients);
        assertEquals(200, orderResponse.getStatusCode(),"Неправильный статус код");
        orderResponse.then().assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Проверка возможности создать заказ без авторизации")
    public void createOrderWithOutAuthTest(){
        Response getIngredients = apiClient.getIngredients();
        Ingredients ingredients = new Ingredients();
        ingredients.setAllIngredients(getIngredients.as(IngredientsGetResponse.class));
        Response orderResponse = apiClient.createOrderWithoutAuth(ingredients);
        assertEquals(401, orderResponse.getStatusCode(),"Неправильный статус код");
        orderResponse.then().assertThat().body("message", equalTo("You should be authorised"));

    }

    @Test
    @DisplayName("Проверка возможности создать заказ с неправильным кодом ингредиентов")
    public void createOrderWithWrongIngredientsHashTest(){
        String[] wrongIds = new String[]{"dsadqwdodfso","dwqk12d21d","sdadk123ksak"};
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Ingredients ingredients = new Ingredients().setIngredients(wrongIds);
        Response orderResponse = apiClient.createOrderWithAuth(token, ingredients);
        assertEquals(500, orderResponse.getStatusCode(),"Неправильный статус код");

    }

    @Test
    @DisplayName("Проверка возможности создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest(){
        String[] emptyIds = new String[]{};
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Ingredients ingredients = new Ingredients().setIngredients(emptyIds);
        Response orderResponse = apiClient.createOrderWithAuth(token, ingredients);
        assertEquals(400, orderResponse.getStatusCode(),"Неправильный статус код");
        orderResponse.then().assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }


    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }
}
