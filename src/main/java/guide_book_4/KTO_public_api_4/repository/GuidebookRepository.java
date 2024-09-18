package guide_book_4.KTO_public_api_4.repository;

import guide_book_4.KTO_public_api_4.entity.GuidebookEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuidebookRepository extends JpaRepository<GuidebookEntity, Long> {
    List<GuidebookEntity> findByUserId(UserEntity user);

    //List<GuidebookEntity> findAllByUserId(Long userId);
}
