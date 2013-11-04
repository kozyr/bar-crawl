package repositories;

import com.vividsolutions.jts.geom.Point;
import models.BarCrawl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarCrawlRepository extends CrudRepository<BarCrawl, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT distinct br FROM BarCrawl br JOIN FETCH br.bars b where dwithin(:point, b.geom, :distance)=true")
    public List<BarCrawl> findByLocation(@Param("point") Point point, @Param("distance") double distance);
}
