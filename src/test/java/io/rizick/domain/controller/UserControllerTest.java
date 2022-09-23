package io.rizick.domain.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.ResponseError;
import io.rizick.domain.model.dto.UserRequest;
import io.rizick.domain.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(UserController.class)
class UserControllerTest {
    @TestHTTPResource("/users")
    private URL apiURL;

    @Inject
    private UserRepository userRepository;

    private Long userId;

    @BeforeEach
    @Transactional
    public void setUp() {
        var user = new User();
        user.setName("UsuarioTeste");
        user.setAge(17);
        userRepository.persist(user);
        userId = user.getId();
    }


    @Test
    @DisplayName("Should create a user")
    @Order(1)
    public void createUserTest() {
        var user = new UserRequest();
        user.setName("UsuarioTeste");
        user.setAge(17);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                        .when().post()
                        .then().extract().response();
        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));

    }

    @Test
    @DisplayName("Should return error when request is invalid")
    @Order(2)
    public void createUserValidationErrorTest() {
        var user = new UserRequest();
        user.setName(null);
        user.setAge(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post()
                .then().extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
//        assertEquals("Idade nao pode ser nula", errors.get(0).get("message"));
//        assertEquals("Nome nao pode estar em branco", errors.get(1).get("message"));
    }

    @Test
    @DisplayName("Should list all users")
    @Order(3)
    public void listAllUsersTest() {

        given()
                .contentType(ContentType.JSON)
                .when().get(apiURL)
                .then().statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

    @Test
    @DisplayName("Should delete a User")
    @Order(4)
    public void deleteUserTest() {
        var response =
                given()
                        .contentType(ContentType.JSON)
                        .delete( "/" + userId)
                        .then().extract().response();
        assertEquals(204, response.getStatusCode());

    }


}