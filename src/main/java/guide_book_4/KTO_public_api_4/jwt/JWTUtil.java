package guide_book_4.KTO_public_api_4.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // userId를 뽑아내는 메서드
    public Long getUserId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(Long userId) { //String role

        long expirationTimeMs = 604800000L; // 7 days in milliseconds

        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis())) //현재 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs)) //언제 소멸 할건지
                .signWith(secretKey) //암호화
                .compact(); //토큰 컴팩
    }
}
