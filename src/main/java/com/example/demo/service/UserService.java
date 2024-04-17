package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.UserDB;

public interface UserService {
	
	boolean userCreate(String name, String pass);
	
	List<UserDB> authLogin(String name, String pass);
	
	List<UserDB> getRanking();

}
