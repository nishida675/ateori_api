package com.example.demo.app.LoginLogoutUser;

import javax.validation.constraints.Size;

import org.springframework.lang.NonNull;

public class userCreateForm {
	public userCreateForm() {}
	public userCreateForm(String name, String pass) {
		super();
		this.name = name;
		this.pass = pass;
	
	}

	@Size(min = 1, max = 20, message="Please input 20 characters or less")
    private String name;
	
	@NonNull
    private String pass;

    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	
}    
