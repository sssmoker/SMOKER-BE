package com.ssmoker.smoker.domain.smokingArea.repository;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SmokingAreaRepository extends JpaRepository<SmokingArea, Long> {
}
