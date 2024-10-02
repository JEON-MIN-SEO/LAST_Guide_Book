package guide_book_4.KTO_public_api_4.repository;


import guide_book_4.KTO_public_api_4.entity.DayEntity;
import guide_book_4.KTO_public_api_4.entity.GuidebookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<DayEntity, Long> {
    // guidebookId와 dayNumber로 특정 DayEntity 조회
    Optional<DayEntity> findByGuidebookIdAndDayNumber(Long guidebookId, Long dayNumber);


    // GuidebookEntity와 dayNumber로 DayEntity를 조회하는 메서드
    Optional<DayEntity> findByGuidebookAndDayNumber(GuidebookEntity guidebook, int dayNumber);

    void deleteByGuidebook(GuidebookEntity guidebook);

    // 특정 guidebookId에 해당하는 모든 DayEntity를 조회하는 메서드
    List<DayEntity> findByGuidebookId(Long guidebookId);

    // 특정 guidebookId와 dayNumber로 DayEntity를 조회하는 메서드
    Optional<DayEntity> findByGuidebookIdAndDayNumber(Long guidebookId, int dayNumber);
}

