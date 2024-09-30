package guide_book_4.KTO_public_api_4.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "day_bookmark")
@Data
@Getter
@Setter
public class DayBookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 ID 생성
    private Long id; // 기본 키 필드 추가

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private DayEntity day;

    @ManyToOne
    @JoinColumn(name = "bookmark_id", nullable = false)
    private BookmarkEntity bookmark;
}
