package nbdream.comment.repository;

import nbdream.comment.domain.Comment;
import nbdream.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.author = :author")
    List<Comment> findByAuthor(@Param("author") Member author);

    @Query("SELECT c FROM Comment c WHERE c.author.id = :authorId")
    List<Comment> findByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT c FROM Comment c WHERE c.bulletin.id = :bulletinId")
    List<Comment> findByBulletinId(@Param("bulletinId") Long bulletinId);

}
