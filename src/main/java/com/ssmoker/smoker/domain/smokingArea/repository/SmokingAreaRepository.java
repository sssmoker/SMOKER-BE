package com.ssmoker.smoker.domain.smokingArea.repository;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SmokingAreaRepository extends JpaRepository<SmokingArea, Long> {
    //null 이 아닌 0을 반환하도록 함
    @Query("select coalesce(count(r), 0) " +
            "from SmokingArea s left join Review r " +
            "on r.smokingArea.id = s.id " +
            "where s.id = :smokingAreaId")
    int findReviewCountBySmokingAreaId(@Param("smokingAreaId") Long smokingAreaId);

    //null이 아닌 0을 반환하도록 함
    @Query("select coalesce(count(sa), 0)" +
            "from SmokingArea s left join SavedSmokingArea sa " +
            "on sa.smokingArea.id = s.id " +
            "where sa.id = :smokingAreaId")
    int findSavedCountBySmokingAreaId(@Param("smokingAreaId") Long smokingAreaId);

    //1km 반경 내의 모든 db를 출력하도록 하기
    @Query(value = """
            select *
            from smoking_area s 
            where ST_Distance_Sphere(
            Point(s.longitude, s.latitude),
            Point(:userLng, :userLat)
                  ) <= (1000 / 1.3)""", nativeQuery = true)
    List<SmokingArea> findBySmokingAreaIdWithin1km(
            @Param("userLat") Double userLat,
            @Param("userLng") Double userLng);
}
