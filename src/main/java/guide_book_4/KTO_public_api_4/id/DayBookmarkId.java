package guide_book_4.KTO_public_api_4.id;

import java.io.Serializable;
import java.util.Objects;

public class DayBookmarkId implements Serializable {
    private Long dayId;
    private Long bookmarkId;

    // 기본 생성자
    public DayBookmarkId() {}

    public DayBookmarkId(Long dayId, Long bookmarkId) {
        this.dayId = dayId;
        this.bookmarkId = bookmarkId;
    }

    // equals() 메서드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayBookmarkId)) return false;
        DayBookmarkId that = (DayBookmarkId) o;
        return Objects.equals(dayId, that.dayId) && Objects.equals(bookmarkId, that.bookmarkId);
    }

    // hashCode() 메서드
    @Override
    public int hashCode() {
        return Objects.hash(dayId, bookmarkId);
    }
}

