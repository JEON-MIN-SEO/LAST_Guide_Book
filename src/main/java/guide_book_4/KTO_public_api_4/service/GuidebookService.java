package guide_book_4.KTO_public_api_4.service;

import guide_book_4.KTO_public_api_4.dto.DayDTO;
import guide_book_4.KTO_public_api_4.dto.GuidebookDTO;
import guide_book_4.KTO_public_api_4.entity.DayEntity;
import guide_book_4.KTO_public_api_4.entity.GuidebookEntity;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import guide_book_4.KTO_public_api_4.error.CustomException;
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

    public GuidebookService(GuidebookRepository guidebookRepository, DayRepository dayRepository, UserRepository userRepository) {
        this.guidebookRepository = guidebookRepository;
        this.dayRepository = dayRepository;
        this.userRepository = userRepository;
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
        dto.setContentIds(convertJsonToContentIds(day.getContentJson()));
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

            day.setContentJson(convertContentIdsToJson(dayDTO.getContentIds()));
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

        // GuidebookDTO로 변환
        GuidebookDTO guidebookDTO = new GuidebookDTO();
        guidebookDTO.setTitle(guidebook.getTitle());
        guidebookDTO.setDestination(guidebook.getDestination());
        guidebookDTO.setStartDate(guidebook.getStartDate());
        guidebookDTO.setEndDate(guidebook.getEndDate());

        // DayEntity에서 데이터를 가져와서 DayDTO로 변환
        List<DayEntity> dayEntities = dayRepository.findByGuidebook(guidebook);
        List<DayDTO> dayDTOs = dayEntities.stream()
                .map(dayEntity -> {
                    DayDTO dayDTO = new DayDTO();
                    dayDTO.setDayNumber(dayEntity.getDayNumber());
                    // contentJson을 contentIds 리스트로 변환
                    dayDTO.setContentIds(convertJsonToContentIds(dayEntity.getContentJson()));
                    return dayDTO;
                })
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

