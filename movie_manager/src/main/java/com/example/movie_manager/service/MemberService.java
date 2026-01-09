package com.example.movie_manager.service;

import com.example.movie_manager.dto.MemberJoinDTO;
import com.example.movie_manager.dto.MemberLoginDTO;
import com.example.movie_manager.entity.Member;
import com.example.movie_manager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;

    public void join(MemberJoinDTO dto) {

        String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
        String nick = dto.getNick() == null ? "" : dto.getNick().trim();
        String pw = dto.getPassword() == null ? "" : dto.getPassword().trim();
        String pw2 = dto.getPasswordConfirm() == null ? "" : dto.getPasswordConfirm().trim();

        if (!StringUtils.hasText(username) || !StringUtils.hasText(nick) || !StringUtils.hasText(pw)) {
            throw new IllegalArgumentException("필수값이 비었습니다.");
        }

        if (!pw.equals(pw2)) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }

        // ✅ Oracle 구버전 안전(우리가 만든 COUNT 방식 메서드)
        if (memberRepo.existsUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (memberRepo.existsNick(nick)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .password(pw) // ✅ 평문 저장
                .nick(nick)
                .role("USER")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        memberRepo.save(member);
    }

    public Member login(MemberLoginDTO dto) {

        String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
        String pw = dto.getPassword() == null ? "" : dto.getPassword().trim();

        Member member = memberRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호 오류"));

        // ✅ 평문 비교
        if (!member.getPassword().equals(pw)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호 오류");
        }

        if (!member.isEnabled()) {
            throw new IllegalArgumentException("비활성화된 계정입니다.");
        }

        return member;
    }
}
