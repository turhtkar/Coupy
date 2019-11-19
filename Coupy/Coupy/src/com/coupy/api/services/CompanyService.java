package com.coupy.api.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.coupy.api.ClientDTO;
import com.coupy.api.Income;
import com.coupy.api.IncomeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import LoginManager.Login;
import exception.CouponException;
import Facade.ClientType;
import Facade.CompanyFacade;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponCatagory;

@Path("/company")
public class CompanyService {
	private ClientConfig clientConfig = new ClientConfig();
	private Client client = ClientBuilder.newClient(clientConfig);
//	private WebTarget webTargetIncome = client.target("http://localhost:8888/api/");
	private WebTarget webTargetIncome = client.target("http://localhost:8888/api/");

	private ObjectMapper objectMapper = new ObjectMapper();
	
	public synchronized Income storeCompanyIncome(Income Income) throws JsonProcessingException {
//		Income.setDescription(IncomeType.COMPANY_NEW_COUPON);
		Entity<String> IncomeAsJson = Entity.json(objectMapper.writeValueAsString(Income));
		Builder builder = webTargetIncome.path("/company/storeTransaction").request();
		Response response = builder.post(IncomeAsJson);
		Income result = response.readEntity(Income.class);
		return result;
	}
	public synchronized Collection<Income> viewIncomebyCompany(long companyID) {
		Builder builder = webTargetIncome.path("/company/viewIncome/"+companyID).request(MediaType.APPLICATION_JSON);
		Response response = builder.get(Response.class);
		Collection<Income> result = response.readEntity(new GenericType<Collection<Income>>(){});
		return result;
		
	}
	@GET
	@Path("/incomes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompanyIncomes(@Context HttpServletRequest request) throws CouponException {
		Collection<Income> Incomes = viewIncomebyCompany(getProfile(request).getId());
		Income[] allIncomes = Incomes.toArray(new Income[0]);
		return Response.status(Response.Status.OK).entity(allIncomes).build();
	}
	
	private CompanyFacade getCompany(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("company");
		String sessionName = session.getId();
		 System.out.println(sessionName);
		return companyFacade;
	}

	@GET
	@Path("/login/{email}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public ClientDTO companyLogin(@PathParam("email") String email, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		Login couponSystem = Login.getInstance();
		CompanyFacade companyFacade = (CompanyFacade) couponSystem.login(password, email, ClientType.COMPANY);
		HttpSession session = request.getSession(true);
		String sessionName = session.getId();
		 System.out.println(sessionName);
		session.setAttribute("company", companyFacade);
		ClientDTO clientDTO = new ClientDTO(password, email, ClientType.COMPANY);
		// System.out.println("CompanyService.login() " + sessionName);
		return clientDTO;
	}

	@Path("/logout")
	@GET
	public void companyLogout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// System.out.println("CompanyService.companyLogout()");
			session.invalidate();
		}
	}
	@POST
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCoupon(Coupon couponToAdd, @Context HttpServletRequest request) throws CouponException {
		try {
			getCompany(request).addCoupon(couponToAdd);
			Income income = new Income();
			StringBuilder companyID = new StringBuilder();
			Date date = new Date();
			income.setName(couponToAdd.getTitle());
			income.setTransactionDate(date);
			income.setDescription(IncomeType.COMPANY_NEW_COUPON);
			income.setAmount(100);
			companyID.append("Co");
			companyID.append(getProfile(request).getId());
			String clientID=companyID.toString();
			income.setClientID(clientID);
			try {
				storeCompanyIncome(income);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (CouponException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Income income = new Income();
//		StringBuilder companyID = new StringBuilder();
//		Date date = new Date();
//		income.setName(couponToAdd.getTitle());
//		income.setTransactionDate(date);
//		income.setDescription(IncomeType.COMPANY_NEW_COUPON);
//		income.setAmount(100);
//		companyID.append("Co");
//		companyID.append(getProfile(request).getId());
//		String clientID=companyID.toString();
//		income.setClientID(clientID);
//		try {
//			storeCompanyIncome(income);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return Response.status(Response.Status.OK).entity(couponToAdd).build();
		
	}
	@DELETE
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Coupon removeCoupon(Coupon couponToRemove, @Context HttpServletRequest request) throws CouponException {
		getCompany(request).removeCoupon(couponToRemove);
		return couponToRemove;
	}
	@PUT
	@Path("/coupons/{couponId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon updateCoupon(Coupon couponToUpdate, @Context HttpServletRequest request) throws CouponException {
		System.out.println(couponToUpdate);
		getCompany(request).updateCoupon(couponToUpdate);
		Income income = new Income();
		StringBuilder companyID = new StringBuilder();
		Date date = new Date();
		income.setName(getProfile(request).getName());
		income.setTransactionDate(date);
		income.setDescription(IncomeType.COMPANY_UPDATE_COUPON);
		income.setAmount(10);
		companyID.append("Co");
		companyID.append(getProfile(request).getId());
		String clientID=companyID.toString();
		income.setClientID(clientID);
		try {
			storeCompanyIncome(income);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return couponToUpdate;
	}
	
	
	@GET
	@Path("/coupons/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@PathParam("id") long couponId, @Context HttpServletRequest request ) throws CouponException  {
		Coupon coupon = getCompany(request).getCoupon(couponId);
		return coupon;
	}
	@GET
	@Path("/coupons/CompanyCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanyCoupons(@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCompanyCoupons = getCompany(request).getAllCompanyCoupons().toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allCompanyCoupons).build();
	}
	@GET
	@Path("/coupons/CompanyCoupons/byCatagory/{catagory}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCouponsByCatagory(@PathParam("catagory") String catagory ,@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCompanyCouponsByCatagory = getCompany(request).getCouponsByCatagory(new CouponCatagory()).toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allCompanyCouponsByCatagory).build();
		
	}
	@GET
	@Path("/coupons/CompanyCoupons/byPrice/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCouponsUntilPrice(@PathParam("price") double price ,@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCompanyCouponsUntilPrice = getCompany(request).getCouponsUntilPrice(price).toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allCompanyCouponsUntilPrice).build();
	}
	@GET
	@Path("/user/")
	@Produces(MediaType.APPLICATION_JSON)
	public Company getProfile(@Context HttpServletRequest request) throws CouponException {
		Company profile = getCompany(request).getCompany(); 
		return profile;
	}
	@GET
	@Path("/coupons/Catagories/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCatagories (@Context HttpServletRequest request) throws CouponException {
		CouponCatagory[] allCatagories = getCompany(request).getAllCatagories().toArray(new CouponCatagory[0]);
		return Response.status(Response.Status.OK).entity(allCatagories).build();
	}
	
}
