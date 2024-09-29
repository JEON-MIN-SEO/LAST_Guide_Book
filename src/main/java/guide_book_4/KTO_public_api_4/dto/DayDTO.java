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

    private List<Long> bookmarkIds;

    private List<BookmarkDTO> bookmarks;
}