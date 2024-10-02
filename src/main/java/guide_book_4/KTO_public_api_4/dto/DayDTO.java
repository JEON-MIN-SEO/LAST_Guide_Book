package guide_book_4.KTO_public_api_4.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DayDTO {

    private Long id;

    private int dayNumber;

    private List<Long> bookmarkIds; // 해당 일자에 연결될 북마크 ID 목록

    private List<BookmarkDTO> bookmarks;
}