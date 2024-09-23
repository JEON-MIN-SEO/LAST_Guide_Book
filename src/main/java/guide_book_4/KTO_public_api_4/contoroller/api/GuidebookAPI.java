package guide_book_4.KTO_public_api_4.contoroller.api;

import guide_book_4.KTO_public_api_4.dto.DayDTO;
import guide_book_4.KTO_public_api_4.dto.GuidebookDTO;
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

    // 사용자가 만든 모든 가이드북 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<GuidebookDTO>>> getGuidebooksByUserId(@PathVariable("userId") Long userId) {
        try {
            List<GuidebookDTO> guidebooks = guidebookService.getGuidebooksByUserId(userId);
            ApiResponse<List<GuidebookDTO>> response = new ApiResponse<>(guidebooks);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<List<GuidebookDTO>> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 가이드북 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createGuidebook(@RequestBody GuidebookDTO guidebookDTO) {
        try {
            guidebookService.createGuidebook(guidebookDTO);
            ApiResponse<String> response = new ApiResponse<>("Guidebook created successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 일정 추가
    @PostMapping("/{guidebookId}/updateDays")
    public ResponseEntity<ApiResponse<String>> updateDaysInGuidebook(@PathVariable("guidebookId") Long guidebookId, @RequestBody List<DayDTO> dayDTOs) {
        try {
            guidebookService.updateDaysInGuidebook(guidebookId, dayDTOs);
            ApiResponse<String> response = new ApiResponse<>("Days updated successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 가이드북 삭제
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
