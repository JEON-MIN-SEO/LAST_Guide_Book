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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuidebookService {

    private final GuidebookRepository guidebookRepository;
    private final DayRepository dayRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public GuidebookService(GuidebookRepository guidebookRepository, DayRepository dayRepository,
                            BookmarkRepository bookmarkRepository,UserRepository userRepository) {
        this.guidebookRepository = guidebookRepository;
        this.dayRepository = dayRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    // 1. 가이드북 전체 조회
    public List<GuidebookDTO> getAllGuidebooks(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(1001, "User not found"));

        List<GuidebookEntity> guidebooks = guidebookRepository.findByUserId(user);
        return guidebooks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

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

    // 2. 가이드북 상세 조회 (모든 정보 포함)
    public GuidebookDTO getFullGuidebook(Long guidebookId) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1003, "Guidebook not found"));

        return convertToFullDTO(guidebook);
    }

    private GuidebookDTO convertToFullDTO(GuidebookEntity guidebook) {
        GuidebookDTO dto = convertToDTO(guidebook);
        dto.setUserId(guidebook.getUserId().getId());

        List<DayDTO> dayDTOs = guidebook.getDays().stream().map(this::convertDayToDTO).collect(Collectors.toList());
        dto.setDays(dayDTOs);

        return dto;
    }

    private DayDTO convertDayToDTO(DayEntity day) {
        DayDTO dayDTO = new DayDTO();
        dayDTO.setId(day.getId());
        dayDTO.setDayNumber(day.getDayNumber());

        List<BookmarkDTO> bookmarkDTOs = day.getBookmarks().stream().map(this::convertBookmarkToDTO).collect(Collectors.toList());
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
    public void createGuidebook(GuidebookDTO guidebookDTO) {
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

    // 4. 일정 추가
    @Transactional
    public void updateDays(Long guidebookId, List<DayDTO> days) {
        GuidebookEntity guidebook = guidebookRepository.findById(guidebookId)
                .orElseThrow(() -> new CustomException(1003, "Guidebook not found"));

        for (DayDTO dayDTO : days) {
            // 기존 DayEntity가 존재하는지 확인
            DayEntity dayEntity = dayRepository.findByGuidebookAndDayNumber(guidebook, dayDTO.getDayNumber())
                    .orElseGet(() -> {
                        // 존재하지 않으면 새로 생성
                        DayEntity newDayEntity = new DayEntity();
                        newDayEntity.setDayNumber(dayDTO.getDayNumber());
                        newDayEntity.setGuidebook(guidebook);
                        return newDayEntity;
                    });

            // 북마크 ID를 추출하는 로직 추가
            List<Long> bookmarkIds = dayDTO.getBookmarks().stream()
                    .map(BookmarkDTO::getId)  // BookmarkDTO에서 id (bookmarkId)를 추출
                    .collect(Collectors.toList());

            // 추출한 bookmarkIds로 북마크 목록 가져오기
            List<BookmarkEntity> bookmarks = bookmarkRepository.findAllById(bookmarkIds);


            // 기존 북마크 정보를 업데이트
            dayEntity.setBookmarks(bookmarks);

            // DayEntity 저장 (새로 추가된 DayEntity도 자동으로 저장됨)
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

