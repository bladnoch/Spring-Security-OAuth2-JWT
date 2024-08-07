package org.example.security_oauth_jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.security_oauth_jwt.dto.*;
import org.example.security_oauth_jwt.entity.UserEntity;
import org.example.security_oauth_jwt.repository.UserRepository;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


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


        UserEntity existData = userRepository.findByUsername(username);

        // login을 하지 않아 null인 경우
        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);


            UserDTO userDTO = new UserDTO();
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");
            userDTO.setUsername(username);

            return new CustomOAuth2User(userDTO);
        }
        // 한번이라도 login을 해서 데이터가 남아있는 경우
        else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }




    }
}
