package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.ClassDB;
import com.example.demo.entity.TournamentDB;

public interface TournamentService {

	List<TournamentDB> getTournament();
	
	Map<String, List<Map<String, Object>>> getParticipant(int userId, int tournamentID, String className1,String className2);
	
	public Map<String, List<Map<String, Object>>> getParticipant(int userId, int tournamentId, String className1);
	
	List<ClassDB> getClassName(int tournamentId);
	
	boolean expectedTournament(int userId, int tournamentId, int classId1, int classId2, int number1, int number2, int number3, int number4, int number5, int number6);
	
	public Map<String, List<Map<String, Object>>> getPreResult(int tournamentID, String className1, String className2);
	
	public int checkResultDate(int tournamentId);
	
	public Map<String, List<Map<String, Object>>> getResult(int userId, int tournamentID, String className1, String className2);
}
