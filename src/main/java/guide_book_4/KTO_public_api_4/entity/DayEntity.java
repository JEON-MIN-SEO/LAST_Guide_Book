package guide_book_4.KTO_public_api_4.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "days")
public class DayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guidebook_id", nullable = false)
    private GuidebookEntity guidebook;

    @Column(name = "content_json", columnDefinition = "TEXT")
    private String contentJson; // JSON 형식의 콘텐츠 데이터를 저장

}
