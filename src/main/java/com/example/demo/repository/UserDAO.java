package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.List;

import com.example.demo.entity.UserDB;

public interface UserDAO {
	
	String selectPass(String name);
	
	int getId(String name);
	
	boolean insertUser(String name, String pass, Timestamp date);
	
	List<UserDB> getAll(int ID);
	
	List<UserDB> getRanking();
}