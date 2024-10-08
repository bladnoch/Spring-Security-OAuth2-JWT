package org.example.security_oauth_jwt.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// 토큰 생성과 관리를 위한 클래스
@Component
public class JWTUtil {
    /**
     * 토큰 payload에 저장될 정보
     * username, role, 생성일, 만료일
     * JWTUil 구현 메소드
     * JWTUtil 생성자
     * username 확인 메소드
     * role 확인 메소드
     * 만료일 확인 메소드
     */

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret){

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

    }


    // 토큰에서 username 가져오기
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    // 토큰에서 role 가져오기
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    // 토큰에서 만료시간 확인하기
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // jwt 생성
    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredMs))
                .signWith(secretKey)
                .compact();
    }

}
