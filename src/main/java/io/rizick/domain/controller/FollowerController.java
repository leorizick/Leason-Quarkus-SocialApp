package io.rizick.domain.controller;

import io.rizick.domain.model.dto.FollowerPerUserResponse;
import io.rizick.domain.model.dto.FollowerRequest;
import io.rizick.domain.model.dto.FollowerResponse;
import io.rizick.domain.service.FollowerService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerController {

    private FollowerService followerService;

    @Inject
    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {
        FollowerPerUserResponse list = followerService.listFollowers(userId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(list).build();
    }


    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {
        if (userId.equals(followerRequest.getFollowerId())) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("Voce não pode seguir a si mesmo")
                    .build();
        }
        var follower = followerService.followUser(userId, followerRequest);
        if (follower == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        return Response
                .noContent()
                .build();

    }


}
