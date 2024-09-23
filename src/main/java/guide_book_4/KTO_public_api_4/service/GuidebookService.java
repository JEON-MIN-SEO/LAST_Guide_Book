package guide_book_4.KTO_public_api_4.service;

import guide_book_4.KTO_public_api_4.dto.BookmarkDTO;
import guide_book_4.KTO_public_api_4.dto.DayDTO;
import guide_book_4.KTO_public_api_4.dto.GuidebookDTO;
import guide_book_4.KTO_public_api_4.entity.BookmarkEntity;
import guide_book_4.KTO_public_api_4.entity.DayEntity;
import guide_book_4.KTO_public_api_4.entity.GuidebookEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.repository.BookmarkRepository;
import guide_book_4.KTO_public_api_4.repository.DayRepository;
import guide_book_4.KTO_public_api_4.repository.GuidebookRepository;
import guide_book_4.KTO_public_api_4.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuidebookService {

    private final GuidebookRepository guidebookRepository;
    private final DayRepository dayRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    public GuidebookService(GuidebookRepository guidebookRepository, DayRepository dayRepository, UserRepository userRepository, BookmarkRepository bookmarkRepository) {
        this.guidebookRepository = guidebookRepository;
        this.dayRepository = dayRepository;
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Transactional(readOnly = true)
    public List<GuidebookDTO> getGuidebooksByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(1005, "User not found"));

        List<GuidebookEntity> guidebooks = guidebookRepository.findByUserId(user);
        return guidebooks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 가이드북 엔티티를 DTO로 변환
    private GuidebookDTO convertToDTO(GuidebookEntity guidebook) {
        GuidebookDTO dto = new GuidebookDTO();
        dto.setId(guidebook.getId());
        dto.setTitle(guidebook.getTitle());
        dto.setDestination(guidebook.getDestination());
        dto.setStartDate(guidebook.getStartDate());
        dto.setEndDate(guidebook.getEndDate());
        dto.setDays(guidebook.getDays().stream().map(this::convertDayToDTO).collect(Collectors.toList()));
        return dto;
    }

    private DayDTO convertDayToDTO(DayEntity day) {
        DayDTO dto = new DayDTO();
        dto.setDayNumber(day.getDayNumber());
        dto.setContents(day.getBookmarks().stream().map(this::convertToBookmarkDTO).collect(Collectors.toList()));
        return dto;
    }

    private BookmarkDTO convertToBookmarkDTO(BookmarkEntity bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setUserId(bookmark.getUserId() != null ? bookmark.getUserId().getId() : null);
        dto.setContentId(bookmark.getContentId());
        dto.setContenttype(bookmark.getContenttype());
        dto.setTitle(bookmark.getTitle());
        dto.setFirstimage(bookmark.getFirstimage());
        dto.setFirstimage2(bookmark.getFirstimage2());
        dto.setAreacode(bookmark.getAreacode());
        dto.setAddr1(bookmark.getAddr1());
        dto.setTel(bookmark.getTel());
        dto.setOverview(bookmark.getOverview());
        dto.setEventstartdate(bookmark.getEventstartdate());
        dto.setEventenddate(bookmark.getEventenddate());
        return dto;
    }

    @Transactional
    public GuidebookDTO createGuidebook(GuidebookDTO guidebookDTO) {
        UserEntity user = userRepository.findById(guidebookDTO.getUserId())
                .orElseThrow(() -> new CustomException(1005, "User not found"));

        GuidebookEntity guidebook = new GuidebookEntity();
        guidebook.setUserId(user);
        guidebook.setTitle(guidebookDTO.getTitle());
        guidebook.setDestination(guidebookDTO.getDestination());
        guidebook.setStartDate(guidebookDTO.getStartDate());
        guidebook.setEndDate(guidebookDTO.getEndDate());

        GuidebookEntity savedGuidebook = guidebookRepository.save(guidebook);

        // 시작일과 종료일을 기준으로 DayEntity 생성
        createDaysForGuidebook(guidebook);

        return convertToDTO(savedGuidebook);
    }


    //시작일과 종료일을 기준으로 DayEntity를 생성
    private void createDaysForGuidebook(GuidebookEntity guidebook) {
        LocalDate startDate = guidebook.getStartDate();
        LocalDate endDate = guidebook.getEndDate();

        //ChronoUnit.DAYS.betweenはjava 8から導入された’java.time'パッケージのChronoUnit Enumクラスに含まれたメソッドで
        //二つの日付けの間の計算をしてくれる　（８・２２〜８・３０＝８）
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // 해당 일수만큼 DayEntity 생성
        for (int i = 0; i < numberOfDays; i++) {
            DayEntity day = new DayEntity();
            day.setDayNumber(i + 1);  // dayNumber는 1부터 시작
            day.setGuidebook(guidebook);
            dayRepository.save(day);
        }
    }

    @Transactional
    public void updateDaysInGuidebook(Long guidebookId, List<DayDTO> dayDTOs) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1005, "Guidebook not found"));

        for (DayDTO dayDTO : dayDTOs) {
            DayEntity day = dayRepository.findByGuidebookAndDayNumber(guidebook, dayDTO.getDayNumber())
                    .orElseThrow(() -> new CustomException(1005, "Day not found"));

            // Bookmark 객체 목록 자체를 저장
            List<BookmarkEntity> bookmarks = dayDTO.getContents().stream()
                    .map(bookmarkDTO -> convertToBookmarkEntity(bookmarkDTO, guidebook))
                    .collect(Collectors.toList());
            day.setBookmarks(bookmarks);

            dayRepository.save(day);
        }
    }

    private BookmarkEntity convertToBookmarkEntity(BookmarkDTO bookmarkDTO, GuidebookEntity guidebook) {
        BookmarkEntity bookmarkEntity = new BookmarkEntity();

        // userId를 통해 UserEntity를 가져와 설정
        UserEntity user = userRepository.findById(bookmarkDTO.getUserId())
                .orElseThrow(() -> new CustomException(1005, "User not found"));

        bookmarkEntity.setUserId(user);

        // DayEntity 설정
        DayEntity day = dayRepository.findByGuidebookAndDayNumber(guidebook, bookmarkDTO.getDayNumber())
                .orElseThrow(() -> new CustomException(1005, "Day not found"));
        bookmarkEntity.setDay(day);

        // 필요한 필드들을 DTO에서 Entity로 변환
        bookmarkEntity.setContentId(bookmarkDTO.getContentId());
        bookmarkEntity.setContenttype(bookmarkDTO.getContenttype());
        bookmarkEntity.setTitle(bookmarkDTO.getTitle());
        bookmarkEntity.setFirstimage(bookmarkDTO.getFirstimage());
        bookmarkEntity.setFirstimage2(bookmarkDTO.getFirstimage2());
        bookmarkEntity.setAreacode(bookmarkDTO.getAreacode());
        bookmarkEntity.setAddr1(bookmarkDTO.getAddr1());
        bookmarkEntity.setTel(bookmarkDTO.getTel());
        bookmarkEntity.setOverview(bookmarkDTO.getOverview());
        bookmarkEntity.setEventstartdate(bookmarkDTO.getEventstartdate());
        bookmarkEntity.setEventenddate(bookmarkDTO.getEventenddate());

        return bookmarkEntity;
    }



//    private String convertContentIdsToJson(List<String> contentIds) {
//        return String.join(",", contentIds);
//    }

    // JSON 문자열을 List<String>으로 변환
//    private List<String> convertJsonToContentIds(String contentJson) {
//        if (contentJson == null || contentJson.isEmpty()) {
//            return new ArrayList<>();
//        }
//        return new ArrayList<>(Arrays.asList(contentJson.split(",")));
//    }

    @Transactional
    public void deleteGuidebook(Long guidebookId) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1005, "Guidebook not found"));

        // 가이드북에 관련된 모든 DayEntity 삭제
        dayRepository.deleteByGuidebook(guidebook);

        // 가이드북 삭제
        guidebookRepository.delete(guidebook);
    }
}

