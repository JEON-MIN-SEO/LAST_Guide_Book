package guide_book_4.KTO_public_api_4.repository;

import guide_book_4.KTO_public_api_4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이메일로 사용자 존재 여부 확인
    boolean existsByUsername(String username);

    // 이메일로 사용자 찾기
    UserEntity findByUsername(String username);
}
