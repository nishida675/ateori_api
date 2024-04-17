package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.ClassDB;
import com.example.demo.entity.ParticipantDB;
import com.example.demo.entity.ResultDB;
import com.example.demo.entity.TournamentDB;

public interface TournamentDAO {
	
  String getTournament(int tournamentID);
  
  void innsertTournament(List<TournamentDB> list);
  
  int getTournamentId(String name);
  
  List<TournamentDB> getAllTournament();
  
  List<ClassDB> getClassName(int tournamentID);
  
  void innsertClassName(int tournamentID, List<ClassDB> list);
  
  void innsertParticipant(List<ParticipantDB> list);
  
  public Map<String, List<Map<String, Object>>> getClassDetailsByTournament(int userId, int tournamentID, String Name1, String Name2);
  
  public Map<String, List<Map<String, Object>>> getClassDetailsByTournament(int userId, int tournamentID, String Name1);
  
  boolean expectedTournament(int userId, int tournamentId, int classId, int number1, int number2, int number3);
  
  public int getClassId(int tournamentID,String name);
  
  public List<Map<String, String>> getPreNumbers(int tournamentId, int classId) ;
  
  public  int getEventDate(int tournamentId);
  
  public int getResultCount(int tournamentId);
  
  void insertResult(int tournamentId, int classId1, List<ResultDB> list);

  public void updateResultCount(int tournamentId);
  
  public Map<String, List<Map<String, Object>>> getResult(int tournamentId, int classId, String className);
  
  public int resultVerification(int userId, int tournamentId, int classId);
  
  void updateCount(int userId, int countResult);
  
  public int checkAddition(int userId, int tournamentId, int classId);
  
  public void uppdateAddition(int userId, int tournamentId, int classId);
  
  public Map<String, List<Map<String, Object>>> getExpectedParticipant(int userId, int tournamentId, int classId, String keyName);
  
}
