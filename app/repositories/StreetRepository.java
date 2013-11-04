package repositories;

import com.vividsolutions.jts.geom.Point;
import models.Street;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StreetRepository extends CrudRepository<Street, Integer> {
        @Transactional(readOnly = true)
        @Query("SELECT b FROM Street b where dwithin(:point, b.geom, :distance)=true")
        public List<Street> findByLocation(@Param("point") Point point, @Param("distance") double distance);
}
