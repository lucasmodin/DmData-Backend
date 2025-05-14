package DmData.cookie.repository;

import DmData.cookie.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT COUNT(DISTINCT v.visitorId) FROM Visit v")
    long countDistinctVisitors();

    //query to get number of visits pr. city
    @Query("SELECT v.city, COUNT(v) FROM Visit v GROUP BY v.city")
    List<Object[]> countByCity();





}
