package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.StellarBurgerRestClient;
import ru.yandex.praktikum.model.User;

import java.util.Base64;

import static io.restassured.RestAssured.given;
public class UserClient extends StellarBurgerRestClient {

    private static final String USER_CREATE_URI = BASE_URI + "auth/register";
    private static final String USER_BASE_URI = BASE_URI + "auth/user";


    @Step("Create user {user}")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(USER_CREATE_URI)
                .then();
    }

    @Step("Delete user {user}")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_BASE_URI)
                .then();
    }

    @Step("Update email of user {user}")
    public ValidatableResponse updateEmail(String accessToken, String newEmail) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .body("{\n" +
                        "  \"email\": \"" + newEmail +"\" \n}")
                .when()
                .patch(USER_BASE_URI)
                .then();
    }

    @Step("Update email of user {user}")
    public ValidatableResponse updateEmailUnauthorized(String newEmail) {
        return given()
                .spec(getBaseReqSpec())
                .body("{\n" +
                        "  \"email\": \"" + newEmail +"\" \n}")
                .when()
                .patch(USER_BASE_URI)
                .then();
    }

    @Step("Update email of user {user}")
    public ValidatableResponse updateName(String accessToken, String newName) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .body("{\n" +
                        "  \"name\": \"" + newName +"\" \n}")
                .when()
                .patch(USER_BASE_URI)
                .then();
    }

    @Step("Update email of user {user}")
    public ValidatableResponse updateNameUnauthorized(String newName) {
        return given()
                .spec(getBaseReqSpec())
                .body("{\n" +
                        "  \"name\": \"" + newName +"\" \n}")
                .when()
                .patch(USER_BASE_URI)
                .then();
    }
}
