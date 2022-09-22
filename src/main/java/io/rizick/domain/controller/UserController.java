package io.rizick.domain.controller;

import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.ResponseError;
import io.rizick.domain.model.dto.UserRequest;
import io.rizick.domain.service.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private UserService userService;
    private Validator validator;

    @Inject
    public UserController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @GET
    public Response listALL() {
        return Response
                .ok(userService.listUsers().list())
                .build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id) {
        User user = userService.findById(id);
        return Response
                .ok(user)
                .build();
    }

    @POST
    @Transactional
    public Response createUser(UserRequest userRequest) {
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()) {
            ResponseError responseError = ResponseError.createFromValidation(violations);

            return Response
                    .status(422)
                    .entity(responseError)
                    .build();
        }
        User user = userService.createUser(userRequest);
        return Response
                .status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserRequest userRequest) {
        userService.updateUser(id, userRequest);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}