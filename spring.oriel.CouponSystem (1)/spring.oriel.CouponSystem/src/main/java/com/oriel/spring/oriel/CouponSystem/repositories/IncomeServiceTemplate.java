package com.oriel.spring.oriel.CouponSystem.repositories;

import java.util.Collection;

import com.oriel.spring.oriel.CouponSystem.entities.Income;

public interface IncomeServiceTemplate {

	public void storeIncome(Income income);
	public Collection<Income> viewAllIncome();
	public Collection<Income> viewByCustomer(long customerID);
	public Collection<Income> viewByCompany(long companyID);
}
