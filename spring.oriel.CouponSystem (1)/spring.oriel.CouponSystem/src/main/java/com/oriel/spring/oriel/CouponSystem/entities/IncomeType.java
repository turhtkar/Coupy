package com.oriel.spring.oriel.CouponSystem.entities;

public enum IncomeType {

	CUSTOMER_PURCHASE("Income For : Customer Purchesed Coupon"), COMPANY_NEW_COUPON("Income For : Company Created New Coupon"), COMPANY_UPDATE_COUPON("Income For : Company Updated an Existing Coupon");
	
	public final String description;
	
	private IncomeType(String description) {
		this.description=description;
	}
	public String getDescription() {
		return description;
	}


}
