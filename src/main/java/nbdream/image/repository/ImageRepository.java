package nbdream.image.repository;

import nbdream.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByTargetId(Long id);

    @Query("SELECT i FROM Image i WHERE i.targetId IN :targetIds")
    List<Image> findAllByTargetIds(@Param("targetIds") List<Long> targetIds);

    Optional<Image> findByImageUrl(String imageUrl);
}
