import client.ApiClient;
import io.restassured.response.Response;
import model.AuthResponse;
import model.Ingredients;
import model.OrderList;
import model.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static Consts.IngredientsId.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetOrdersTest {
    private String token = "Not null";
    ApiClient apiClient = new ApiClient();

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getOrdersTest(){
        UserRegistration userRegistration = new UserRegistration().setEmail("practest@gmail.ru").setPassword("12345").setName("Олег");
        Response registerResponse = apiClient.createUser(userRegistration);
        token = registerResponse.as(AuthResponse.class).getAccessToken();
        Ingredients ingredients = new Ingredients().setIngredients(new String[]{CRATER_BUN ,SAUCE_SPICY_X,PROTOSTOMIA_MEAT,MINERAL_RINGS,FLUORESCENT_BUN});
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
