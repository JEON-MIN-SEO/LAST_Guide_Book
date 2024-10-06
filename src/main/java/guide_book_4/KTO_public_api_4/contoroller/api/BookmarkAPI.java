package guide_book_4.KTO_public_api_4.contoroller.api;

import guide_book_4.KTO_public_api_4.dto.BookmarkDTO;
import guide_book_4.KTO_public_api_4.error.ApiResponse;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//CRUD 「作成（Create）」「読み出し（Read）」「更新（Update）」「削除（Delete）」
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkAPI {

    private final BookmarkService bookmarkService;

    public BookmarkAPI(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // 특정 유저의 모든 북마크 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<BookmarkDTO>>> getUserBookmarks(@PathVariable("userId") Long userId) {
        try {
            List<BookmarkDTO> bookmarks = bookmarkService.getUserBookmarks(userId);
            ApiResponse<List<BookmarkDTO>> response = new ApiResponse<>(bookmarks);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<List<BookmarkDTO>> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
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

    // 특정 가이드북에 있는 북마크 조회
    @GetMapping("/guidebook/{guidebookId}")
    public ResponseEntity<ApiResponse<List<BookmarkDTO>>> getGuidebookBookmarks(@PathVariable Long guidebookId) {
        try {
            List<BookmarkDTO> bookmarks = bookmarkService.getGuidebookBookmarks(guidebookId);
            ApiResponse<List<BookmarkDTO>> response = new ApiResponse<>(bookmarks);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<List<BookmarkDTO>> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}


        /* 基本エラー勝利コード
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        */