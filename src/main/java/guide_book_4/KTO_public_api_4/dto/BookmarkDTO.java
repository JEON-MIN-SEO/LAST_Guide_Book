package guide_book_4.KTO_public_api_4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookmarkDTO {

    private Long id;

    private Long userId;

    private String contentId;

    private int contenttype;

    private String title;

    private String firstimage;

    private String firstimage2;

    private String areacode;

    private String addr1;

    private String tel;

    private String overview;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate eventstartdate;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate eventenddate;
}