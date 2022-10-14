package in.reqres;

import in.reqres.models.lombock.UserBodyLombockModel;
import in.reqres.models.pojo.UserBodyPojoModel;
import in.reqres.models.pojo.UserBodyResponsePojoModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

        UserBodyResponsePojoModel response = given()
                .when()
                .contentType(JSON)
                .log().all()
                .body(user)
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(UserBodyResponsePojoModel.class);

        assertThat(response.getName(), equalTo("morpheus"));
        assertThat(response.getJob(), equalTo("leader"));
        assertThat(response.getCreatedAt(), notNullValue());
        assertThat(response.getId(), notNullValue());
    }

    @Test
    void changeUserNameTest() {
        UserBodyLombockModel user = new UserBodyLombockModel();
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
    void userLoginTest() {
        String eveHolt = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given()
                .when()
                .contentType(JSON)
                .log().all()
                .body(eveHolt)
                .post("https://reqres.in/api/login")
                .then()
                .log().body()
                .log().headers()
                .statusCode(200)
                .body("token", notNullValue());
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
