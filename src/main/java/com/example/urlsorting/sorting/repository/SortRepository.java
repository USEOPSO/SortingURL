package com.example.urlsorting.sorting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.urlsorting.sorting.dto.response.SortResponseDto;
import com.example.urlsorting.sorting.entities.Sort;

public interface SortRepository extends JpaRepository<Sort, Long> {
	@Query("SELECT s FROM Sort s WHERE s.user.userId = :userId")
	List<Sort> findByUser(Long userId);
}
