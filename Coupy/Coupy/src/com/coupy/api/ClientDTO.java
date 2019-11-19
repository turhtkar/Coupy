package com.coupy.api;

import java.io.Serializable;

import Facade.ClientType;

public class ClientDTO implements Serializable {

	private static final long serialVersionUID = 3951473867409396019L;
	
	private String email;
	private String password;
	private ClientType clientType;
	
	public ClientDTO() {
	}

	public ClientDTO(String email, String password, ClientType clientType) {
		super();
		this.email = email;
		this.password = password;
		this.clientType = clientType;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return "ClientDTO [email=" + email + "password=" + password + ", clientType=" + clientType + "]";
	}
	
	
	

}
