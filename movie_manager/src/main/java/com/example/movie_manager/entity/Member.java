package com.example.movie_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_member_nick", columnNames = "nick")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "SEQ_MEMBER_GEN",
        sequenceName = "SEQ_MEMBER",
        allocationSize = 1
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEMBER_GEN")
    private Long id;

    @Column(nullable = false, length = 30)
    private String username;   // 로그인 ID

    @Column(nullable = false, length = 100)
    private String password;   // 암호화 저장

    @Column(nullable = false, length = 30)
    private String nick;       // 닉네임(표시용)

    @Column(nullable = false, length = 10)
    private String role;       // USER / ADMIN

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}