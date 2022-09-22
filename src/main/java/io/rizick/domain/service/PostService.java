package io.rizick.domain.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.rizick.domain.model.Post;
import io.rizick.domain.model.User;
import io.rizick.domain.model.dto.PostRequest;
import io.rizick.domain.model.dto.PostResponse;
import io.rizick.domain.repository.FollowerRepository;
import io.rizick.domain.repository.PostRepository;
import io.rizick.domain.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostService(UserRepository userRepository,
                       PostRepository postRepository,
                       FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    public Post createPost(Long id, PostRequest postRequest) {
        User user = userRepository.findById(id);
        if(user == null){
            return null;
        }
        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return post;
    }

    public List<PostResponse> listAll(Long id, Long followerId){
        User user = userRepository.findById(id);
        if(user == null){
            return null;
        }

        User follower = userRepository.findById(followerId);
        boolean follows = followerRepository.follows(follower, user);
        if(!follows){
            return null;
        }

        var query = postRepository.find("user", Sort.by("date", Sort.Direction.Descending), user);
        var list = query.list();

        var postResponseList = list.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return postResponseList;
    }


}
