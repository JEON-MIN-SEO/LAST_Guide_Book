package guide_book_4.KTO_public_api_4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    //@JsonIgnore //객체를 json으로 직렬화 하지 않는 어노테이션 즉, Json으로 만들지 않고 보낸다.
    @ManyToOne(fetch = FetchType.LAZY) //여러 BookmarkEntity가 하나의 UserEntity에 연결되는 다대일(Many-to-One) 관계를 설정합니다.
    @JoinColumn(name = "user_id")
    private UserEntity userId; //UserEntityのPK（主キ）をuserIdに設定する

    @Column(name = "content_id")
    private String contentId;
}