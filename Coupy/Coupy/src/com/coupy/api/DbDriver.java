package com.coupy.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import exception.CouponException;

@ApplicationPath("/api")

public class DbDriver extends Application {

	public DbDriver() throws CouponException {

		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); // throw new CouponException(e + "Couldn't get connection to database");
		}

	}

}
