package com.example.urlsorting.sorting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.urlsorting.sorting.entities.Sort;
import com.example.urlsorting.user.entities.User;

public interface SortRepository extends JpaRepository<Sort, Long> {

	Sort findBySortId(Long sortId);
	Sort findByUserAndDestination(User user, String destination);
}
