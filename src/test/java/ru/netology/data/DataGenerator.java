package ru.netology.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@Data
@Getter

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static class User {
        public static final String userName = faker.name().username();
        public static final String password = faker.internet().password();
    }

    private static void ActiveRegistration() {
        // TODO: отправить запрос на указанный в требованиях path, передав в body запроса объект user
        //  и не забудьте передать подготовленную спецификацию requestSpec.
        //  Пример реализации метода показан в условии к задаче.
        given()
                .spec(requestSpec)
                .body(new Gson().toJson(new RegistrationDto(User.userName, User.password, "active")))
                .when() // "когда"
                .post("/api/system/users")
                .then()
                .statusCode(200);

    }

    private static void NonActiveRegistration() {
        given()
                .spec(requestSpec)
                .body(new Gson().toJson(new RegistrationDto(User.userName, User.password, "blocked")))
                .when() // "когда"
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            // TODO: создать пользователя user используя методы getRandomLogin(), getRandomPassword() и параметр status
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), "active");
        }

       /* public static RegistrationDto getRegisteredUser(String status) {
            // TODO: объявить переменную registeredUser и присвоить ей значение возвращённое getUser(status).
            // Послать запрос на регистрацию пользователя с помощью вызова sendRequest(registeredUser)

            return registeredUser;
        }*/
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
