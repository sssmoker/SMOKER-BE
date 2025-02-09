package com.ssmoker.smoker.domain.review.repository;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.domain.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(
            value = "SELECT r FROM Review r JOIN FETCH r.member WHERE r.smokingArea.id = :smokingAreaId ORDER BY r.createdAt DESC",
            countQuery = "SELECT COUNT(r) FROM Review r WHERE r.smokingArea.id = :smokingAreaId"
    )
    Page<Review> findReviewsWithMemberBySmokingAreaId(@Param("smokingAreaId") Long smokingAreaId, PageRequest pageRequest);

    @Query("SELECT coalesce(avg(r.score), 0) " +
            "FROM Review r WHERE r.smokingArea.id = :smokingAreaId")
    Double findAvgScoreBySmokingId(@Param("smokingAreaId") Long smokingAreaId);

    @Query("SELECT r.score " +
            "FROM Review r WHERE r.smokingArea.id = :smokingAreaId")
    List<Double> findScoresBySmokingAreaId(Long smokingAreaId);

    Page<Review> findAllByMember(Member member,PageRequest pageRequest);
}
