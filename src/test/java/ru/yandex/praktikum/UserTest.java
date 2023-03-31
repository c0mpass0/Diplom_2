package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserGenerator;

import java.util.Base64;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserTest {
    private UserClient userClient;
    private String accessToken;
    private String wrongAccessToken = Base64.getEncoder().encodeToString(("Wrong Token").getBytes());

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void clearData() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    public void userCanBeCreatedWithValidData() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    public void userCantBeCreatedWithSameCredentials() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .assertThat()
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void userCantBeCreatedWithoutEmail() {
        User user = UserGenerator.getRandom();
        user.setEmail("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .assertThat()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void userCantBeCreatedWithoutPassword() {
        User user = UserGenerator.getRandom();
        user.setPassword("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .assertThat()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void userCantBeCreatedWithoutName() {
        User user = UserGenerator.getRandom();
        user.setName("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .assertThat()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Изменение почты пользователя")
    public void userEmailChangedSuccessfully() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        String newEmail = user.getEmail() + "n";

        userClient.updateEmail(accessToken, newEmail)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .body("user.email", is(newEmail.toLowerCase()));
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    public void userNameChangedSuccessfully() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        String newName = user.getName() + "New";

        userClient.updateName(accessToken, newName)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .body("user.name", is(newName));
    }

    @Test
    @DisplayName("Изменение почты пользователя без авторизации")
    public void userEmailChangeWithoutAuthorizationFailed() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        String newEmail = user.getEmail() + "n";

        userClient.updateEmailUnauthorized(newEmail)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение почты пользователя без авторизации")
    public void userNameChangeWithoutAuthorizationFailed() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        String newName = user.getName() + "New";

        userClient.updateNameUnauthorized(newName)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
