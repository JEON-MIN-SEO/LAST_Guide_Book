package guide_book_4.KTO_public_api_4.service;

import guide_book_4.KTO_public_api_4.dto.BookmarkDTO;
import guide_book_4.KTO_public_api_4.dto.DayBookmarkDTO;
import guide_book_4.KTO_public_api_4.dto.DayDTO;
import guide_book_4.KTO_public_api_4.dto.GuidebookDTO;
import guide_book_4.KTO_public_api_4.entity.*;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuidebookService {

    private final GuidebookRepository guidebookRepository;
    private final DayRepository dayRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final DayBookmarkRepository dayBookmarkRepository;

    public GuidebookService(GuidebookRepository guidebookRepository, DayRepository dayRepository,
                            BookmarkRepository bookmarkRepository,UserRepository userRepository,
                            DayBookmarkRepository dayBookmarkRepository) {
        this.guidebookRepository = guidebookRepository;
        this.dayRepository = dayRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.dayBookmarkRepository = dayBookmarkRepository;
    }

    // 1. 가이드북 조회
    public List<GuidebookDTO> getAllGuidebooks(Long userId) {
        // UserEntity를 가져오거나, 필요한 경우 Repository를 통해 조회
        UserEntity user = new UserEntity(); // 혹은 userId를 통해 DB에서 UserEntity를 조회
        user.setId(userId); // userId 설정

        List<GuidebookEntity> guidebooks = guidebookRepository.findAllByUserId(user);
        return guidebooks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GuidebookDTO convertToDTO(GuidebookEntity guidebook) {
        GuidebookDTO dto = new GuidebookDTO();
        dto.setId(guidebook.getId());
        dto.setTitle(guidebook.getTitle());
        dto.setDestination(guidebook.getDestination());
        dto.setStartDate(guidebook.getStartDate());
        dto.setEndDate(guidebook.getEndDate());
        dto.setUserId(guidebook.getUserId().getId());

        return dto;
    }

    // 2. 가이드북 상세 조회 (모든 정보 포함)
    public GuidebookDTO getFullGuidebook(Long guidebookId) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1003, "Guidebook not found"));

        // 가이드북 엔티티를 DTO로 변환
        return convertToFullDTO(guidebook);
    }

    private GuidebookDTO convertToFullDTO(GuidebookEntity guidebook) {
        GuidebookDTO dto = new GuidebookDTO();
        dto.setId(guidebook.getId());
        dto.setTitle(guidebook.getTitle());
        dto.setDestination(guidebook.getDestination());
        dto.setStartDate(guidebook.getStartDate());
        dto.setEndDate(guidebook.getEndDate());
        dto.setUserId(guidebook.getUserId().getId());

        // DayEntity를 DayDTO로 변환
        List<DayDTO> dayDTOs = guidebook.getDays().stream()
                .map(this::convertDayToDTO)
                .collect(Collectors.toList());
        dto.setDays(dayDTOs);

        return dto;
    }

    private DayDTO convertDayToDTO(DayEntity day) {
        DayDTO dayDTO = new DayDTO();
        dayDTO.setId(day.getId());
        dayDTO.setDayNumber(day.getDayNumber());

        // DayBookmarkEntity를 통해 BookmarkEntity 가져오기
        List<BookmarkDTO> bookmarkDTOs = day.getDayBookmarks().stream()
                .map(dayBookmark -> convertBookmarkToDTO(dayBookmark.getBookmark()))
                .collect(Collectors.toList());

        dayDTO.setBookmarks(bookmarkDTOs);
        return dayDTO;
    }

    private BookmarkDTO convertBookmarkToDTO(BookmarkEntity bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setId(bookmark.getId());
        dto.setContentId(bookmark.getContentId());
        dto.setTitle(bookmark.getTitle());
        dto.setFirstimage(bookmark.getFirstimage());
        dto.setFirstimage2(bookmark.getFirstimage2());
        dto.setAreacode(bookmark.getAreacode());
        dto.setAddr1(bookmark.getAddr1());
        dto.setContenttype(bookmark.getContenttype());
        dto.setTel(bookmark.getTel());
        dto.setEventstartdate(bookmark.getEventstartdate());
        dto.setEventenddate(bookmark.getEventenddate());
        dto.setOverview(bookmark.getOverview());
        return dto;
    }


    // 3. 가이드북 생성 (시작일과 종료일을 기준으로 DayEntity를 생성)
    @Transactional
    public GuidebookEntity createGuidebook(GuidebookDTO guidebookDTO) {
        UserEntity user = userRepository.findById(guidebookDTO.getUserId())
                .orElseThrow(() -> new CustomException(1001, "User not found"));

        GuidebookEntity guidebook = new GuidebookEntity();
        guidebook.setDestination(guidebookDTO.getDestination());
        guidebook.setStartDate(guidebookDTO.getStartDate());
        guidebook.setEndDate(guidebookDTO.getEndDate());
        guidebook.setTitle(guidebookDTO.getTitle());
        guidebook.setUserId(user);

        guidebookRepository.save(guidebook);

        // DayEntity 생성
        createDaysForGuidebook(guidebook);

        return guidebook;
    }

    // 시작일과 종료일을 기준으로 DayEntity를 생성
    private void createDaysForGuidebook(GuidebookEntity guidebook) {
        LocalDate startDate = guidebook.getStartDate();
        LocalDate endDate = guidebook.getEndDate();

        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        for (int i = 0; i < numberOfDays; i++) {
            DayEntity day = new DayEntity();
            day.setDayNumber(i + 1);  // dayNumber는 1부터 시작
            day.setGuidebook(guidebook);
            dayRepository.save(day);
        }
    }

    // 일정 추가 기능
    @Transactional
    public void updateDays(Long guidebookId, List<DayDTO> days) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1003, "Guidebook not found"));

        for (DayDTO dayDTO : days) {
            // bookmarkIds가 null인 경우 빈 리스트로 초기화
            if (dayDTO.getBookmarkIds() == null) {
                dayDTO.setBookmarkIds(new ArrayList<>()); // bookmarkIds 초기화
            }

            DayEntity dayEntity = dayRepository.findByGuidebookAndDayNumber(guidebook, dayDTO.getDayNumber())
                    .orElseGet(() -> {
                        DayEntity newDayEntity = new DayEntity();
                        newDayEntity.setDayNumber(dayDTO.getDayNumber());
                        newDayEntity.setGuidebook(guidebook);
                        return newDayEntity;
                    });

            // bookmarkIds를 사용하여 BookmarkEntity를 가져온다
            List<BookmarkEntity> bookmarks = bookmarkRepository.findAllById(dayDTO.getBookmarkIds());

            // 기존 DayBookmarkEntity를 삭제하고 새로 추가
            dayEntity.getDayBookmarks().clear(); // 기존 DayBookmarkEntity 제거
            for (BookmarkEntity bookmark : bookmarks) {
                DayBookmarkEntity dayBookmarkEntity = new DayBookmarkEntity();
                dayBookmarkEntity.setDay(dayEntity); // 연결
                dayBookmarkEntity.setBookmark(bookmark); // 연결
                dayEntity.getDayBookmarks().add(dayBookmarkEntity); // 추가
            }

            dayRepository.save(dayEntity);
        }
    }




    // 5. 가이드북 삭제
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
