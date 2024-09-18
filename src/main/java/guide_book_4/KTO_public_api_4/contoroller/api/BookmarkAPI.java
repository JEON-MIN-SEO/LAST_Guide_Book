package guide_book_4.KTO_public_api_4.contoroller.api;

import guide_book_4.KTO_public_api_4.dto.BookmarkDTO;
import guide_book_4.KTO_public_api_4.error.ApiResponse;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//CRUD 「作成（Create）」「読み出し（Read）」「更新（Update）」「削除（Delete）」
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkAPI {

    private final BookmarkService bookmarkService;

    public BookmarkAPI(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // ユーザーIDでブックマークリスト照会(しょうかい, 조회）
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBookmarksByUserId(@PathVariable("userId") Long userId) {
        //@PathVariable("userId")이걸 왜 지정해야 하는지 찾아보기 맵핑이 안되는지 확인
        try {
            List<Map<String, Object>> bookmarks = bookmarkService.getBookmarksByUserId(userId);
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(bookmarks);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<List<Map<String, Object>>> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addBookmark(@RequestBody BookmarkDTO bookmarkDTO) {
        try {
            bookmarkService.addBookmark(bookmarkDTO);
            ApiResponse<String> response = new ApiResponse<>("Bookmark added successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        /* 基本エラー勝利コード
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        */
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteBookmark(@RequestParam("userId") Long userId, @RequestParam("contentId") String contentId) {
        try {
            bookmarkService.deleteBookmark(userId, contentId);
            ApiResponse<String> response = new ApiResponse<>("Bookmark deleted successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
