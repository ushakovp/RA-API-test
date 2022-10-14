package in.reqres;

import in.reqres.models.lombock.UserBodyLombockModel;
import in.reqres.models.lombock.UserBodyResponseLombokModel;
import in.reqres.models.pojo.UserBodyPojoModel;
import in.reqres.models.pojo.UserBodyResponsePojoModel;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.UserSpec.requestSpec;
import static in.reqres.specs.UserSpec.responseSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserTests {


    @Test
    void getSingleUserTest() {
        var userId = 3;
        given()
                .spec(requestSpec)
                .get("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("data.id", is(userId));
    }

    @Test
    void createUserTest() {
        var user = new UserBodyPojoModel();
        user.setName("morpheus");
        user.setJob("leader");

        UserBodyResponsePojoModel response = given()
                .spec(requestSpec)
                .body(user)
                .post("/api/users")
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
        UserBodyPojoModel user = new UserBodyPojoModel();
        user.setName("morpheus");
        user.setJob("leader");

        given()
                .spec(requestSpec)
                .body(user)
                .patch("/api/user/2")
                .then()
                .spec(responseSpec)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void userRegisterTest() {
        UserBodyLombockModel user = new UserBodyLombockModel();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        UserBodyResponseLombokModel response = given()
                .spec(requestSpec)
                .body(user)
                .post("/api/register")
                .then()
                .spec(responseSpec)
                .extract()
                .as(UserBodyResponseLombokModel.class);

        assertThat(response.getId(), notNullValue());
        assertThat(response.getToken(), notNullValue());
    }

    @Test
    void userLoginTest() {
        UserBodyLombockModel user = new UserBodyLombockModel();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        given()
                .spec(requestSpec)
                .body(user)
                .post("/api/login")
                .then()
                .spec(responseSpec)
                .body("token", notNullValue());
    }

    @Test
    void deleteUserTest() {

        given()
                .spec(requestSpec)
                .delete("/api/users/2")
                .then()
                .statusCode(204);

    }
}
