package io.rizick.domain.service;

import io.rizick.domain.model.Follower;
import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.FollowerPerUserResponse;
import io.rizick.domain.model.dto.FollowerRequest;
import io.rizick.domain.model.dto.FollowerResponse;
import io.rizick.domain.repository.FollowerRepository;
import io.rizick.domain.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.Collectors;

@ApplicationScoped
public class FollowerService {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    @Inject
    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    public FollowerPerUserResponse listFollowers(Long userId) {

        var user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }
        var list = followerRepository.findByUser(userId);
        FollowerPerUserResponse response = new FollowerPerUserResponse();
        response.setFollowersCount(list.size());

        var followersList = list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        response.setContent(followersList);

        return response;
    }

    public Follower followUser(Long userId, FollowerRequest followerRequest) {

        var user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }

        var follower = userRepository.findById(followerRequest.getFollowerId());

        boolean follows = followerRepository.follows(follower, user);

        if (!follows) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            followerRepository.persist(entity);

            return entity;
        }

        return null;
    }

    public User unfollowUser(Long userId){
        var user = userRepository.findById(userId);
        if(user == null){
            return null;
        }
        return user;
    }


    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        followerRepository.deleteByFollowerAndUser(followerId, userId);
    }
}
