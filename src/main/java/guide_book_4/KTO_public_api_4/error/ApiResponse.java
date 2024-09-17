package guide_book_4.KTO_public_api_4.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 제외합니다.
public class ApiResponse<T> {
    private T data;
    private Integer errorId;
    private String errorMessage;

    public ApiResponse(T data) {
        this.data = data;
        this.errorId = null;
        this.errorMessage = null;
    }

    public ApiResponse(int errorId, String errorMessage) {
        this.data = null;
        this.errorId = errorId;
        this.errorMessage = errorMessage;
    }
}
