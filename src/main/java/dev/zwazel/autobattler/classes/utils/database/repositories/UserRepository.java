package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.utils.database.UserOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where lower(u.username) = lower(?1)")
    Optional<User> findByUsername(String username);

    @Query("select (count(u) > 0) from User u where lower(u.username) = lower(?1)")
    boolean existsByUsername(String username);

    @Query("select u from User u")
    List<UserOnly> findAllUserOnly();
}
