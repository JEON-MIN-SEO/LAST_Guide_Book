package guide_book_4.KTO_public_api_4.repository;

import guide_book_4.KTO_public_api_4.entity.DayBookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DayBookmarkRepository extends JpaRepository<DayBookmarkEntity, Long> {

    @Transactional
    void deleteByBookmarkId(Long bookmarkId); // 특정 북마크 ID로 day_bookmark 삭제
}
