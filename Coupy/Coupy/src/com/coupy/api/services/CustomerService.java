package com.coupy.api.services;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.coupy.api.ClientDTO;
import com.coupy.api.Income;
import com.coupy.api.IncomeType;

import Facade.ClientType;
import Facade.CustomerFacade;
import JavaBeans.Coupon;
import JavaBeans.CouponCatagory;
import JavaBeans.Customer;
import LoginManager.Login;
import exception.CouponException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/customer")
public class CustomerService {
	
	private ClientConfig clientConfig = new ClientConfig();
	private Client client = ClientBuilder.newClient(clientConfig);
//	private WebTarget webTargetIncome = client.target("http://localhost:8888/api/");
	private WebTarget webTargetIncome = client.target("http://localhost:8888/api/");

	private ObjectMapper objectMapper = new ObjectMapper();
	
	

	public synchronized Income storeCustomerIncome(Income Income) throws JsonProcessingException {
		Entity<String> IncomeAsJson = Entity.json(objectMapper.writeValueAsString(Income));
		Builder builder = webTargetIncome.path("/customer/storeTransaction").request();
		Response response = builder.post(IncomeAsJson);
		Income result = response.readEntity(Income.class);
		return result;
	}

	private CustomerFacade getCustomer (HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("customer");
		String sessionName = session.getId();
		// System.out.println(sessionName);
		return customerFacade;
	}

	@GET
	@Path("/login/{email}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public ClientDTO customerLogin(@PathParam("email") String email, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		Login couponSystem = Login.getInstance();
		CustomerFacade customerFacade = (CustomerFacade) couponSystem.login(password, email, ClientType.CUSTOMER);
		HttpSession session = request.getSession(true);
		String sessionName = session.getId();
		System.out.println(sessionName);
		session.setAttribute("customer", customerFacade);
		ClientDTO client = new ClientDTO(email, password, ClientType.CUSTOMER);
		// System.out.println("CompanyService.login() " + sessionName);
		return client;
	}

	@GET
	@Path("/logout")
	public void customerLogout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// System.out.println("CompanyService.companyLogout()");
			session.invalidate();
		}
	}
	
	@GET
	@Path("/getAllCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupons (@Context HttpServletRequest request) throws CouponException {
//	Coupon[] allCoupons = getCustomer(request).getAllCoupons().toArray(new Coupon[0]);
//		List<Coupon> allCoupons;
		Coupon[] allCoupons = getCustomer(request).getAllCoupons().toArray(new Coupon[0]);
	//		if(allCoupons!=null) {
	//				
	//		}
		return Response.status(Response.Status.OK).entity(allCoupons).build();
	}
//	@GET
//	@Path("/getAllCoupons")
//	@Produces(MediaType.APPLICATION_JSON)
//	public ArrayList<Coupon> getAllCoupons (List<Coupon> allCoupons,@Context HttpServletRequest request) throws CouponException {
////		Coupon coupon = new Coupon();
////	Coupon[] allCoupons = getCustomer(request).getAllCoupons().toArray(new Coupon[0]);
////		List<Coupon> allCoupons;
//		allCoupons =  getCustomer(request).getAllCoupons();
//			for(Coupon coupon : allCoupons) {
//				System.out.println(coupon);
//				
//			}
////		allCoupons = new ArrayList<Coupon>();
//		return (ArrayList<Coupon>) allCoupons;
//	}
	@GET
	@Path("/getAllCoupons/4Sale")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCouponsForSale (@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCouponsForSale = getCustomer(request).getAllCouponsForSale().toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allCouponsForSale).build();
	}
	@GET
	@Path("/getAllCoupons/4Sale/byCatagory/{catagory}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCouponsForSaleByCatagory (@PathParam("catagory") String catagory,@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCouponsForSaleByCatagory = getCustomer(request).getAllCouponsForSaleByCatagory(catagory).toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allCouponsForSaleByCatagory).build();
	}
	@GET
	@Path("/getAllCoupons/4Sale/byPrice/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCouponsForSaleByPrice (@PathParam("price") double price,@Context HttpServletRequest request) throws CouponException {
		Coupon[] allCouponsForSaleByPrice = getCustomer(request). getAllCouponsForSaleByPrice(price).toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allCouponsForSaleByPrice).build();
	}
	@GET
	@Path("/Cart")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons(@Context HttpServletRequest request) throws CouponException {
		Coupon[] allPurchasedCoupons = getCustomer(request).getAllPurchasedCoupons().toArray(new Coupon[0]);
			return Response.status(Response.Status.OK).entity(allPurchasedCoupons).build();
	}
	@GET
	@Path("/Cart/byCatagory/{catagory}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByCatagory(@PathParam("catagory") String catagory,@Context HttpServletRequest request) throws CouponException {
		Coupon[] allPurchasedCouponsByCatagory = getCustomer(request).getAllPurchasedCouponsByCatagory(catagory).toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allPurchasedCouponsByCatagory).build();
	}
	@GET
	@Path("/Cart/byPrice/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByPrice(@PathParam("price") double price,@Context HttpServletRequest request) throws CouponException {
		Coupon[] allPurchasedCouponsByPrice = getCustomer(request).getAllPurchasedCouponsByPrice(price).toArray(new Coupon[0]);
		return Response.status(Response.Status.OK).entity(allPurchasedCouponsByPrice).build();

	}
	@POST
	@Path("/Cart")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon purchaseCoupon(Coupon purchasedCoupon, @Context HttpServletRequest request) throws CouponException {
		getCustomer(request).purchaseCoupon(purchasedCoupon);
		Income income = new Income();
		StringBuilder customerID = new StringBuilder();
		Date date=new Date();
		income.setName(purchasedCoupon.getTitle());
		income.setDescription(IncomeType.CUSTOMER_PURCHASE);
		income.setTransactionDate(date);
		income.setAmount(purchasedCoupon.getPrice());
		customerID.append("Cu");
		customerID.append(getProfile(request).getId());
		String clientID=customerID.toString();
		income.setClientID(clientID);
		try {
			storeCustomerIncome(income);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return purchasedCoupon;
	}
	@GET
	@Path("/Catagories/{catagories}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCatagories (@Context HttpServletRequest request) throws CouponException {
		CouponCatagory[] allCatagories = getCustomer(request). getAllCatagories().toArray(new CouponCatagory[0]);
		return Response.status(Response.Status.OK).entity(allCatagories).build();
	}
	@GET
	@Path("/user/")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getProfile(@Context HttpServletRequest request) throws CouponException {
		Customer c1 = getCustomer(request).getCustomer();
		return c1;
	}
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer addCustomer(Customer customerToAdd, @Context HttpServletRequest request) throws CouponException {
		getCustomer(request).AddCustomer(customerToAdd);
		return customerToAdd;
	}
}
