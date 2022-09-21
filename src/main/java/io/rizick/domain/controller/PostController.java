package io.rizick.domain.controller;

import io.rizick.domain.model.Post;
import io.rizick.domain.model.dto.PostRequest;
import io.rizick.domain.service.PostService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {

    private PostService postService;

    @Inject
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GET
    public Response listAll(@PathParam("userId") Long id) {
        return Response
                .ok(postService.listAll(id))
                .build();
    }

    @POST
    @Transactional
    public Response createPost(@PathParam("userId") Long id, PostRequest postRequest) {
        Post post = postService.createPost(id, postRequest);
        if (post == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        return Response
                .status(Response.Status.CREATED)
                .entity(post)
                .build();
    }

}
