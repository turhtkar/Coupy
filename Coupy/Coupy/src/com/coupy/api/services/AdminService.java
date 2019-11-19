package com.coupy.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import Facade.AdminFacade;
import Facade.ClientType;
import JavaBeans.Company;
import JavaBeans.Customer;
import LoginManager.Login;
import exception.CouponException;

@Path("/admin")
public class AdminService {
	private ClientConfig clientConfig = new ClientConfig();
	private Client client = ClientBuilder.newClient(clientConfig);
//	private WebTarget webTargetIncome = client.target("http://localhost:8888/api/");
	private WebTarget webTargetIncome = client.target("http://localhost:8888/api/");
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public synchronized Collection<Income> viewIncomebyCompany(long companyID) {
		Builder builder = webTargetIncome.path("/admin/company/viewIncome/"+companyID).request(MediaType.APPLICATION_JSON);
		Response response = builder.get(Response.class);
		Collection<Income> result = response.readEntity(new GenericType<Collection<Income>>(){});
//		String jsonStr = response.readEntity(String.class);
//		
//		JsonNode rootNode = objectMapper.readTree(jsonStr);

//		Collection<Income>result = objectMapper.readValue(rootNode.toString(),new TypeReference<Collection<Income>>(){});
//		Collection<Income>result = response.readEntity(new GenericType<Collection<Income>>(){});
//		Collection<Income> result1 = objectMapper.readValue(, new TypeReference<Collection<Income>>(){});
//		result = new ArrayList<>();
		System.out.println(result.toString());

		return result;
		
	}
	public synchronized Collection<Income> viewIncomebyCustomer(long customerID) {
		String webTargetIncomePath= "/admin/customer/viewIncome/"+customerID;
		Builder builder = webTargetIncome.path(webTargetIncomePath).request(MediaType.APPLICATION_JSON);
		Response response = builder.get(Response.class);
		Collection<Income> result = response.readEntity(new GenericType<Collection<Income>>(){});
		return result;
	}
	public synchronized Collection<Income> viewAllIncoms() {
		Builder builder = webTargetIncome.path("/admin/viewIncome").request(MediaType.APPLICATION_JSON);
		Response response = builder.get(Response.class);
		Collection<Income> result = response.readEntity(new GenericType<Collection<Income>>(){});
//		result = new ArrayList<>();
		return result;
	}
	@GET
	@Path("/company/incomes/{companyID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompanyIncomes(@PathParam("companyID") long companyID,@Context HttpServletRequest request) throws CouponException {
		Collection<Income> Incomes = viewIncomebyCompany(companyID);
		Income[] allIncomes = Incomes.toArray(new Income[0]);
		return Response.status(Response.Status.OK).entity(allIncomes).build();
	}
	@GET
	@Path("/customer/incomes/{customerID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerIncomes(@PathParam("customerID") long customerID,@Context HttpServletRequest request) throws CouponException {
		Collection<Income> CustomerIncomes = viewIncomebyCustomer(customerID);
		Income[] allCustomerIncomes = CustomerIncomes.toArray(new Income[0]);
		return Response.status(Response.Status.OK).entity(allCustomerIncomes).build();
	}
	@GET
	@Path("/incomes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllIncomes(@Context HttpServletRequest request) throws CouponException {
		Collection<Income> Incomes = viewAllIncoms();
		Income[] allIncomes = Incomes.toArray(new Income[0]);
		return Response.status(Response.Status.OK).entity(allIncomes).build();
	}

	private AdminFacade getAdmin (HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("admin");
		String sessionName = session.getId();
		System.out.println(sessionName); //test
		return adminFacade;
	}
	@Path("/login/{email}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ClientDTO adminLogin(@PathParam("email") String email, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		Login couponSystem = Login.getInstance();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login(password, email, ClientType.ADMIN);
		HttpSession session = request.getSession(true);
		String sessionName = session.getId();
		System.out.println(sessionName);
		session.setAttribute("admin", adminFacade);
		// System.out.println("AdminService.adminLogin()");
		ClientDTO client = new ClientDTO(email, password, ClientType.ADMIN);
		return client;
	}

	@Path("/logout")
	@GET
	public void adminLogout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// System.out.println("AdminService.adminLogout()" + session.getId());
			session.invalidate();
		}
	}
	
	@POST
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Company addCompany(Company companyToAdd, @Context HttpServletRequest request) throws CouponException {
		try {
			getAdmin(request).addCompany(companyToAdd);
		} catch (CouponException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companyToAdd;
	}
	
	@DELETE
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Company removeCompany(Company companyToRemove, @Context HttpServletRequest request) throws CouponException {
		getAdmin(request).removeCompany(companyToRemove);
		return companyToRemove;
	}
	
	@PUT
	@Path("/companies/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Company updateCompany(@PathParam("id") long id, Company companyToUpdate, @Context HttpServletRequest request) throws CouponException {
		getAdmin(request).updateCompany(companyToUpdate);
		return companyToUpdate;
	}
	@GET
	@Path("/companies/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("id") long companyId, @Context HttpServletRequest request) throws CouponException {
		Company company = getAdmin(request).getCompany(companyId);
		return company;
		//wont show company
	}
	@GET
	@Path("/companies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanies(@Context HttpServletRequest request) throws CouponException {
		Company[] allCompanies = getAdmin(request).getAllCompanies().toArray(new Company[0]);
		return Response.status(Response.Status.OK).entity(allCompanies).build();
		
	}
	@POST
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer createCustomer(Customer customerToAdd, @Context HttpServletRequest request) throws CouponException {
		getAdmin(request).createCustomer(customerToAdd);
		return customerToAdd;
	}
	@DELETE
	@Path("/customers/{customerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer removeCustomer(@PathParam("customerId") long customerId , @Context HttpServletRequest request) throws CouponException {
		getAdmin(request).removeCustomer(customerId);
		return getAdmin(request).getCustomer(customerId);
	}
	@PUT
	@Path("/customers/{customerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer updateCustomer(@PathParam("customerId") long customerId, Customer UpdatedCustomer,@Context HttpServletRequest request) throws CouponException {
		getAdmin(request).updateCustomer(UpdatedCustomer);
		return UpdatedCustomer;
	}
	@GET
	@Path("/customers/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("customerId") long customerId , @Context HttpServletRequest request) throws CouponException {
		Customer customer = getAdmin(request).getCustomer(customerId);
		return customer;
	}
	@GET
	@Path("/customers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomers(@Context HttpServletRequest request) throws CouponException {
		Customer[] allCustomers = getAdmin(request).getAllCustomers().toArray(new Customer[0]);
		return Response.status(Response.Status.OK).entity(allCustomers).build();
	}
}
