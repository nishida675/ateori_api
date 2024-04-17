package com.example.demo.entity;

public class ParticipantDB {

	private int participantId;
	private int tournamentId;
	private String className;
	private String participantName;
	private String affiliation;
	
	public ParticipantDB() {}
	
	public ParticipantDB(int participantId, int tournamentId, String className, String participantName, String affiliation) {
		super();
		this.participantId = participantId;
		this.tournamentId = tournamentId;
		this.className = className;
		this.participantName = participantName;
		this.setAffiliation(affiliation);
	}
	
	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
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

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	
}
