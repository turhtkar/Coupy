package com.coupy.api.filters;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;


import Facade.AdminFacade;
import Facade.CompanyFacade;
import Facade.CustomerFacade;

/**
 * Servlet Filter implementation class TheCoupySelectors
 */
@WebFilter("/api/*")
public class TheCoupySelectors implements Filter {

    /**
     * Default constructor. 
     */
    public TheCoupySelectors() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getRequestURI();
        javax.servlet.http.Cookie[] ck=req.getCookies();
        HttpSession session = req.getSession(false);
        boolean loginExpired = session == null; //&& (session.getAttribute("admin") != null || session.getAttribute("company") != null || session.getAttribute("customer") != null);
       
       // if(loginExpired) {
        	//First time check
        	//=================
    /*    	if(ck != null) {
        		for (javax.servlet.http.Cookie cookie : ck) {
        			if (cookie.getName().equals("lastVisit")) {
        				break;
        			}
        		}
        	} else {
        		session = req.getSession(true);
        		javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("lastvisit", session.getId());
        		cookie.setPath(url);
        		cookie.setMaxAge(60 * 60 * 24 * 7);
        		res.addCookie(cookie);
            	chain.doFilter(request, response);
            	return;
        //	}else if() {
        		
      //  	} 
 	
        } */
	
        if(url.contains("/api/admin/login") || url.contains("/api/company/login")
				|| url.contains("/api/customer/login")) {
        	chain.doFilter(request, response);
        	return;
        } else if(loginExpired) {
        	res.getWriter().println("{\"error\":\"you are not logged in1\"}");
			res.setContentType(MediaType.APPLICATION_JSON);
			res.setStatus(500);
			return;
        } else if (url.contains("/api/admin")) {
        	AdminFacade adminFacade = ((AdminFacade) session.getAttribute("admin"));
        	if (adminFacade != null) {
        		chain.doFilter(request, response);
				return;
        	}else {
            	res.getWriter().println("{\"error\":\"you are not logged in as Admin\"}");
            	System.out.println("else");
    			res.setContentType(MediaType.APPLICATION_JSON);
    			res.setStatus(500);
    			return;
            }
        }else if(url.contains("api/company")) {
        	CompanyFacade companyFacade = ((CompanyFacade) session.getAttribute("company"));
        	if (companyFacade != null) {
				chain.doFilter(request, response);
				return;
        	}else {
            	res.getWriter().println("{\"error\":\"you are not logged in as Company\"}");
            	System.out.println("else");
    			res.setContentType(MediaType.APPLICATION_JSON);
    			res.setStatus(500);
    			return;
            }
        }else if(url.contains("/api/customer")) {
        	CustomerFacade customerFacade = ((CustomerFacade) session.getAttribute("customer"));
        	if (customerFacade != null) {
				chain.doFilter(request, response);
				return;
        	}else {
            	res.getWriter().println("{\"error\":\"you are not logged in as Customer\"}");
            	System.out.println("else");
    			res.setContentType(MediaType.APPLICATION_JSON);
    			res.setStatus(500);
    			return;
            }
        }else {
        	res.getWriter().println("{\"error\":\"you are not logged in\"}");
        	System.out.println("else");
			res.setContentType(MediaType.APPLICATION_JSON);
			res.setStatus(500);
			return;
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
