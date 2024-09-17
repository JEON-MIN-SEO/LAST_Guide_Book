package guide_book_4.KTO_public_api_4.service;

import guide_book_4.KTO_public_api_4.dto.CustomUserDetails;
import guide_book_4.KTO_public_api_4.entity.UserEntity;
import guide_book_4.KTO_public_api_4.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB 에서 조회
        UserEntity userDate = userRepository.findByUsername(username);

        if (userDate != null ){
            return new CustomUserDetails(userDate);
        }
        return null;
    }
}
