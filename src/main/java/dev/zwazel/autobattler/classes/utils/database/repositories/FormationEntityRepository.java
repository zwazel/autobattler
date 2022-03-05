package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationEntityRepository extends JpaRepository<FormationEntity, Long> {
    List<FormationEntity> findAllByUserOrderById(User user);

    Long countByUser(User user);
}