package com.study.springboot.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.springboot.dao.member2DAO;
import com.study.springboot.dto.member2DTO;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final member2DAO memberDao;

    public CustomUserDetailsService(member2DAO memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {

        member2DTO member = memberDao.findByUserId(userId);

        if (member == null) {
            throw new UsernameNotFoundException("사용자 없음");
        }

        return new CustomUserDetails(member);
    }
}