# Spring-Security-OAuth2-JWT

### ch.4: 전체 구성, 동작 원리 및 각 클래스 들의 역할을 보여준다. 
* https://www.youtube.com/watch?v=JQJVHcI7Rzo&list=PLJkjrxxiBSFALedMwcqDw_BPaJ3qqbWeB&index=4


### ch.5: 변수 역할
* application.properties 수정



### ch.7
* 네이버 소셜인증 설정
* 기본 : http://localhost:8080
* 리디렉션 : http://localhost:8080/login/oauth2/code/naver.com
* 강의: https://www.youtube.com/watch?v=fNEtoPmgUHs&list=PLJkjrxxiBSFALedMwcqDw_BPaJ3qqbWeB&index=7
* 참고-인증 후 가져올 정보들 : https://velog.io/@mardi2020/Spring-Boot-Spring-Security-OAuth2-%EB%84%A4%EC%9D%B4%EB%B2%84-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%95%B4%EB%B3%B4%EA%B8%B0
* 강의와 다른점: 강의에선 이름, email만 사용하지만 이 프로젝트에선 생일(birthday), 생년(birthyear) 추가함


### ch.8
* 구글 소셜 인증 설정
* 리디렉션 : http://localhost:8080/login/oauth2/code/google
* 강의 : https://www.youtube.com/watch?v=ed10nojCa9g&list=PLJkjrxxiBSFALedMwcqDw_BPaJ3qqbWeB&index=8


### ch.9
* naver, google을 받기위한 response 생성
* OAuth2Response <-- GoogleResponse, NaverResponse
* CustomOAuthUserService를 통해 response가 google인지 naver인지 구분해 값을 가져온다.

### ch.13
* +JWTUtil.java
* client 전송을 위한 jwt를 쿠키에 담아 전송


### ch.14
* +JWTFilter.java 
* filter chain에 요청에 담긴 jwt를ㄹ 검증하는 필터. 요청 쿠키에 jwt가 존재하는 경우 검증후 SecurityContextHolder에 세션을 생성한다.
* refresh, access token의 구현과 email, social login에 대한 여러 방법을 물어보는 뎃글이 많음
* 강의: https://www.youtube.com/watch?v=9g_iN6rLQcQ&list=PLJkjrxxiBSFALedMwcqDw_BPaJ3qqbWeB&index=14