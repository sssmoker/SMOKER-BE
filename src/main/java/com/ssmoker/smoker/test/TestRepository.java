package com.ssmoker.smoker.test;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("select count(*) from Test where id = :id")
    int testQuery(int id);
}
