package io.rizick.domain.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.rizick.domain.model.Follower;
import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.FollowerRequest;
import io.rizick.domain.repository.FollowerRepository;
import io.rizick.domain.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FollowerControllerTest {

    @Inject
    private UserRepository userRepository;
    @Inject
    private FollowerRepository followerRepository;

    private Long userId;
    private Long followerId;
    private Long followedUserId;

    @BeforeEach
    @Transactional
    void setUp() {
        var user = new User();
        user.setName("UsuarioTest");
        user.setAge(17);
        userRepository.persist(user);
        userId = user.getId();

        var follower = new User();
        user.setName("FollowerTest");
        user.setAge(17);
        userRepository.persist(follower);
        followerId = follower.getId();

        var userFollowed = new User();
        user.setName("FollowedUser");
        user.setAge(17);
        userRepository.persist(userFollowed);
        followedUserId = userFollowed.getId();

        var followed = new Follower();
        followed.setFollower(follower);
        followed.setUser(userFollowed);
        followerRepository.persist(followed);

    }

    @Test
    @DisplayName("Should return a error 409 when user follow itself")
    @Order(1)
    public void errorSameUserAsFollowerTest() {

        FollowerRequest followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .body(followerRequest)
                .when().put()
                .then().statusCode(409);
    }

    @Test
    @DisplayName("Should return a error when user not exist")
    @Order(2)
    public void errorFollowAUserNotFoundTest() {

        FollowerRequest followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", 999)
                .body(followerRequest)
                .when().put()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Should follow a user")
    @Order(3)
    public void followUserTest() {

        FollowerRequest followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", userId)
                .when().put()
                .then().statusCode(204);
    }

    @Test
    @DisplayName("Should return error when NOT FOUND a user")
    @Order(4)
    public void listFollowerUserNotFoundTest() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", 999)
                .when().get()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Should list a user followers")
    @Order(5)
    public void listFollowerTest() {
        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", followedUserId)
                .when().get()
                .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(1, followersCount);

    }

    @Test
    @DisplayName("Should delete a follower")
    @Order(6)
    public void deleteFollowUserTest() {

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("followerId", followerId)
                        .pathParam("userId", userId)
                        .delete( )
                        .then().extract().response();
        assertEquals(204, response.getStatusCode());

    }


}