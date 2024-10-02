package guide_book_4.KTO_public_api_4.service;


import guide_book_4.KTO_public_api_4.dto.BookmarkDTO;
import guide_book_4.KTO_public_api_4.entity.BookmarkEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.repository.BookmarkRepository;
//import guide_book_4.KTO_public_api_4.repository.DayBookmarkRepository;
import guide_book_4.KTO_public_api_4.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service//ブックマークについてのサービス
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
//    private final DayBookmarkRepository dayBookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository) { //DayBookmarkRepository dayBookmarkRepository
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
//        this.dayBookmarkRepository = dayBookmarkRepository;
    }

    public void addBookmark(BookmarkDTO bookmarkDTO) {
        // 有効性検査:空値であることを確認
        if (bookmarkDTO.getUserId() == null || bookmarkDTO.getContentId() == null) {
            throw new CustomException(1006, "User ID and Content ID must not be null");
            //throw new IllegalArgumentException("User ID and Content ID must not be null");
        }

        // UserEntityからユーザーを確認
        UserEntity userEntity = userRepository.findById(bookmarkDTO.getUserId())
                .orElseThrow(() -> new CustomException(1001, "User not found"));

        // ブックマーク作成
        BookmarkEntity bookmarkEntity = new BookmarkEntity();
        bookmarkEntity.setUserId(userEntity); // UserEntity 설정
        bookmarkEntity.setContentId(bookmarkDTO.getContentId());
        bookmarkEntity.setTitle(bookmarkDTO.getTitle());
        bookmarkEntity.setFirstimage(bookmarkDTO.getFirstimage());
        bookmarkEntity.setFirstimage2(bookmarkDTO.getFirstimage2());
        bookmarkEntity.setAreacode(bookmarkDTO.getAreacode());
        bookmarkEntity.setAddr1(bookmarkDTO.getAddr1());
        bookmarkEntity.setTel(bookmarkDTO.getTel());
        bookmarkEntity.setOverview(bookmarkDTO.getOverview());
        bookmarkEntity.setEventstartdate(bookmarkDTO.getEventstartdate());
        bookmarkEntity.setEventenddate(bookmarkDTO.getEventenddate());
        bookmarkEntity.setContenttype(bookmarkDTO.getContenttype());

        // 重複検査またはユニークチェック
        if (bookmarkRepository.existsByUserIdAndContentId(userEntity, bookmarkDTO.getContentId())) {
            throw new CustomException(1006, "Bookmark already exists");
            //throw new IllegalArgumentException("Bookmark already exists");
        }

        bookmarkRepository.save(bookmarkEntity);
    }

    @Transactional
    public void deleteBookmark(Long userId, String contentId) {
        // 유저와 북마크가 존재하는지 확인
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(1001, "User not found"));

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByUserIdAndContentId(userEntity, contentId)
                .orElseThrow(() -> new CustomException(1002, "Bookmark not found"));

        // 북마크 삭제
        bookmarkRepository.delete(bookmarkEntity);
    }

    // 특정 유저의 모든 북마크 조회
    public List<BookmarkDTO> getUserBookmarks(Long userId) {
        // 유저와 북마크가 존재하는지 확인
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(1001, "User not found"));

        return bookmarkRepository.findByUserId(userEntity).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
//
//    // 특정 가이드북에 있는 북마크 조회
//    public List<BookmarkDTO> getGuidebookBookmarks(Long guidebookId) {
//        return bookmarkRepository.findByGuidebookId(guidebookId).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    // 엔티티를 DTO로 변환
    private BookmarkDTO convertToDTO(BookmarkEntity bookmarkEntity) {
        BookmarkDTO bookmarkDTO = new BookmarkDTO();
        bookmarkDTO.setId(bookmarkEntity.getId());
        bookmarkDTO.setUserId(bookmarkEntity.getUserId().getId());
        bookmarkDTO.setContentId(bookmarkEntity.getContentId());
        bookmarkDTO.setTitle(bookmarkEntity.getTitle());
        bookmarkDTO.setFirstimage(bookmarkEntity.getFirstimage());
        bookmarkDTO.setFirstimage2(bookmarkEntity.getFirstimage2());
        bookmarkDTO.setAreacode(bookmarkEntity.getAreacode());
        bookmarkDTO.setAddr1(bookmarkEntity.getAddr1());
        bookmarkDTO.setContenttype(bookmarkEntity.getContenttype());
        bookmarkDTO.setTel(bookmarkEntity.getTel());
        bookmarkDTO.setEventstartdate(bookmarkEntity.getEventstartdate());
        bookmarkDTO.setEventenddate(bookmarkEntity.getEventenddate());
        bookmarkDTO.setOverview(bookmarkEntity.getOverview());

        return bookmarkDTO;
    }
}


