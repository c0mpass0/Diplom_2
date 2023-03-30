package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.StellarBurgerRestClient;
import ru.yandex.praktikum.model.Login;

import ru.yandex.praktikum.client.base.StellarBurgerRestClient;

import static io.restassured.RestAssured.given;

public class LoginClient extends StellarBurgerRestClient {

    private static final String USER_LOGIN_URI = BASE_URI + "auth/login";

    @Step("Login in user {user}")
    public ValidatableResponse login(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .when()
                .post(USER_LOGIN_URI)
                .then();
    }
}
