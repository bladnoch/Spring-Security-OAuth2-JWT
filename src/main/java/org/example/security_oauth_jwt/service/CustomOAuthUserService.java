package org.example.security_oauth_jwt.service;

import org.example.security_oauth_jwt.dto.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 값을 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("CustomOAuthUserService.loadUser");
        System.out.println(oAuth2User);

        // provider을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // if문 이후 유저 정보를 받기 위한 빈 response 생성
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        }else {
            return null;
        }

        // 추후 작성


        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();


        UserDTO userDTO = new UserDTO();
        userDTO.setName(oAuth2Response.getName());
        userDTO.setRole("ROLE_USER");
        userDTO.setUsername(username);

        return new CustomOAuth2User(userDTO);

    }
}
