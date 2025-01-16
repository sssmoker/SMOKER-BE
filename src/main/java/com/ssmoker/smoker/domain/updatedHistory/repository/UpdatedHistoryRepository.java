package com.ssmoker.smoker.domain.updatedHistory.repository;

import com.ssmoker.smoker.domain.updatedHistory.domain.UpdatedHistory;
import com.ssmoker.smoker.domain.updatedHistory.dto.UpdatedHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdatedHistoryRepository extends JpaRepository<UpdatedHistory, Long> {

    @Query(value = "SELECT new com.ssmoker.smoker.domain.updatedHistory.dto.UpdatedHistoryResponse(" +
            "uh.member.nickName, uh.member.updateCount, uh.createdAt) " +
            "FROM UpdatedHistory uh " +
            "JOIN uh.smokingArea sa " +
            "WHERE sa.id = :smokingAreaId",
            countQuery = "SELECT COUNT(uh) FROM UpdatedHistory uh JOIN uh.smokingArea sa WHERE sa.id = :smokingAreaId")
    Page<UpdatedHistoryResponse> findPagedMemberDetailsBySmokingAreaId(@Param("smokingAreaId") Long smokingAreaId, PageRequest pageRequest);

    @Query(value = "SELECT new com.ssmoker.smoker.domain.updatedHistory.dto.UpdatedHistoryResponse(" +
            "uh.member.nickName, uh.member.updateCount, uh.createdAt) " +
            "FROM UpdatedHistory  uh " +
            "JOIN uh.member m " +
            "WHERE m.id = :memberId",
            countQuery = "SELECT COUNT(uh) FROM UpdatedHistory uh JOIN uh.member m WHERE m.id = :memberId")
    Page<UpdatedHistoryResponse> findUpdatedHistoriesByMemberId(@Param("memberId") Long memberId, PageRequest pageRequest);
}
