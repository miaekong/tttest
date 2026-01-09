package com.example.movie_manager.service;

import com.example.movie_manager.entity.Role;
import com.example.movie_manager.entity.UserAccount;
import com.example.movie_manager.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userRepo;

    // 최초 실행 시 기본 계정 생성 (팀프로젝트에서 “로그인 안됨” 가장 흔한 사고 방지)
    @PostConstruct
    public void initUsers() {
        userRepo.findByUsername("admin").orElseGet(() ->
                userRepo.save(UserAccount.builder()
                        .username("admin")
                        .password("1234")
                        .nickname("관리자")
                        .role(Role.ADMIN)
                        .build())
        );

        userRepo.findByUsername("user").orElseGet(() ->
                userRepo.save(UserAccount.builder()
                        .username("user")
                        .password("1234")
                        .nickname("일반사용자")
                        .role(Role.USER)
                        .build())
        );
    }

    public UserAccount login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}