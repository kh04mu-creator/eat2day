package com.study.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.study.springboot.dao.member2DAO;
import com.study.springboot.dto.member2DTO;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final member2DAO memberDao;

    public CustomOAuth2UserService(member2DAO memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // google/kakao
        String providerId = extractProviderId(provider, oauth2User);
        String email = extractEmail(provider, oauth2User);          // null 가능
        String nickname = extractNickname(provider, oauth2User);    // null 가능

        // providerId는 소셜 계정 식별 핵심값이라 없으면 진행 불가
        if (providerId == null || providerId.isBlank()) {
            throw new OAuth2AuthenticationException("providerId not found");
        }

        member2DTO member = memberDao.findByProvider(provider, providerId);

        // 없으면 자동 회원 생성
        if (member == null) {
            member2DTO dto = new member2DTO();
            dto.setM_provider(provider);
            dto.setM_provider_id(providerId);
            dto.setM_email(email);
            dto.setM_name(nickname != null ? nickname : "소셜회원");
            dto.setM_nickname(nickname != null ? nickname : "소셜회원");
            dto.setM_auth("ROLE_USER");
            dto.setM_verified("N");

            // 소셜은 NULL 가능
            dto.setM_pw(null);
            dto.setM_gender(null);
            dto.setM_birth(null);
            dto.setM_tel(null);
            dto.setM_addr(null);

            memberDao.m_writeSocialDao(dto);
            member = memberDao.findByProvider(provider, providerId);
        }

        // 컨트롤러에서 m_no를 쉽게 꺼낼 수 있도록 attributes 확장
        Map<String, Object> merged = new HashMap<>(oauth2User.getAttributes());
        merged.put("provider", provider);
        merged.put("providerId", providerId);
        merged.put("m_no", member.getM_no());

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(member.getM_auth())),
                merged,
                "m_no"
        );
    }

    /* =========================
       provider별 정보 파싱 메서드
    ========================= */

    private String extractProviderId(String provider, OAuth2User user) {
        if ("google".equals(provider)) {
            // Google 고유 ID
            return user.getAttribute("sub");
        }
        if ("kakao".equals(provider)) {
            // Kakao 고유 ID (Long일 수 있음)
            Object id = user.getAttribute("id");
            return id == null ? null : String.valueOf(id);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private String extractEmail(String provider, OAuth2User user) {
        if ("google".equals(provider)) {
            return user.getAttribute("email");
        }
        if ("kakao".equals(provider)) {
            Map<String, Object> account = user.getAttribute("kakao_account");
            if (account == null) return null;
            Object email = account.get("email");
            return email == null ? null : String.valueOf(email);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private String extractNickname(String provider, OAuth2User user) {
        if ("google".equals(provider)) {
            String name = user.getAttribute("name");
            if (name != null && !name.isBlank()) return name;
            return user.getAttribute("given_name");
        }
        if ("kakao".equals(provider)) {
            // 1) properties.nickname 우선
            Map<String, Object> props = user.getAttribute("properties");
            if (props != null) {
                Object nick = props.get("nickname");
                if (nick != null) return String.valueOf(nick);
            }

            // 2) kakao_account.profile.nickname fallback
            Map<String, Object> account = user.getAttribute("kakao_account");
            if (account != null) {
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");
                if (profile != null) {
                    Object nick = profile.get("nickname");
                    if (nick != null) return String.valueOf(nick);
                }
            }
        }
        return null;
    }
}
