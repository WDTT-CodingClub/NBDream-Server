package nbdream.image.repository;

import nbdream.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByTargetId(Long id);

    Optional<Image> findByImageUrl(String s);
}
