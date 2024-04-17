package com.example.demo.entity;

import java.sql.Timestamp;

public class UserDB {
	private int id;
	private String  name;
	private String pass;
	private int count;
	private Timestamp Date;
	
	
	private int rankNumber;
	
	public UserDB() {}
	
	public UserDB(int id, String name, String pass, int count, Timestamp date, int rank) {
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.count = count;
		this.setDate(date);
		this.setRank(rankNumber);
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Timestamp getDate() {
		return Date;
	}

	public void setDate(Timestamp date) {
		Date = date;
	}

	public int getRank() {
		return rankNumber;
	}

	public void setRank(int rank) {
		this.rankNumber = rank;
	}

}
