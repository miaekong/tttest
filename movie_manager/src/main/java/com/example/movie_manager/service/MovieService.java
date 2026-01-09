// [경로] src/main/java/com/example/movie_manager/service/MovieService.java
package com.example.movie_manager.service;

import com.example.movie_manager.entity.Movie;
import com.example.movie_manager.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepo;

    // 업로드 경로 (application.properties)
    @Value("${app.upload.dir}")
    private String uploadDir;

    // =================================================
    // ✅ 관리자 대시보드 통계
    // =================================================
    public long countActiveMovies() {
        return movieRepo.countActive();
    }

    public long countFeaturedMovies() {
        return movieRepo.countFeatured();
    }

    public long countArchivedMovies() {
        return movieRepo.countArchived();
    }

    // =================================================
    // ✅ 생성 / 수정
    // =================================================
    public Movie create(Movie movie) {
        return movieRepo.save(movie);
    }

    public Movie update(Movie movie) {
        return movieRepo.save(movie);
    }

    // =================================================
    // ✅ 파일 업로드 (포스터 저장)
    // =================================================
    public String savePoster(MultipartFile poster) {
        if (poster == null || poster.isEmpty()) {
            return null;
        }

        String originalFilename = poster.getOriginalFilename();
        String ext = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String savedFileName = UUID.randomUUID() + ext;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, savedFileName);
        try {
            poster.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("포스터 파일 저장 실패", e);
        }

        return savedFileName;
    }

    // =================================================
    // ✅ 목록 / 검색 / 페이징 (Oracle XE / 11g 안전)
    // =================================================
    public Page<Movie> search(String q, int page, int size) {
        int currentPage = Math.max(page, 0);
        int pageSize = Math.max(size, 1);

        int startRow = currentPage * pageSize + 1;
        int endRow = (currentPage + 1) * pageSize;

        List<Movie> content;
        long total;

        if (!StringUtils.hasText(q)) {
            content = movieRepo.findActivePaged(startRow, endRow);
            total = movieRepo.countActive();
        } else {
            String keyword = q.trim();
            content = movieRepo.searchActivePaged(keyword, startRow, endRow);
            total = movieRepo.countSearchActive(keyword);
        }

        Pageable pageable = PageRequest.of(
                currentPage,
                pageSize,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return new PageImpl<>(content, pageable, total);
    }

    // =================================================
    // ✅ 단건 조회
    // =================================================
    public Movie findById(Long id) {
        return movieRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: id=" + id));
    }

    // =================================================
    // ✅ 삭제 / 보관 처리
    // =================================================

    // ❌ 물리 삭제 (비권장 – 정말 필요할 때만)
    public void deletePhysical(Long id) {
        movieRepo.deleteById(id);
    }

    // ✅ 소프트 삭제 (관리자 삭제 버튼용)
    public void archive(Long id) {
        int updated = movieRepo.archiveById(id);
        if (updated == 0) {
            throw new IllegalArgumentException("Movie not found: id=" + id);
        }
    }
}
