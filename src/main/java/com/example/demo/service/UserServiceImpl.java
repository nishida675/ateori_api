package com.example.demo.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserDB;
import com.example.demo.repository.UserDAO;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserDAO dao;
	
	public UserServiceImpl(UserDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<UserDB> authLogin(String name, String pass) {
		String Pass = dao.selectPass(name);
		List<UserDB> list = null ;
		if(Pass.equals(pass)) {
		int id = dao.getId(name);
		list = dao.getAll(id);
		}
		if(list.isEmpty()) {
			throw new InquiryNotFoundException("No inquiry");
		}
		return list;
	}

	@Override
	public boolean userCreate(String name, String pass) {
		// 現在の日付を取得
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
		boolean check = dao.insertUser(name, pass, timestamp);
		return check;
		
	}

	@Override
	public List<UserDB> getRanking() {
		List<UserDB> list = dao.getRanking();
		return list;
	}

}
