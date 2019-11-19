package com.oriel.spring.oriel.CouponSystem.controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oriel.spring.oriel.CouponSystem.entities.Income;

@RestController
@RequestMapping("api/")
public class IncomeRestController {

	@Autowired
	private com.oriel.spring.oriel.CouponSystem.repositories.IncomeServiceTemplate IncomeServiceTemplate;

	@RequestMapping(value = "company/storeTransaction", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public synchronized void storeCompanyIncome(@RequestBody Income income) {
		System.out.println(income);
//		StringBuilder CompanyID = new StringBuilder();
//		CompanyID.append("Co");
//		CompanyID.append(income.getClientID());
//		income.setClientID(CompanyID.toString());
		IncomeServiceTemplate.storeIncome(income);
	}

	@RequestMapping(value = "customer/storeTransaction", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public synchronized void storeCustomerIncome(@RequestBody Income income) {
		StringBuilder CustomerID = new StringBuilder();
//		CustomerID.append("Cu");
//		CustomerID.append(income.getClientID());
//		income.setClientID(CustomerID.toString());
		IncomeServiceTemplate.storeIncome(income);
	}

	@RequestMapping(value = "company/viewIncome/{companyID}", method = RequestMethod.GET, produces = "application/json")
	public synchronized Collection<Income> viewIncomebyCompany(@PathVariable("companyID") long companyID) {
//		Collection<Income> CompanyIncomes = this.incomeServiceDAO.findAllById(companyID);
		Collection<Income> CompanyIncomes = IncomeServiceTemplate.viewByCompany(companyID);
		CompanyIncomes = new ArrayList<>();
		Income[] allCompanyIncomes = CompanyIncomes.toArray(new Income[0]);
		return IncomeServiceTemplate.viewByCompany(companyID);
	}

	@RequestMapping(value = "admin/company/viewIncome/{companyID}", method = RequestMethod.GET, produces = "application/json")
	public synchronized Collection<Income> adminViewIncomebyCompany(@PathVariable("companyID") long companyID) {
		Collection<Income> CompanyIncomes = IncomeServiceTemplate.viewByCompany(companyID);
		CompanyIncomes = new ArrayList<>();
		Income[] allCompanyIncomes = CompanyIncomes.toArray(new Income[0]);

		return IncomeServiceTemplate.viewByCompany(companyID);
	}

	@RequestMapping(value = "admin/customer/viewIncome/{customerID}", method = RequestMethod.GET, produces = "application/json")
	public synchronized Collection<Income> adminViewIncomebyCustomer(@PathVariable("customerID") long customerID) {
		Collection<Income> customerIncomes = IncomeServiceTemplate.viewByCustomer(customerID);
		customerIncomes = new ArrayList<>();
		Income[] allCustomerIncomes = customerIncomes.toArray(new Income[0]);

		return IncomeServiceTemplate.viewByCustomer(customerID);
	}

	@RequestMapping(value = "admin/viewIncome", method = RequestMethod.GET, produces = "application/json")
	public synchronized Collection<Income> viewAllIncomes() {
		Collection<Income> Incomes = IncomeServiceTemplate.viewAllIncome();
		Incomes = new ArrayList<>();
		Income[] allIncomes = Incomes.toArray(new Income[0]);

		return IncomeServiceTemplate.viewAllIncome();
	}

}
