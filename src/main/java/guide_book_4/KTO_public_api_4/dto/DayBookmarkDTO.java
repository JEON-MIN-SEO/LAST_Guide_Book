package guide_book_4.KTO_public_api_4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 포함하는 생성자
public class DayBookmarkDTO {

    private int dayNumber;         // 하루의 번호 (예: 1, 2, 3...)

    private List<Long> bookmarkIds; // 해당 일자에 연결될 북마크 ID 목록

    @Override
    public String toString() {
        return "DayBookmarkDTO{" +
                "dayNumber=" + dayNumber +
                ", bookmarkIds=" + bookmarkIds +
                '}';
    }
}
