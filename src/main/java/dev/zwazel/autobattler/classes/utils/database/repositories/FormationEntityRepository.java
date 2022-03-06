package dev.zwazel.autobattler.classes.utils.database.repositories;

import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.FormationUnitTable;
import dev.zwazel.autobattler.classes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationEntityRepository extends JpaRepository<FormationEntity, Long> {
    List<FormationEntity> findAllByUserOrderById(User user);

    @Query("select f, fU from FormationEntity f, FormationUnitTable fU where fU.positionX = :#{#formationUnitTable.positionX} and fU.positionY = :#{#formationUnitTable.positionY} and fU.unitModel.id = :#{#formationUnitTable.unitModel.id} and fU.unitPriority = :#{#formationUnitTable.unitPriority} and f.user.id = :#{#user.id}")
    List<FormationEntity> findAllByFormationUnitTableAndUser(@Param("formationUnitTable") FormationUnitTable formationUnitTable, @Param("user") User user);

    Long countByUser(User user);
}