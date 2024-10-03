package guide_book_4.KTO_public_api_4.entity;

import guide_book_4.KTO_public_api_4.id.DayBookmarkId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "day_bookmark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayBookmarkEntity {

    @EmbeddedId
    @Id
    private DayBookmarkId id;

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id")
    private DayEntity day;

    @ManyToOne
    @MapsId("bookmarkId")
    @JoinColumn(name = "bookmark_id")
    private BookmarkEntity bookmark;
}
