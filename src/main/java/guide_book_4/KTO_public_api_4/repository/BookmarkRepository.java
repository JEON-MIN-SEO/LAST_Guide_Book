package guide_book_4.KTO_public_api_4.repository;

import guide_book_4.KTO_public_api_4.entity.BookmarkEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    // 사용자 ID와 콘텐츠 ID로 중복 여부 확인
    boolean existsByUserIdAndContentId(UserEntity user, String contentId);

    // 특정 유저의 북마크 리스트 가져오기
    List<BookmarkEntity> findByUserId(Long userId);

    // 특정 가이드북에 있는 북마크 리스트 가져오기
    @Query("SELECT b FROM BookmarkEntity b JOIN b.days d WHERE d.guidebook.id = :guidebookId")
    List<BookmarkEntity> findByGuidebookId(@Param("guidebookId") Long guidebookId);

    // 특정 유저의 특정 콘텐츠 북마크 찾기
    Optional<BookmarkEntity> findByUserIdAndContentId(UserEntity userId, String contentId);
}