package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.utils.database.FormationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FormationEntityRepository extends CrudRepository<FormationEntity, Long> {
    Optional<List<FormationEntity>> findAllByUserId(Long userId);
    Optional<FormationEntity> findByUserIdAndId(Long userId, Long id);
}
