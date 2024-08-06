package org.example.security_oauth_jwt.dto;

public interface OAuth2Response {
    // 제공자 (ex. naver, google,)
    String getProvider();
    // 제공자에서 발급해주는 아이디(번호)
    String getroviderId();
    // 이메일
    String getEmail();
    // 사용자 실명(설정한 이름)
    String getName();
}
