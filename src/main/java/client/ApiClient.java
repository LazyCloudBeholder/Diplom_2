package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Ingredients;
import model.User;
import model.UserLogin;
import model.UserRegistration;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public ApiClient() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Step("Отправляет запрос на создание пользователя")
    public Response createUser(UserRegistration userRegistration) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userRegistration)
                .when()
                .post("/api/auth/register");
    }
    @Step("Отправляет запрос на авторизацию пользователя")
    public Response loginUser(UserLogin login){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/auth/login");
    }

    @Step("Отправляет запрос на удаление пользователя")
    public void deleteUser(String token){
        given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");
    }

    @Step("Отправляет запрос на изменение данный пользователя с авторизацией")
    public Response changeUserDataWithAuth(String token, User user){
        return given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(user)
                .patch("/api/auth/user");
    }

    @Step("Отправляет запрос на изменение данный пользователя без авторизации")
    public Response changeDataWithoutAuth(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user");
    }
    @Step("Отправляет запрос на создание заказа без авторизации")
    public Response createOrderWithoutAuth(Ingredients ingredients){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }
    @Step("Отправляет запрос на создание заказа с авторизацией")
    public Response createOrderWithAuth(String token,Ingredients ingredients){
        return given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    @Step("Отправляет запрос на получение списка заказов")
    public Response getOrders(String token){
        return given()
                .header("Authorization", token)
                .when()
                .get("/api/orders");
    }

    @Step("Отправляет запрос на получение списка заказов без авторизации")
    public Response getOrdersWithoutAuth( ){
        return given()
                .when()
                .get("/api/orders");
    }

}