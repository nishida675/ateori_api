package com.example.demo.entity;


public class ClassDB {
	
	private int classId;
	private int tournamentId;
	private String className;
	
	public ClassDB () {}
	
	
	public ClassDB(int classId, int tournamentId, String className) {
		super();
		this.setClassId(classId);
		this.setTournamentId(tournamentId);
		this.setClassName(className);
	}


	public int getTournamentId() {
		return tournamentId;
	}


	public void setTournamentId(int tournamentId) {
		this.tournamentId = tournamentId;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public int getClassId() {
		return classId;
	}


	public void setClassId(int classId) {
		this.classId = classId;
	}

}
