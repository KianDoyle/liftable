package com.kd.liftable.repositories;

import com.kd.liftable.models.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NameRepository extends JpaRepository<Name, String> {

    @Query(value = "SELECT name FROM lifter_names WHERE MATCH(name) AGAINST (:searchTerm IN NATURAL LANGUAGE MODE) LIMIT 10", nativeQuery = true)
    List<Name> searchByNameOnlyEfficient(@Param("searchTerm") String searchTerm);

}
