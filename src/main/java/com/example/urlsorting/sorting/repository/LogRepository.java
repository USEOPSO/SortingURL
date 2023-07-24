package com.example.urlsorting.sorting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.urlsorting.sorting.entities.Log;
import com.example.urlsorting.sorting.entities.Sort;

public interface LogRepository extends JpaRepository<Log, Long> {
	@Query("SELECT l FROM Log l WHERE l.sort.sortId = :sortId")
	List<Log> findBySortId(@Param("sortId") Long sortId);
}
