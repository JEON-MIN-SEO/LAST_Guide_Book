package guide_book_4.KTO_public_api_4.entity;

import guide_book_4.KTO_public_api_4.id.DayBookmarkId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "day_bookmark")
@Data
@Getter
@Setter
@IdClass(DayBookmarkId.class)
public class DayBookmarkEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private DayEntity dayId;

    @Id
    @ManyToOne
    @JoinColumn(name = "bookmark_id", nullable = false)
    private BookmarkEntity bookmarkId;
}
