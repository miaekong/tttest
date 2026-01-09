package com.example.movie_manager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberJoinDTO {
    private String username;
    private String password;
    private String passwordConfirm;
    private String nick;
}