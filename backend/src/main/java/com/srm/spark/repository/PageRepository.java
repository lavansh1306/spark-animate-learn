package com.srm.spark.repository;

import com.srm.spark.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {
    Optional<Page> findByName(String name);
    Boolean existsByName(String name);
}
