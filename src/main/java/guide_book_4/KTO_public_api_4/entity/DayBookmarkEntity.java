package guide_book_4.KTO_public_api_4.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "day_bookmark")
@Getter
@Setter
public class DayBookmarkEntity {

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private DayEntity day;

    @ManyToOne
    @JoinColumn(name = "bookmark_id", nullable = false)
    private BookmarkEntity bookmark;
}
