package in.reqres;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class UserTests {

    private final String morpheus = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

    @Test
    void getSingleUserTest() {
        var userId = 3;
        given()
                .when()
                .get("https://reqres.in/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("data.id", is(userId));
    }

    @Test
    void createUserTest() {
        given()
                .when()
                .contentType(JSON)
                .body(morpheus)
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void changeUserNameTest() {

        given()
                .when()
                .contentType(JSON)
                .body(morpheus)
                .patch("https://reqres.in/api/user/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("$", hasKey("updatedAt"));
    }

    @Test
    void userRegisterTest() {

        String eveHolt = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given()
                .when()
                .contentType(JSON)
                .body(eveHolt)
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"))
                .body("$", hasKey("id"));
    }

    @Test
    void deleteUserTest() {

        given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);

    }
}
