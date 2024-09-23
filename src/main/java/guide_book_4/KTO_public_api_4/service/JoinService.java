package guide_book_4.KTO_public_api_4.service;

import guide_book_4.KTO_public_api_4.dto.UserDTO;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import guide_book_4.KTO_public_api_4.error.CustomException;
import guide_book_4.KTO_public_api_4.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void JoinProcess(UserDTO userDTO) {
        // 이메일 유효성 검사: null 또는 빈 값인지 확인
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            throw new CustomException(1001, "Emailは必須項目です");
        }

        // 비밀번호 유효성 검사: null 또는 빈 값인지 확인
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new CustomException(1002, "Passwordは必須項目です");
        }

        //DBへ同じユーザがあるか確認するメソッドが必要、UserRepositoryでexistsByメソッドを使う
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new CustomException(1003, "すでに存在するEmailです"); // 例外処理、エラコード、メッセージ
        } else {
            UserEntity data = new UserEntity();
            data.setUsername(userDTO.getUsername());
            data.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            userRepository.save(data);
        }
    }
}
