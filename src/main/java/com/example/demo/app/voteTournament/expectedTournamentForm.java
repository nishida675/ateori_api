package com.example.demo.app.voteTournament;

import org.springframework.lang.NonNull;

public class expectedTournamentForm {
	public expectedTournamentForm() {
	}

	public expectedTournamentForm(int userId, int tournamentId, int classId1, int classId2, String number1, String number2, String number3,
			String number4, String number5, String number6) {
		super();
		this.userId = userId;
		this.tournamentId = tournamentId;
		this.classId1 = classId1;
		this.classId2 = classId2;
		this.number1 = number1;
		this.number2 = number2;
		this.number3 = number3;
		this.number4 = number4;
		this.number5 = number5;
		this.number6 = number6;

	}
	

	@NonNull
    private int userId;
	
	@NonNull
	private int tournamentId;
	
	@NonNull
	private int classId1;
	
	@NonNull
	private int classId2;
	
	private String number1;
	private String number2;
	private String number3;
	private String number4;
	private String number5;
	private String number6;
	
	// userId のゲッター
    public int getUserId() {
        return userId;
    }

    // userId のセッター
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // tournamentId のゲッター
    public int getTournamentId() {
        return tournamentId;
    }

    // tournamentId のセッター
    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }
    
    public int getClassId1() {
        return classId1;
    }

    public void setClassId1(int classId1) {
        this.classId1 = classId1;
    }

    public int getClassId2() {
        return classId2;
    }

    public void setClassId2(int classId2) {
        this.classId2 = classId2;
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
	
	public String getNumber4() {
		return number4;
	}

	public void setNumber4(String number4) {
		this.number4 = number4;
	}
	
	public String getNumber5() {
		return number5;
	}

	public void setNumber5(String number5) {
		this.number5 = number5;
	}
	
	public String getNumber6() {
		return number6;
	}

	public void setNumber6(String number6) {
		this.number6 = number6;
	}

}
