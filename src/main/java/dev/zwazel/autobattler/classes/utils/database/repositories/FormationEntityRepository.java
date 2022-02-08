package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.utils.database.FormationEntity;
import dev.zwazel.autobattler.classes.utils.database.FormationOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormationEntityRepository extends JpaRepository<FormationEntity, Long> {
    List<FormationOnly> findAllByUserIdOrderById(Long userId);
    boolean existsByFormationJson(String formationJson);
    Optional<FormationEntity> findByUserIdAndId(Long userId, Long id);
}
