package guide_book_4.KTO_public_api_4.service;


import guide_book_4.KTO_public_api_4.dto.BookmarkDTO;
import guide_book_4.KTO_public_api_4.entity.BookmarkEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.repository.BookmarkRepository;
import guide_book_4.KTO_public_api_4.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service//ブックマークについてのサービス
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    //ユーザーidでボックマークの全てを読み込む
    public List<Map<String, Object>> getBookmarksByUserId(Long userId) {
        //findByIdを利用してユーザーがあるかを先に検査
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException( 1007, "User not found"));

        //ある場合全てのBookmarkEntityを変換
        List<BookmarkEntity> bookmarks = bookmarkRepository.findAllByUserId(userEntity);

        // 엔티티에서 Map<String, Object>로 변환하여 반환
        return bookmarks.stream()
                .map(bookmark -> {
                    Map<String, Object> bookmarkMap = new HashMap<>();
                    bookmarkMap.put("userId", bookmark.getUserId().getId());
                    bookmarkMap.put("contentId", bookmark.getContentId());
                    bookmarkMap.put("title", bookmark.getTitle());
                    bookmarkMap.put("firstimage", bookmark.getFirstimage());
                    bookmarkMap.put("firstimage2", bookmark.getFirstimage2());
                    bookmarkMap.put("areacode", bookmark.getAreacode());
                    bookmarkMap.put("addr1", bookmark.getAddr1());
                    bookmarkMap.put("tel", bookmark.getTel());
                    bookmarkMap.put("overview", bookmark.getOverview());
                    bookmarkMap.put("eventstartdate", bookmark.getEventstartdate());
                    bookmarkMap.put("eventenddate", bookmark.getEventenddate());
                    bookmarkMap.put("contenttype", bookmark.getContenttype());
                    return bookmarkMap;
                })
                .collect(Collectors.toList());
    }

    public void addBookmark(BookmarkDTO bookmarkDTO) {
        // 有効性検査:空値であることを確認
        if (bookmarkDTO.getUserId() == null || bookmarkDTO.getContentId() == null) {
            throw new CustomException(1004, "User ID and Content ID must not be null");
            //throw new IllegalArgumentException("User ID and Content ID must not be null");
        }

        // UserEntityからユーザーを確認
        UserEntity userEntity = userRepository.findById(bookmarkDTO.getUserId())
                .orElseThrow(() -> new CustomException(1005, "User not found")
                        //new IllegalArgumentException("User not found")
                );

        // 重複検査またはユニークチェック
        if (bookmarkRepository.existsByUserIdAndContentId(userEntity, bookmarkDTO.getContentId())) {
            throw new CustomException(1006, "Bookmark already exists");
        }

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

        bookmarkRepository.save(bookmarkEntity);
    }

    public void deleteBookmark(Long userId, String contentId) {
        // 유저와 북마크가 존재하는지 확인
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(1001, "User not found"));

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByUserIdAndContentId(userEntity, contentId)
                .orElseThrow(() -> new CustomException(1002, "Bookmark not found"));

        // 북마크 삭제
        bookmarkRepository.delete(bookmarkEntity);
    }
}


