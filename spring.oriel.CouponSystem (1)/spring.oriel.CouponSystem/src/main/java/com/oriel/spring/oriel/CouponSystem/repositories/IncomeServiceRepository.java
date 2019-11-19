package com.oriel.spring.oriel.CouponSystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oriel.spring.oriel.CouponSystem.entities.Income;

public interface IncomeServiceRepository extends JpaRepository<Income, Long> {
	
	@Query("SELECT i FROM Income i WHERE i.clientID=?1")
	List<Income> findByClientID(String clientID);

}
