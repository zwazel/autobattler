package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.utils.database.UnitOnly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitModelRepository extends JpaRepository<UnitModel, Long> {
    List<UnitOnly> findByName(String name);

    List<UnitOnly> findAllByUserOrderByLevel(User user);
}