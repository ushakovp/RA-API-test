package in.reqres;

import in.reqres.models.pojo.UserBodyPojoModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserTests {


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
        UserBodyPojoModel user = new UserBodyPojoModel();
        user.setName("morpheus");
        user.setJob("leader");

        given()
                .when()
                .contentType(JSON)
                .log().all()
                .body(user)
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .body("job", is("leader"));
    }

    @Test
    void changeUserNameTest() {
        UserBodyPojoModel user = new UserBodyPojoModel();
        user.setName("morpheus");
        user.setJob("leader");

        given()
                .when()
                .log().all()
                .contentType(JSON)
                .body(user)
                .patch("https://reqres.in/api/user/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("updatedAt", notNullValue());
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
                .body("token", notNullValue())
                .body("id", notNullValue());
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
