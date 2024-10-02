package guide_book_4.KTO_public_api_4.repository;

import guide_book_4.KTO_public_api_4.entity.DayBookmarkEntity;
import guide_book_4.KTO_public_api_4.entity.DayEntity;
import guide_book_4.KTO_public_api_4.id.DayBookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayBookmarkRepository extends JpaRepository<DayBookmarkEntity, DayBookmarkId> {
    // 해당 북마크가 참조되고 있는지 확인하는 메서드
    List<DayBookmarkEntity> findByBookmarkId(Long bookmarkId);

    //삭제
    void deleteByDay(DayEntity day);
}
