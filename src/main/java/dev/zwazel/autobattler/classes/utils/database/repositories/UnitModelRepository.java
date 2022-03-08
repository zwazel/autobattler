package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.utils.database.UnitOnly;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitModelRepository extends JpaRepository<UnitModel, Long> {
  List<UnitModel> findByName(String name);
  Long countByUser(User user);
  List<UnitModel> findAllByUserOrderByLevel(User user);
}
