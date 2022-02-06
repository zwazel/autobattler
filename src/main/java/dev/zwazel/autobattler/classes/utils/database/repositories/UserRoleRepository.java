package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.utils.EnumUserRole;
import dev.zwazel.autobattler.classes.utils.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(EnumUserRole name);
}
