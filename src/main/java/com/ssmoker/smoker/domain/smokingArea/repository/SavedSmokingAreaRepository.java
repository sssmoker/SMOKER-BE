package com.ssmoker.smoker.domain.smokingArea.repository;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.smokingArea.domain.SavedSmokingArea;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedSmokingAreaRepository extends JpaRepository<SavedSmokingArea, Long> {
    Optional<SavedSmokingArea> findBySmokingAreaAndMember(SmokingArea smokingArea, Member member);

    @Query("SELECT s.smokingArea FROM SavedSmokingArea s WHERE s.member.id = :memberId ORDER BY s.updatedAt DESC")
    List<SmokingArea> findSmokingAreasByMemberId(@Param("memberId") Long memberId);

    // 특정 Member가 저장한 장소 중 주소명으로 검색
    @Query("SELECT s.smokingArea FROM SavedSmokingArea s WHERE s.member.id = :memberId AND s.smokingArea.location.address LIKE CONCAT('%', :address, '%') ORDER BY s.updatedAt DESC")
    List<SmokingArea> findSmokingAreasByMemberIdAndAddress(@Param("memberId") Long memberId, @Param("address") String address);

    // 특정 Member가 저장한 장소 중 장소명으로 검색
    @Query("SELECT s.smokingArea FROM SavedSmokingArea s WHERE s.member.id = :memberId AND s.smokingArea.smokingAreaName LIKE CONCAT('%', :areaName, '%') ORDER BY s.updatedAt DESC")
    List<SmokingArea> findSmokingAreasByMemberIdAndAreaName(@Param("memberId") Long memberId, @Param("areaName") String areaName);
}
