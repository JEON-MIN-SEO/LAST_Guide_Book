package guide_book_4.KTO_public_api_4.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "content_id"})
})//"user_id", "content_id" 조합의 컬럼이 중복 저장 되는걸 방지 + DataIntegrityViolationException가 발생
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //여러 BookmarkEntity가 하나의 UserEntity에 연결되는 다대일(Many-to-One) 관계를 설정합니다.
    @JoinColumn(name = "user_id")
    private UserEntity userId; //UserEntityのPK（主キ）をuserIdに設定する

    @Column(name = "content_id")
    private String contentId;

    @Column(name = "contentType")
    private int contenttype;

    @Column(name = "title")
    private String title;

    @Column(name = "firstImage")
    private String firstimage;

    @Column(name = "firstImage2")
    private String firstimage2;

    @Column(name = "areaCode")
    private String areacode;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "tel")
    private String tel;

    @Column(name = "overview")
    @Lob
    private String overview;

    @Column(name = "eventStartDate")
    private LocalDate eventstartdate;

    @Column(name = "eventEndDate")
    private LocalDate eventenddate;
}