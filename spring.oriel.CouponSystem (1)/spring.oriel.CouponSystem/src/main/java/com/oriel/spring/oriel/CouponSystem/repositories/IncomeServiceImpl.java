package com.oriel.spring.oriel.CouponSystem.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oriel.spring.oriel.CouponSystem.entities.Income;


@Service
@Transactional
public class IncomeServiceImpl implements IncomeServiceTemplate{

	@Autowired
	private IncomeServiceRepository rep;
	
	@Override	
	public void storeIncome(Income income) {
		System.out.println("entityManager: " + income);
		rep.save(income);
	}

	@Override
	public Collection<Income> viewAllIncome() {
		return rep.findAll();
	}

	@Override
	public Collection<Income> viewByCustomer(long customerID) {
		StringBuilder CustomerID = new StringBuilder();
		CustomerID.append("Cu");
		CustomerID.append(customerID);
		String clientID = CustomerID.toString();
		List<Income> result = rep.findByClientID(clientID);
//		for(Income income:allIncomes) {
//			if(result.isPresent()) {
//				income=result.get();
//					CustomerIncomes.add(income);
//			}else {
//				income=result.get();
//			}
//		}
		return result;
	}

	@Override
	public Collection<Income> viewByCompany(long companyID) {
		StringBuilder CompanyID = new StringBuilder();
		CompanyID.append("Co");
		CompanyID.append(companyID);
		String clientID = CompanyID.toString();
		List<Income> result = rep.findByClientID(clientID);
		return result;
		}

	
	

}
