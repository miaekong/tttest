package com.example.movie_manager.controller;

import com.example.movie_manager.dto.MemberJoinDTO;
import com.example.movie_manager.dto.MemberLoginDTO;
import com.example.movie_manager.entity.Member;
import com.example.movie_manager.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // 회원가입 폼
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinDTO", new MemberJoinDTO());
        return "auth/join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@ModelAttribute("joinDTO") MemberJoinDTO dto, Model model) {
        try {
            memberService.join(dto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/join";
        }
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDTO", new MemberLoginDTO());
        return "auth/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") MemberLoginDTO dto,
                        HttpSession session,
                        Model model) {
        try {
            Member member = memberService.login(dto);

            // ✅ 세션 키(프로젝트에서 쓰는 키로 통일)
            session.setAttribute("loginId", member.getId());
            session.setAttribute("loginNick", member.getNick());
            session.setAttribute("loginRole", member.getRole());

            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
