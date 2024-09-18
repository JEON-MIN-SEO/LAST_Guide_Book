package guide_book_4.KTO_public_api_4.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GuidebookDTO {
    private Long id;
    private Long userId;  // User의 ID
    private String title; // Guidebook 제목
    private String destination;  // 여행지
    private LocalDate startDate; // 여행 시작일
    private LocalDate endDate;   // 여행 종료일
    private List<DayDTO> days;  // Day 정보 리스트
}
