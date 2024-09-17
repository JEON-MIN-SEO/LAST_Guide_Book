package guide_book_4.KTO_public_api_4.contoroller.api;

import guide_book_4.KTO_public_api_4.dto.UserDTO;
import guide_book_4.KTO_public_api_4.error.ApiResponse;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class JoinAPI {

    private final JoinService joinService;

    public JoinAPI (JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> JoinProcess(UserDTO userDTO) {
        try {
            joinService.JoinProcess(userDTO);
            ApiResponse<String> response = new ApiResponse<>("User registered successfully");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(e.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

