package com.coupy.api.filters;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class EnableCorsFilter implements ContainerResponseFilter {
	
	@Override
    public void filter(ContainerRequestContext request,
            ContainerResponseContext response) throws IOException {
        response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");// or "*" or /* ?
        response.getHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, X-PINGOTHER");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        
        System.out.println("hi from CORS");
    }

	
}

