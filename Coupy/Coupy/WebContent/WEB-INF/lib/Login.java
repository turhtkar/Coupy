package LoginManager;


import DAO.CompaniesDAO;
import DAO.CompaniesDBDAO;
import DAO.CouponsDAO;
import DAO.CouponsDBDAO;
import DAO.CustomersDAO;
import DAO.CustomersDBDAO;
import DailyJob.CouponExpirationJob;
import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CouponClientFacade;
import Facade.CustomerFacade;
import JavaBeans.Company;
import JavaBeans.Customer;
import connection_pool.ConnectionPool;
import exception.CouponException;

public class Login {

	private AdminFacade adminFacade;
	private CustomerFacade customerFacade;
	private CompanyFacade companyFacade;

	private CompaniesDAO companyDAO;
	private CustomersDAO customerDao;
	private CouponsDAO couponDao;

	public void setCompanyDao(CompaniesDAO companyDao) {
		this.companyDAO = companyDao;
	}

	public void setCustomerDao(CustomersDAO customerDao) {
		this.customerDao = customerDao;
	}

	public void setCouponDao(CouponsDAO couponDao) {
		this.couponDao = couponDao;
	}

	private Company company;
	private Customer customer;

	private CouponExpirationJob couponExpirationJob;

	private Thread dailyThread;

	private static Login instance;

	public static Login getInstance() throws CouponException {
		if (instance == null) {
			instance = new Login();
		}
		return instance;
	}

	public CouponClientFacade login(String email, String password, ClientType clientType) throws CouponException {
		if (clientType == ClientType.ADMIN) {
			adminFacade = (AdminFacade) new AdminFacade(companyDAO, customerDao, couponDao).login(email, password,
					clientType);
			return adminFacade;
		} else if (clientType == ClientType.COMPANY) {
			companyFacade = (CompanyFacade) new CompanyFacade(companyDAO, customerDao, couponDao, company)
					.login(email, password, clientType);
			return companyFacade;
		} else if (clientType == ClientType.CUSTOMER) {
			customerFacade = (CustomerFacade) new CustomerFacade(customerDao, couponDao, customer)
					.login(email, password, clientType);
			return customerFacade;
		}
		return null;
	}

	private Login() throws CouponException {
		companyDAO = new CompaniesDBDAO();
		customerDao = new CustomersDBDAO();
		couponDao = new CouponsDBDAO();

		adminFacade = new AdminFacade(companyDAO, customerDao, couponDao);
		customerFacade = new CustomerFacade(customerDao, couponDao, customer);
		companyFacade = new CompanyFacade(companyDAO, customerDao, couponDao, company);

		couponExpirationJob = new CouponExpirationJob(companyDAO, customerDao, couponDao, false);
		dailyThread = new Thread(couponExpirationJob);
		dailyThread.start();
	}

	public void shutDown() throws CouponException {
		ConnectionPool.getInstance().closeAllConnections();
		couponExpirationJob.endTask();
	}
}
