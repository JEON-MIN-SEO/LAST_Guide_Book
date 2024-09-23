package guide_book_4.KTO_public_api_4.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DayDTO {

    private int dayNumber;   // 하루의 번호 (1, 2, 3...)

    // contents 필드를 초기화
    private List<BookmarkDTO> contents = new ArrayList<>();
    // 북마크 객체를 저장
}