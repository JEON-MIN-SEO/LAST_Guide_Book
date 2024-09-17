package guide_book_4.KTO_public_api_4.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final int errorCode;

    public CustomException(int errorCode, String message) {
        super(message); // 부모 클래스의 생성자를 호출하여 메시지를 설정
        this.errorCode = errorCode;
    }
}
