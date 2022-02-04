package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.utils.database.FormationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormationEntityRepository extends JpaRepository<FormationEntity, Long> {
    Optional<List<FormationEntity>> findAllByUserId(Long userId);

    Optional<FormationEntity> findByUserIdAndId(Long userId, Long id);
}
