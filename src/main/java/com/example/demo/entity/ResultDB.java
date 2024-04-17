package com.example.demo.entity;

public class ResultDB {
	private int resultId;
	private int tournamentId;
	private int classId;
	private String result1Name;
	private String result1Affiliation;
	private String result2Name;
	private String result2Affiliation;
	private String result3Name;
	private String result3Affiliation;
	
	public ResultDB() {}
	
	public ResultDB(int resultId, int tournamentId, int classId, String result1Name, String result1Affiliation, String result2Name, String result2Affiliation, String result3Name, String result3Affiliation) {
		super();
		this.setResultId(resultId);
		this.setTournamentId(tournamentId);
		this.setClassId(classId);
		this.setResult1Name(result1Name);
		this.setResult1Affiliation(result1Affiliation);
		this.setResult2Name(result2Name);
		this.setResult2Affiliation(result2Affiliation);
		this.setResult3Name(result3Name);
		this.setResult3Affiliation(result3Affiliation);
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
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

	public String getResult1Name() {
		return result1Name;
	}

	public void setResult1Name(String result1Name) {
		this.result1Name = result1Name;
	}

	public String getResult1Affiliation() {
		return result1Affiliation;
	}

	public void setResult1Affiliation(String result1Affiliation) {
		this.result1Affiliation = result1Affiliation;
	}

	public String getResult2Name() {
		return result2Name;
	}

	public void setResult2Name(String result2Name) {
		this.result2Name = result2Name;
	}

	public String getResult2Affiliation() {
		return result2Affiliation;
	}

	public void setResult2Affiliation(String result2Affiliation) {
		this.result2Affiliation = result2Affiliation;
	}

	public String getResult3Name() {
		return result3Name;
	}

	public void setResult3Name(String result3Name) {
		this.result3Name = result3Name;
	}

	public String getResult3Affiliation() {
		return result3Affiliation;
	}

	public void setResult3Affiliation(String result3Affiliation) {
		this.result3Affiliation = result3Affiliation;
	}

}
