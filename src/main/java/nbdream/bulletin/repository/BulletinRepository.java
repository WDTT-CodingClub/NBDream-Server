package nbdream.bulletin.repository;

import nbdream.bulletin.domain.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {
}
