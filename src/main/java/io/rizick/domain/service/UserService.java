package io.rizick.domain.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.UserRequest;
import io.rizick.domain.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UserService {

    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PanacheQuery<User> listUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        User user = userRepository.findById(id);
        return user;
    }

    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        userRepository.persist(user);

        return user;
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id);
        userRepository.delete(user);
    }

    public User updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id);

        if (user != null) {
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            return user;
        }
        return null;
    }
}
