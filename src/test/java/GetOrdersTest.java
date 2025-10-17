import client.ApiClient;
import io.restassured.response.Response;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetOrdersTest {
    private String token = "Not null";
    ApiClient apiClient = new ApiClient();
    private final String email = "practest@gmail.ru";
    private final String password= "123456";
    private final String name = "Олег";

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getOrdersTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail(email).setPassword(password).setName(name);
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Response getIngredients = apiClient.getIngredients();
        Ingredients ingredients = new Ingredients();
        ingredients.setAllIngredients(getIngredients.as(IngredientsGetResponse.class));
        apiClient.createOrderWithAuth(token, ingredients);
        Response gerOrdersResponse = apiClient.getOrders(token);
        OrderList orders = gerOrdersResponse.as(OrderList.class);
        assertEquals(200, gerOrdersResponse.getStatusCode(),"Неправильный статус код");
        assertTrue(orders.ordersNotNull(),"Заказы не должны быть null");



    }

    @Test
    @DisplayName("Проверка получения списка заказов без авторизации")
    public void getOrdersWithoutAuth(){
        Response getOrdersResponse = apiClient.getOrdersWithoutAuth();
        assertEquals(401, getOrdersResponse.getStatusCode(),"Неправильный статус код");
        getOrdersResponse.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @AfterEach
    public void tearDown(){
        apiClient.deleteUser(token);
    }

}
