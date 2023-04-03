package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.StellarBurgerRestClient;
import ru.yandex.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends StellarBurgerRestClient {

    private static final String ORDER_BASE_URL = BASE_URI + "orders";

    @Step("Create order")
    public ValidatableResponse createOrder(String accessToken, Order order){
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_BASE_URL)
                .then();
    }

    @Step("Create order")
    public ValidatableResponse createOrderUnauthorized(Order order){
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(ORDER_BASE_URL)
                .then();
    }

    @Step("Get orders of user")
    public ValidatableResponse getOrdersOfUser(String accessToken){
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_BASE_URL)
                .then();
    }

    @Step("Get orders of user")
    public ValidatableResponse getUnauthorisedOrdersOfUser(){
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_BASE_URL)
                .then();
    }
}
