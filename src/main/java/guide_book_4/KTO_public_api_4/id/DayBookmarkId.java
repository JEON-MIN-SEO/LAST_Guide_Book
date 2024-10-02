package guide_book_4.KTO_public_api_4.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 생성
@Getter
@Setter
public class DayBookmarkId implements Serializable {
    private Long dayId;
    private Long bookmarkId;

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

