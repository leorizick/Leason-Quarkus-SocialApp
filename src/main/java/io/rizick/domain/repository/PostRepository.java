package io.rizick.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.rizick.domain.model.Post;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
