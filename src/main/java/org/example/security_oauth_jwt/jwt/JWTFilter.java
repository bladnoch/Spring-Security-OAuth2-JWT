package org.example.security_oauth_jwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.security_oauth_jwt.dto.CustomOAuth2User;
import org.example.security_oauth_jwt.dto.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }


        String authorization = null;
        Cookie[] cookies = request.getCookies();

        // 받은 쿠키에 authorization이 있으면 가져오기
        for (Cookie cookie : cookies) {
            System.out.println("JWTFilter.doFilterInternal");
            System.out.println("cookie.getName()");
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        // authorization 헤더 검증
        if (authorization == null) {

            System.out.println("token not included");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 있을경우 다음을 진행
        //토큰
        String token = authorization;

        // 토큰 소멸시간 검증
        // 소멸 시간이 남지 않았을 경우 종료
        if (jwtUtil.isExpired(token)) {
            System.out.println("token has been expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setName(username);
        userDTO.setRole(role);


        // UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 그 다음 필터 요청
        filterChain.doFilter(request,response);
    }
}
