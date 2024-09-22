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
        dto.setContentId(getBookmarksByContentIds(day.getContentJson())); // 변경된 부분
        return dto;
    }

    public List<BookmarkDTO> getBookmarksByContentIds(String contentJson) {
        List<String> contentIds = convertJsonToContentIds(contentJson); // JSON을 List<String>으로 변환
        List<BookmarkEntity> bookmarks = bookmarkRepository.findByContentIdIn(contentIds);

        return bookmarks.stream()
                .map(this::convertToBookmarkDTO) // BookmarkEntity를 BookmarkDTO로 변환
                .collect(Collectors.toList());
    }


    private BookmarkDTO convertToBookmarkDTO(BookmarkEntity bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setUserId(bookmark.getUserId() != null ? bookmark.getUserId().getId() : null); // UserEntity에서 userId 가져오기
        dto.setContentId(bookmark.getContentId());
        dto.setContenttype(bookmark.getContenttype());
        dto.setTitle(bookmark.getTitle());
        dto.setFirstimage(bookmark.getFirstimage());
        dto.setFirstimage2(bookmark.getFirstimage2());
        dto.setAreacode(bookmark.getAreacode());
        dto.setAddr1(bookmark.getAddr1());
        dto.setTel(bookmark.getTel());
        dto.setOverview(bookmark.getOverview());
        return dto;
    }

    @Transactional
    public void createGuidebook(GuidebookDTO guidebookDTO) {
        UserEntity user = userRepository.findById(guidebookDTO.getUserId())
                .orElseThrow(() -> new CustomException(1005, "User not found"));

        GuidebookEntity guidebook = new GuidebookEntity();
        guidebook.setTitle(guidebookDTO.getTitle());
        guidebook.setDestination(guidebookDTO.getDestination());
        guidebook.setStartDate(guidebookDTO.getStartDate());
        guidebook.setEndDate(guidebookDTO.getEndDate());
        guidebook.setUserId(user);

        guidebookRepository.save(guidebook);

        // startDate와 endDate를 기반으로 DayEntity 생성
        createDaysForGuidebook(guidebook);
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

//            day.setContentJson(convertContentIdsToJson(dayDTO.getContentIds()));
//            dayRepository.save(day);
            // Bookmark 객체의 contentId 목록을 JSON으로 변환
            day.setContentJson(convertContentIdsToJson(dayDTO.getContentId().stream()
                    .map(BookmarkDTO::getContentId)
                    .collect(Collectors.toList())));
            dayRepository.save(day);
        }
    }

    private String convertContentIdsToJson(List<String> contentIds) {
        return String.join(",", contentIds);
    }

    @Transactional(readOnly = true)
    public GuidebookDTO getGuidebookById(Long guidebookId) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1005, "Guidebook not found"));

        GuidebookDTO guidebookDTO = convertToDTO(guidebook);
        List<DayEntity> dayEntities = dayRepository.findByGuidebook(guidebook);
        List<DayDTO> dayDTOs = dayEntities.stream()
                .map(this::convertDayToDTO)
                .collect(Collectors.toList());

        guidebookDTO.setDays(dayDTOs);
        return guidebookDTO;
    }

    // JSON 문자열을 List<String>으로 변환
    private List<String> convertJsonToContentIds(String contentJson) {
        if (contentJson == null || contentJson.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(contentJson.split(",")));
    }

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

