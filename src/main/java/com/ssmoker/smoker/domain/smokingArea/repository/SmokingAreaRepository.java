package com.ssmoker.smoker.domain.smokingArea.repository;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SmokingAreaRepository extends JpaRepository<SmokingArea, Long> {
    //null 이 아닌 0을 반환하도록 함
    @Query("select coalesce(count(r), 0) " +
            "from SmokingArea s left join Review r " +
            "on r.smokingArea.id = s.id " +
            "where s.id = :smokingAreaId Group by s")
    int findReviewCountBySmokingAreaId(@Param("smokingAreaId") Long smokingAreaId);

    //null이 아닌 0을 반환하도록 함
    @Query("select coalesce(count(sa) , 0)" +
            "from SmokingArea s left join SavedSmokingArea sa " +
            "on sa.smokingArea.id = s.id " +
            "where sa.id = :smokingAreaId Group by s")
    int findSavedCountBySmokingAreaId(@Param("smokingAreaId") Long smokingAreaId);
}
