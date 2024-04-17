package com.example.demo.entity;

public class ExpectedDB {
	
	private int expectedId;
	private int userId;
	private int tournamentId;
	private int classId;
	private String number1;
	private String number2;
	private String number3;
	
	public ExpectedDB() {}
	
	public ExpectedDB(int expectedId, int userId, int tournamentId, int classId, String number1, String number2, String number3) {
		super();
		this.expectedId = expectedId;
		this.userId = userId;
		this.tournamentId = tournamentId;
		this.classId = classId;
		this.number1 = number1;
		this.number2 = number2;
		this.number3 = number3;
	}

	public int getExpectedId() {
		return expectedId;
	}

	public void setExpectedId(int expectedId) {
		this.expectedId = expectedId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTournamentId() {
		return tournamentId;
	}

	public void setTournamentId(int tournamentId) {
		this.tournamentId = tournamentId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getNumber1() {
		return number1;
	}

	public void setNumber1(String number1) {
		this.number1 = number1;
	}

	public String getNumber2() {
		return number2;
	}

	public void setNumber2(String number2) {
		this.number2 = number2;
	}

	public String getNumber3() {
		return number3;
	}

	public void setNumber3(String number3) {
		this.number3 = number3;
	}

}
