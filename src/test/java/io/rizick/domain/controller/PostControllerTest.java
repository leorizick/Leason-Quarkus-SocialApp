package io.rizick.domain.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.rizick.domain.model.Follower;
import io.rizick.domain.model.Post;
import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.PostRequest;
import io.rizick.domain.repository.FollowerRepository;
import io.rizick.domain.repository.PostRepository;
import io.rizick.domain.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
class PostControllerTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    public void setUp() {
        var user = new User();
        user.setName("UsuarioTeste");
        user.setAge(17);
        userRepository.persist(user);
        userId = user.getId();

        var userFollow = new User();
        user.setName("UsuarioFollowerTeste");
        user.setAge(17);
        userRepository.persist(userFollow);
        followerId = userFollow.getId();

        var follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollow);
        followerRepository.persist(follower);

        Post post = new Post();
        post.setText("Postagem teste");
        post.setUser(user);
        postRepository.persist(post);

    }

    @Test
    @DisplayName("Should create a post for an user")
    public void createPostTest() {
        var postRequest = new PostRequest();
        postRequest.setText("Post test");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when().post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when user of post not exist")
    public void createPostForInexistentUser() {
        var postRequest = new PostRequest();
        postRequest.setText("Post test");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", 0)
                .when().post()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Should return forbiden when dont follow the user")
    public void listPostForbidenTest() {
        given()
                .contentType(ContentType.JSON)
                .header("followerId", userId)
                .pathParam("userId", userId)
                .when().get()
                .then().statusCode(403).body(Matchers.is("Voce nao segue este usuario"));

    }

    @Test
    @DisplayName("Should list posts")
    public void listPostsTest() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when().get()
                .then().statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }
}