package com.example.movie_manager.repository;

import com.example.movie_manager.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // ✅ Oracle 구버전 안전: fetch first(=limit) 안 쓰고 COUNT로 중복 체크
    @Query("select (count(m) > 0) from Member m where m.username = :username")
    boolean existsUsername(@Param("username") String username);

    @Query("select (count(m) > 0) from Member m where m.nick = :nick")
    boolean existsNick(@Param("nick") String nick);

    Optional<Member> findByUsername(String username);
}