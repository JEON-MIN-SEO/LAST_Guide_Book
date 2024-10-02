package guide_book_4.KTO_public_api_4.contoroller.api;

import guide_book_4.KTO_public_api_4.dto.DayBookmarkDTO;
import guide_book_4.KTO_public_api_4.dto.DayDTO;
import guide_book_4.KTO_public_api_4.dto.GuidebookDTO;
import guide_book_4.KTO_public_api_4.entity.GuidebookEntity;
import guide_book_4.KTO_public_api_4.error.ApiResponse;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.service.GuidebookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guidebook")
public class GuidebookAPI {

    private final GuidebookService guidebookService;

    public GuidebookAPI(GuidebookService guidebookService) {
        this.guidebookService = guidebookService;
    }

    // 1. 가이드북 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<GuidebookDTO>>> getAllGuidebooks(@PathVariable("userId") Long userId) {
        try {
            List<GuidebookDTO> guidebooks = guidebookService.getAllGuidebooks(userId);
            ApiResponse<List<GuidebookDTO>> response = new ApiResponse<>(guidebooks);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<List<GuidebookDTO>> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 2. 가이드북 상세 조회
    @GetMapping("/{guidebookId}/full")
    public ResponseEntity<ApiResponse<GuidebookDTO>> getFullGuidebook(@PathVariable("guidebookId") Long guidebookId) {
        try {
            GuidebookDTO guidebook = guidebookService.getFullGuidebook(guidebookId);
            ApiResponse<GuidebookDTO> response = new ApiResponse<>(guidebook);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<GuidebookDTO> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 3. 가이드북 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createGuidebook(@RequestBody GuidebookDTO guidebookDTO) {
        try {
            GuidebookEntity guidebookEntity = guidebookService.createGuidebook(guidebookDTO);
            ApiResponse<Long> response = new ApiResponse<>(guidebookEntity.getId());
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<Long> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 4. 일정 추가
    @PostMapping("/{guidebookId}/updateDays")
    public ResponseEntity<ApiResponse<String>> updateDays(@PathVariable("guidebookId") Long guidebookId, @RequestBody List<DayDTO> days) {
        try {
            guidebookService.updateDays(guidebookId, days);
            ApiResponse<String> response = new ApiResponse<>("Days updated successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 5. 가이드북 삭제
    @DeleteMapping("/{guidebookId}")
    public ResponseEntity<ApiResponse<String>> deleteGuidebook(@PathVariable("guidebookId") Long guidebookId) {
        try {
            guidebookService.deleteGuidebook(guidebookId);
            ApiResponse<String> response = new ApiResponse<>("Guidebook deleted successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}