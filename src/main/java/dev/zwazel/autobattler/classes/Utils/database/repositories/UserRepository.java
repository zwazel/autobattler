package dev.zwazel.autobattler.classes.Utils.database.repositories;

import dev.zwazel.autobattler.classes.Utils.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
