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

        return bookmarks.stream()
                .map(bookmark -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("contentId", bookmark.getContentId());
                    return map;
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
        // ブックマーク作成
        BookmarkEntity bookmarkEntity = new BookmarkEntity();
        bookmarkEntity.setUserId(userEntity); // UserEntity 설정
        bookmarkEntity.setContentId(bookmarkDTO.getContentId());

        // 重複検査またはユニークチェック
        if (bookmarkRepository.existsByUserIdAndContentId(userEntity, bookmarkDTO.getContentId())) {
            throw new CustomException(1006, "Bookmark already exists");
            //throw new IllegalArgumentException("Bookmark already exists");
        }

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


