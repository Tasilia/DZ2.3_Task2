package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    void registration(String login, String password) {
        $("[data-test-id=\"login\"] input").setValue(login);
        $("[data-test-id=\"password\"] input").setValue(password);
        $("[data-test-id=\"action-login\"]").click();
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        registration(registeredUser.getLogin(), registeredUser.getPassword());
        $x("//h2[contains(text(), \"Личный кабинет\")]").shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        registration(notRegisteredUser.getLogin(), notRegisteredUser.getPassword());
        $(".notification_status_error .notification__content").shouldHave(text("Неверно указан логин " +
                "или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        registration(blockedUser.getLogin(), blockedUser.getPassword());
        $(".notification_status_error .notification__content").shouldHave(text("Пользователь заблокирован"))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        registration(wrongLogin, registeredUser.getPassword());
        $(".notification_status_error .notification__content").shouldHave(text("Неверно указан логин " +
                "или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        registration(registeredUser.getLogin(), wrongPassword);
        $(".notification_status_error .notification__content").shouldHave(text("Неверно указан логин " +
                "или пароль")).shouldBe(visible);
    }
}
