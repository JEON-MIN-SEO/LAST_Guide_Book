package guide_book_4.KTO_public_api_4.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "days")
public class DayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayNumber;

    @ManyToOne
    @JoinColumn(name = "guidebook_id")
    private GuidebookEntity guidebook;

    @OneToMany(mappedBy = "day", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<DayBookmarkEntity> dayBookmarks = new ArrayList<>();
}

//    @ManyToMany
//    @JoinTable(
//        name = "day_bookmark",
//        joinColumns = @JoinColumn(name = "day_id"),
//        inverseJoinColumns = @JoinColumn(name = "bookmark_id"))
//    private List<BookmarkEntity> bookmarks;


//    @Column(name = "content_json", columnDefinition = "TEXT")
//    private String contentJson; // JSON 형식의 콘텐츠 데이터를 저장
