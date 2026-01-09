// [경로] src/main/java/com/example/movie_manager/entity/UserAccount.java
package com.example.movie_manager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_account")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "SEQ_USER_GEN",
        sequenceName = "SEQ_USER",
        allocationSize = 1
)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_GEN")
    private Long id;

    @Column(nullable=false, unique=true, length=50)
    private String username;

    @Column(nullable=false, length=100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=10)
    private Role role;

    @Column(nullable=false, length=50)
    private String nickname;
}
