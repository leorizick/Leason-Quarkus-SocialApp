package io.rizick.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.rizick.domain.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
