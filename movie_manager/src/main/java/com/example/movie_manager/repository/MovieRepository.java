package com.example.movie_manager.repository;

import com.example.movie_manager.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // =========================================================
    // ✅ Oracle XE/11g에서도 100% 동작하는 ROWNUM 기반 페이징
    // (Pageable이 만드는 fetch first ? rows only 사용 금지)
    // =========================================================

    // 활성(archived=0) 영화 목록 페이징
    @Query(value = """
        select * from (
            select t.*, rownum rn from (
                select * from movie
                where archived = 0
                order by id desc
            ) t
            where rownum <= :endRow
        )
        where rn >= :startRow
        """, nativeQuery = true)
    List<Movie> findActivePaged(@Param("startRow") int startRow,
                                @Param("endRow") int endRow);

    // 활성(archived=0) 영화 수
    @Query(value = "select count(*) from movie where archived = 0", nativeQuery = true)
    long countActive();

    // 추천(featured=1) && 활성(archived=0) 영화 수
    @Query(value = "select count(*) from movie where featured = 1 and archived = 0", nativeQuery = true)
    long countFeatured();

    // 보관(archived=1) 영화 수
    @Query(value = "select count(*) from movie where archived = 1", nativeQuery = true)
    long countArchived();

    // =========================================================
    // ✅ 검색(제목 기준) + 페이징 (ROWNUM)
    // =========================================================

    // 제목 검색 + 활성(archived=0) 목록 페이징
    @Query(value = """
        select * from (
            select t.*, rownum rn from (
                select * from movie
                where archived = 0
                  and lower(title) like lower('%' || :q || '%')
                order by id desc
            ) t
            where rownum <= :endRow
        )
        where rn >= :startRow
        """, nativeQuery = true)
    List<Movie> searchActivePaged(@Param("q") String q,
                                  @Param("startRow") int startRow,
                                  @Param("endRow") int endRow);

    // 제목 검색 + 활성(archived=0) 총 개수
    @Query(value = """
        select count(*) from movie
        where archived = 0
          and lower(title) like lower('%' || :q || '%')
        """, nativeQuery = true)
    long countSearchActive(@Param("q") String q);

    // =========================================================
    // ✅ 삭제 기능 (소프트 삭제 = archived = 1)
    // =========================================================

    @Modifying
    @Transactional
    @Query("update Movie m set m.archived = true where m.id = :id")
    int archiveById(@Param("id") Long id);
}
