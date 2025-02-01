package com.ssmoker.smoker.domain.smokingArea.repository;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SmokingAreaJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public SmokingAreaJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] bulkInsert(List<SmokingArea> smokingAreas) {
        String sql = "INSERT INTO smoking_area (" +
                "smoking_area_name, latitude, longitude, address, area_type, " +
                "has_air_purifier, has_air_conditioning, has_chair, has_trash_bin, " +
                "has_ventilation_system, is_accessible, has_cctv, has_emergency_button, " +
                "has_voice_guidance, has_fire_extinguisher, is_regularly_cleaned, " +
                "has_cigarette_disposal, has_sunshade, has_rain_protection" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SmokingArea sa = smokingAreas.get(i);
                Feature feature = sa.getFeature();

                ps.setString(1, sa.getSmokingAreaName());
                ps.setDouble(2, sa.getLocation().getLatitude());
                ps.setDouble(3, sa.getLocation().getLongitude());
                // address 컬럼은 Location 객체의 address 필드를 사용합니다.
                ps.setString(4, sa.getLocation().getAddress());
                ps.setString(5, sa.getAreaType());

                ps.setBoolean(6, feature.getHasAirPurifier());
                ps.setBoolean(7, feature.getHasAirConditioning());
                ps.setBoolean(8, feature.getHasChair());
                ps.setBoolean(9, feature.getHasTrashBin());
                ps.setBoolean(10, feature.getHasVentilationSystem());
                ps.setBoolean(11, feature.getIsAccessible());
                ps.setBoolean(12, feature.getHasCCTV());
                ps.setBoolean(13, feature.getHasEmergencyButton());
                ps.setBoolean(14, feature.getHasVoiceGuidance());
                ps.setBoolean(15, feature.getHasFireExtinguisher());
                ps.setBoolean(16, feature.getIsRegularlyCleaned());
                ps.setBoolean(17, feature.getHasCigaretteDisposal());
                ps.setBoolean(18, feature.getHasSunshade());
                ps.setBoolean(19, feature.getHasRainProtection());
            }

            @Override
            public int getBatchSize() {
                return smokingAreas.size();
            }
        });
    }
}