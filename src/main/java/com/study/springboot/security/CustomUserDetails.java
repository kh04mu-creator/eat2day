package com.study.springboot.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.springboot.dto.member2DTO;

public class CustomUserDetails implements UserDetails {

    private final member2DTO member;

    public CustomUserDetails(member2DTO member) {
        this.member = member;
    }

    //  member 전체 접근 (이미 있음)
    public member2DTO getMember() {
        return member;
    }

    //  mno 바로 꺼내기 (⭐ 중요)
    public String getMno() {
        return member.getM_no();   // ← 컬럼명에 맞게
    }
    
    // 닉네임
    public String getNickname() {
        return member.getM_nickname();
    }


    //  로그인 아이디
    public String getLoginId() {
        return member.getM_email();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
            new SimpleGrantedAuthority(member.getM_auth())
        );
    }

    @Override
    public String getPassword() {
        return member.getM_pw();
    }

    @Override
    public String getUsername() {
        return member.getM_email();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}