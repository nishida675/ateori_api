package com.example.demo.entity;

import java.sql.Timestamp;

public class TournamentDB {

	private int tournamentId;
	private String tournamentName;
	private Timestamp eventDate;
	private String eventDate2;
	
	
	public TournamentDB() {}
	
	public TournamentDB(int tournamentId, String tournamentName, String className, Timestamp eventDate, String eventDate2) {
		super();
		this.tournamentId = tournamentId;
		this.tournamentName = tournamentName;
		this.eventDate = eventDate;
		this.eventDate2 = eventDate2;
	}
	
	public int getTournamentId() {
		return tournamentId;
	}

	public void setTournamentId(int tournamentId) {
		this.tournamentId = tournamentId;
	}

	public String getTournamentName() {
		return tournamentName;
	}

	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}

	public Timestamp getEventDate() {
		return eventDate;
	}

	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventDate2() {
		return eventDate2;
	}

	public void setEventDate2(String eventDate2) {
		this.eventDate2 = eventDate2;
	}
}
