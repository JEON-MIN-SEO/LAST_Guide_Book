package guide_book_4.KTO_public_api_4.repository;

import guide_book_4.KTO_public_api_4.entity.BookmarkEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    // UserEntity와 contentId 조합의 북마크가 이미 존재하는지 확인하는 메소드
    boolean existsByUserIdAndContentId(UserEntity userId, String contentId);
    // 특정 유저의 북마크 목록 조회
    List<BookmarkEntity> findAllByUserId(UserEntity user);
    // 유저와 콘텐츠 ID로 북마크를 조회
    Optional<BookmarkEntity> findByUserIdAndContentId(UserEntity user, String contentId);
    // 컨텐츠 id로 bookmark 객체 찾기
    List<BookmarkEntity> findByContentIdIn(List<String> contentIds);

}