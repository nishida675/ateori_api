package com.example.demo.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ClassDB;
import com.example.demo.entity.ParticipantDB;
import com.example.demo.entity.ResultDB;
import com.example.demo.entity.TournamentDB;
import com.example.demo.repository.TournamentDAO;
import com.example.demo.scraping.JOYscraping;
import com.example.demo.scraping.LapCenterscraping;

@Service
public class TournamentServiceImpl implements TournamentService {

	private final TournamentDAO dao;
	private final JOYscraping joy;
	private final LapCenterscraping LCS;

	public TournamentServiceImpl(TournamentDAO dao, JOYscraping joy, LapCenterscraping LCS) {
		this.dao = dao;
		this.joy = joy;
		this.LCS = LCS;
	}

	@Override
	public List<TournamentDB> getTournament() {
		List<TournamentDB> list = new ArrayList<TournamentDB>();
		//大会名をスクレイピング
		try {
			list = joy.getJOY();
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//大会をDBに記録
		if (list != null) {
			dao.innsertTournament(list);
		}
		//大会を取得する
		list = dao.getAllTournament();

		return list;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getParticipant(int userId, int tournamentId, String className1,
			String className2) {
		List<ParticipantDB> list = new ArrayList<ParticipantDB>();
		String name = dao.getTournament(tournamentId);
		//大会の参加者をスクレイピング
		list = joy.getParticipant(tournamentId, name, className1, className2);
		//DBに参加者を入れる
		if (list != null) {
			dao.innsertParticipant(list);
		}
		//DBから参加者を取得する
		Map<String, List<Map<String, Object>>> classDetailsMap = dao.getClassDetailsByTournament(userId, tournamentId,
				className1, className2);
		return classDetailsMap;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getParticipant(int userId, int tournamentId, String className1) {
		List<ParticipantDB> list = new ArrayList<ParticipantDB>();
		String name = dao.getTournament(tournamentId);
		//大会の参加者をスクレイピング
		list = joy.getParticipant(tournamentId, name, className1);
		//DBに参加者を入れる
		if (list != null) {
			dao.innsertParticipant(list);
		}
		//DBから参加者を取得する
		Map<String, List<Map<String, Object>>> classDetailsMap = dao.getClassDetailsByTournament(userId, tournamentId,
				className1);
		return classDetailsMap;
	}

	@Override
	public List<ClassDB> getClassName(int tournamentId) {
		List<ClassDB> list = new ArrayList<ClassDB>();
		String className = dao.getTournament(tournamentId);
		list = joy.getJOYClass(className);
		//クラスをDBに入れる
		if (list != null) {
			dao.innsertClassName(tournamentId, list);
		}
		//DBからクラスを取得する
		list = dao.getClassName(tournamentId);
		return list;
	}

	@Override
	public boolean expectedTournament(int userId, int tournamentId, int classId1, int classId2, int number1,
			int number2, int number3,
			int number4, int number5, int number6) {
		boolean check = false;
		check = dao.expectedTournament(userId, tournamentId, classId1, number1, number2, number3);
		if (check) {
			check = dao.expectedTournament(userId, tournamentId, classId2, number4, number5, number6);
		}
		return check;
	}

	//全体予想順位取得
	@Override
	public Map<String, List<Map<String, Object>>> getPreResult(int tournamentID, String className1, String className2) {
		Map<String, List<Map<String, Object>>> preResult = new HashMap<>();

		// クラス1の事前結果の取得
		int classId1 = dao.getClassId(tournamentID, className1);
		List<Map<String, String>> classDetailsMap1 = dao.getPreNumbers(tournamentID, classId1);
		Map<String, List<Map<String, Object>>> numbers1 = convertToNumberMap(classDetailsMap1);
		preResult.putAll(numbers1);

		// クラス2の事前結果の取得
		int classId2 = dao.getClassId(tournamentID, className2);
		List<Map<String, String>> classDetailsMap2 = dao.getPreNumbers(tournamentID, classId2);
		Map<String, List<Map<String, Object>>> numbers2 = convertToNumberMap(classDetailsMap2);
		preResult.putAll(numbers2);

		return preResult;
	}

	private Map<String, List<Map<String, Object>>> convertToNumberMap(List<Map<String, String>> classDetailsMap) {
	    Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();

	    for (Map<String, String> classDetails : classDetailsMap) {
	        String className = classDetails.get("class_name");
	        String participantName = classDetails.get("participant_name");
	        String affiliation = classDetails.get("affiliation");
	        String number = classDetails.get("number");

	        if (className != null) {
	            // クラス名をキーとして、リストを取得する
	            List<Map<String, Object>> participantList = resultMap.getOrDefault(className, new ArrayList<>());
	            // 参加者情報をマップに追加する
	            Map<String, Object> participantInfo = new HashMap<>();
	            participantInfo.put("participantName", participantName);
	            participantInfo.put("affiliation", affiliation);
	            participantInfo.put("number", number);
	            participantList.add(participantInfo);
	            // クラス名をキーとして、リストを resultMap に設定する
	            resultMap.put(className, participantList);
	        }
	    }

	    return resultMap;
	}

	@Override
	public int checkResultDate(int tournamentId) {
		return dao.getEventDate(tournamentId);
	}

	@Override
	public Map<String, List<Map<String, Object>>> getResult(int userId, int tournamentID, String className1,
			String className2) {
		int checkResult = 0;
		List<ResultDB> list1 = new ArrayList<ResultDB>();
		List<ResultDB> list2 = new ArrayList<ResultDB>();
		Map<String, List<Map<String, Object>>> Result = new HashMap<>();
		int classId1 = dao.getClassId(tournamentID, className1);
		int classId2 = dao.getClassId(tournamentID, className2);
		checkResult = dao.getResultCount(tournamentID);
		if (checkResult == 0) {
			//レース結果をスクレイピングして返す
			String tournamentName = dao.getTournament(tournamentID);
			try {
				list1 = LCS.getLapCenter(tournamentName, className1);
				list2 = LCS.getLapCenter(tournamentName, className2);
			} catch (ParseException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			//DBに結果をinnsert
			if (list1 != null && list2 != null) {
				dao.insertResult(tournamentID, classId1, list1);
				dao.insertResult(tournamentID, classId2, list2);
				//tournamentTableのcheckResultを1にする
				dao.updateResultCount(tournamentID);
			
			//DBから結果を取得
			Map<String, List<Map<String, Object>>> resultClass1 = dao.getResult(tournamentID, classId1, className1);
			Map<String, List<Map<String, Object>>> resultClass2 = dao.getResult(tournamentID, classId2, className2);

			Result.putAll(resultClass1);
			Result.putAll(resultClass2);
			//予想したものがあれば何個あってるか調べる

			int resultCount1 = dao.resultVerification(userId, tournamentID, classId1);
			int resultCount2 = dao.resultVerification(userId, tournamentID, classId2);
			int resultCount = resultCount1 + resultCount2;
			//あってた数DBに入れる　
			int addition1 = dao.checkAddition(userId, tournamentID, classId1);
			if(addition1  == 0) {
			dao.updateCount(userId, resultCount1);
			dao.uppdateAddition(userId, tournamentID, classId1);
			}
			int addition2 = dao.checkAddition(userId, tournamentID, classId2);
			if(addition2  == 0) {
			dao.updateCount(userId, resultCount2);
			dao.uppdateAddition(userId, tournamentID, classId2);
			}
			//ユーザーが予想したものを取得する
			String keyName1 = "expected1";
			String keyName2 = "expected2";
			Map<String, List<Map<String, Object>>> expectParticipant1 = dao.getExpectedParticipant(userId, tournamentID, classId1, keyName1);
			Map<String, List<Map<String, Object>>> expectParticipant2 = dao.getExpectedParticipant(userId, tournamentID, classId2, keyName2);
			Result.putAll(expectParticipant1);
			Result.putAll(expectParticipant2);
			//全ての結果をMapにまとめる
			 List<Map<String, Object>> countList = new ArrayList<>();
		        Map<String, Object> countMap = new HashMap<>();
		        countMap.put("count", resultCount);
		        countList.add(countMap);
		        Result.put("count", countList);
			}else {
				Result = null;
			}
		} else if (checkResult == 1) {
			//レース結果をスクレイピングしているのでDBから結果を取得して返す
			//DBから結果を取得
			Map<String, List<Map<String, Object>>> resultClass1 = dao.getResult(tournamentID, classId1, className1);
			Map<String, List<Map<String, Object>>> resultClass2 = dao.getResult(tournamentID, classId2, className2);

			Result.putAll(resultClass1);
			Result.putAll(resultClass2);
			//予想したものがあれば何個あってるか調べる

			int resultCount1 = dao.resultVerification(userId, tournamentID, classId1);
			int resultCount2 = dao.resultVerification(userId, tournamentID, classId2);
			int resultCount = resultCount1 + resultCount2;
			//あってた数DBに入れる　
			int addition1 = dao.checkAddition(userId, tournamentID, classId1);
			if(addition1  == 0) {
			dao.updateCount(userId, resultCount1);
			dao.uppdateAddition(userId, tournamentID, classId1);
			}
			int addition2 = dao.checkAddition(userId, tournamentID, classId2);
			if(addition2  == 0) {
			dao.updateCount(userId, resultCount2);
			dao.uppdateAddition(userId, tournamentID, classId2);
			}
			//ユーザーが予想したものを取得する
			String keyName1 = "expected1";
			String keyName2 = "expected2";
			Map<String, List<Map<String, Object>>> expectParticipant1 = dao.getExpectedParticipant(userId, tournamentID, classId1, keyName1);
			Map<String, List<Map<String, Object>>> expectParticipant2 = dao.getExpectedParticipant(userId, tournamentID, classId2, keyName2);
			Result.putAll(expectParticipant1);
			Result.putAll(expectParticipant2);
			//全ての結果をMapにまとめる
			 List<Map<String, Object>> countList = new ArrayList<>();
		        Map<String, Object> countMap = new HashMap<>();
		        countMap.put("count", resultCount);
		        countList.add(countMap);
		        Result.put("count", countList);
		}
		return Result;
	}

}
