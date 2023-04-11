package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.StellarBurgerRestClient;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserUpdateEmail;
import ru.yandex.praktikum.model.UserUpdateName;

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

    @Step("Update email of authorized user")
    public ValidatableResponse updateEmailAuthorized(String accessToken, UserUpdateEmail userUpdateEmail) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .body(userUpdateEmail)
                .when()
                .patch(USER_BASE_URI)
                .then();
    }
    @Step("Update name of authorized user")
    public ValidatableResponse updateNameAuthorized(String accessToken, UserUpdateName userUpdateName) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", accessToken)
                .body(userUpdateName)
                .when()
                .patch(USER_BASE_URI)
                .then();
    }

    @Step("Update email of unauthorized user")
    public ValidatableResponse updateEmailUnauthorized(UserUpdateEmail userUpdateEmail) {
        return given()
                .spec(getBaseReqSpec())
                .body(userUpdateEmail)
                .when()
                .patch(USER_BASE_URI)
                .then();
    }
    @Step("Update name of unauthorized user")
    public ValidatableResponse updateNameUnauthorized(UserUpdateName userUpdateEmail) {
        return given()
                .spec(getBaseReqSpec())
                .body(userUpdateEmail)
                .when()
                .patch(USER_BASE_URI)
                .then();
    }
}
