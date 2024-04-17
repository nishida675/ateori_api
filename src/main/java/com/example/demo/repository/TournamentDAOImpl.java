package com.example.demo.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ClassDB;
import com.example.demo.entity.ParticipantDB;
import com.example.demo.entity.ResultDB;
import com.example.demo.entity.TournamentDB;

@Repository
public class TournamentDAOImpl implements TournamentDAO {

	private final JdbcTemplate jdbcTemplate;

	public TournamentDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String getTournament(int tournamentID) {
		String className = null;
		String sql = "SELECT tournament_name FROM tournament_table WHERE tournament_id = ?";
		className = jdbcTemplate.queryForObject(sql, String.class, tournamentID);
		return className;
	}

	@Override
	public void innsertTournament(List<TournamentDB> list) {
		for (TournamentDB tournament : list) {
			// 名前が同じレコードが存在するか確認
			if (existsTournament(tournament.getTournamentName())) {
				// 同名のトーナメントが存在する場合はアップサート（更新）
				updateTournament(tournament);
			} else {
				// 同名のトーナメントが存在しない場合は新規にインサート
				insertNewTournament(tournament);
			}
		}
	}

	private boolean existsTournament(String tournamentName) {
		String sql = "SELECT COUNT(*) FROM tournament_table WHERE tournament_name = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, tournamentName) > 0;
	}

	private void updateTournament(TournamentDB tournament) {
		String sql = "UPDATE tournament_table SET event_date = ? WHERE tournament_name = ?";
		jdbcTemplate.update(sql, tournament.getEventDate(), tournament.getTournamentName());
	}

	private void insertNewTournament(TournamentDB tournament) {
		String sql = "INSERT INTO tournament_table (tournament_name, event_date) VALUES (?, ?)";
		jdbcTemplate.update(sql, tournament.getTournamentName(), tournament.getEventDate());
	}

	@Override
	public int getTournamentId(String name) {
		int ID = 0;
		String sql = "SELECT tournament_id FROM tournament_table WHERE tournament_name = ?";

		try {
			ID = jdbcTemplate.queryForObject(sql, int.class, name);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからなかった場合の処理（例: ログメッセージの出力、例外のスロー）
			// 結果が見つからなかった場合の特別な値を返すことを検討すると良いでしょう。
		}

		return ID;
	}

	@Override
	public int getClassId(int tournamentID, String name) {
		int ID = 0;
		String sql = "SELECT class_id FROM class_table WHERE tournament_id = ? AND class_name = ?";

		try {
			ID = jdbcTemplate.queryForObject(sql, int.class, tournamentID, name);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからなかった場合の処理（例: ログメッセージの出力、例外のスロー）
			// 結果が見つからなかった場合の特別な値を返すことを検討すると良いでしょう。
		}

		return ID;
	}

	public Integer retrieveClassId(int tournamentId, String className) {
		System.out.println("トナメID" + tournamentId);
		System.out.println("className" + className);
		String sql = "SELECT class_id FROM class_table WHERE tournament_id = ? AND class_name = ?";
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class, tournamentId, className);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<TournamentDB> getAllTournament() {
		String sql = "SELECT tournament_id, tournament_name, event_date FROM tournament_table WHERE event_date >= DATE_SUB(NOW(), INTERVAL 2 DAY) AND event_date <= DATE_ADD(NOW(), INTERVAL 1 WEEK) ORDER BY event_date ASC";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		List<TournamentDB> list = new ArrayList<>();
		for (Map<String, Object> result : resultList) {
			TournamentDB tournamentDB = new TournamentDB();
			tournamentDB.setTournamentId((int) result.get("tournament_id"));
			tournamentDB.setTournamentName((String) result.get("tournament_name"));
			LocalDateTime localDateTime = (LocalDateTime) result.get("event_date");
			String eventDate = convertToLocalDate(localDateTime);
			tournamentDB.setEventDate2(eventDate);
			list.add(tournamentDB);
		}
		return list;
	}

	// 日付の変換
	private String convertToLocalDate(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = localDateTime.format(formatter);
		return formattedDate;
	}

	@Override
	public List<ClassDB> getClassName(int tournamentID) {
		String sql = "SELECT class_id, tournament_id, class_name FROM class_table WHERE tournament_id = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, tournamentID);
		List<ClassDB> list = new ArrayList<>();

		for (Map<String, Object> result : resultList) {
			ClassDB classDB = new ClassDB();
			classDB.setClassId((int) result.get("class_id"));
			classDB.setTournamentId((int) result.get("tournament_id"));
			classDB.setClassName((String) result.get("class_name"));
			// 他のClassDBの設定（必要に応じて）

			list.add(classDB);
		}

		return list;
	}

	@Override
	public void innsertClassName(int tournamentID, List<ClassDB> list) {
		for (ClassDB className : list) {
			// 名前が同じレコードが存在するか確認
			if (existsClassName(className.getClassName(), tournamentID)) {
				// 同名のトーナメントが存在する場合はアップサート（更新）
				//updateClassName(tournamentID, className);
			} else {
				// 同名のトーナメントが存在しない場合は新規にインサート
				insertNewClassName(tournamentID, className);
			}
		}

	}

	private boolean existsClassName(String ClassName, int tournamentID) {
		String sql = "SELECT COUNT(*) FROM class_table WHERE tournament_id = ? AND class_name = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, tournamentID, ClassName) > 0;
	}

	/*private void updateClassName(int tournamentID, ClassDB className) {//
	    String sql = "UPDATE class_table SET class_name = ? WHERE tournament_id = ?";
	    jdbcTemplate.update(sql, className.getClassName(), tournamentID);
	} */

	private void insertNewClassName(int tournamentID, ClassDB className) {
		String sql = "INSERT INTO class_table (tournament_id, class_name) VALUES (?, ?)";
		jdbcTemplate.update(sql, tournamentID, className.getClassName());
	}

	@Override
	public void innsertParticipant(List<ParticipantDB> list) {
		for (ParticipantDB participant : list) {
			// 名前が同じレコードが存在するか確認
			if (existsParticipant(participant.getParticipantName(), participant.getTournamentId(),
					participant.getClassName())) {
				// 同名の参加者が存在する場合はアップサート（更新）
				//  updateParticipant(participant);
			} else {
				// 同名の参加者が存在しない場合は新規にインサート
				insertNewParticipant(participant);
			}
		}

	}

	private boolean existsParticipant(String participantName, int tournamentID, String className) {
		String sql = "SELECT COUNT(*) FROM participant_table WHERE participant_name = ? AND tournament_id = ? AND class_name = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, participantName, tournamentID, className) > 0;
	}

	/*  private void updateParticipant(ParticipantDB tournament) {
	    String sql = "UPDATE participant_table SET affiliation = ? WHERE participant_name = ?";
	    jdbcTemplate.update(sql, tournament.getAffiliation(), tournament.getParticipantName());
	} */

	private void insertNewParticipant(ParticipantDB participant) {
		String sql = "INSERT INTO participant_table (tournament_id, class_name, participant_name, affiliation) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, participant.getTournamentId(), participant.getClassName(),
				participant.getParticipantName(), participant.getAffiliation());
	}

	@Override
	public Map<String, List<Map<String, Object>>> getClassDetailsByTournament(int userId, int tournamentID,
			String className1,
			String className2) {
		Map<String, List<Map<String, Object>>> classDetailsMap = new HashMap<>();
		int classId1 = retrieveClassId(tournamentID, className1);
		int classId2 = retrieveClassId(tournamentID, className2);
		List<Map<String, Integer>> expectedNumbersList = getExpectedNumbers(userId, tournamentID, classId1);
		List<Map<String, Integer>> expectedNumbersList2 = getExpectedNumbers(userId, tournamentID, classId2);

		for (Map<String, Integer> expectedNumberMap : expectedNumbersList) {
			Integer number_1 = expectedNumberMap.get("number_1");
			Integer number_2 = expectedNumberMap.get("number_2");
			Integer number_3 = expectedNumberMap.get("number_3");
			String expectedKey = "expected1"; // ユニークなキーを定義

			String sql = "SELECT participant_id, class_name, participant_name, affiliation FROM participant_table WHERE participant_id IN (?, ?, ?)";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, number_1, number_2, number_3);

			for (Map<String, Object> result : resultList) {
				int participantId = (int) result.get("participant_id");
				String className = (String) result.get("class_name");
				String participantName = (String) result.get("participant_name");
				String affiliation = (String) result.get("affiliation");

				Map<String, Object> participantDetails = new HashMap<>();
				participantDetails.put("participantId", participantId);
				participantDetails.put("className", className);
				participantDetails.put("participantName", participantName);
				participantDetails.put("affiliation", affiliation);

				if (participantId == number_1) {
					participantDetails.put("number", 1);
				}
				if (participantId == number_2) {
					participantDetails.put("number", 2);
				}
				if (participantId == number_3) {
					participantDetails.put("number", 3);
				}

				classDetailsMap.computeIfAbsent(expectedKey, k -> new ArrayList<>()).add(participantDetails);
			}
		}

		for (Map<String, Integer> expectedNumberMap : expectedNumbersList2) {
			Integer number_1 = expectedNumberMap.get("number_1");
			Integer number_2 = expectedNumberMap.get("number_2");
			Integer number_3 = expectedNumberMap.get("number_3");
			String expectedKey = "expected2"; // ユニークなキーを定義

			String sql = "SELECT participant_id, class_name, participant_name, affiliation FROM participant_table WHERE participant_id IN (?, ?, ?)";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, number_1, number_2, number_3);

			for (Map<String, Object> result : resultList) {
				int participantId = (int) result.get("participant_id");
				String className = (String) result.get("class_name");
				String participantName = (String) result.get("participant_name");
				String affiliation = (String) result.get("affiliation");

				Map<String, Object> participantDetails = new HashMap<>();
				participantDetails.put("participantId", participantId);
				participantDetails.put("className", className);
				participantDetails.put("participantName", participantName);
				participantDetails.put("affiliation", affiliation);

				if (participantId == number_1) {
					participantDetails.put("number", 1);
				}
				if (participantId == number_2) {
					participantDetails.put("number", 2);
				}
				if (participantId == number_3) {
					participantDetails.put("number", 3);
				}

				classDetailsMap.computeIfAbsent(expectedKey, k -> new ArrayList<>()).add(participantDetails);
			}
		}

		String sql2 = "SELECT participant_id,class_name, participant_name, affiliation FROM participant_table WHERE tournament_id = ? AND class_name IN (?, ?)";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql2, tournamentID, className1, className2);

		for (Map<String, Object> result : resultList) {
			String className = (String) result.get("class_name");
			int participantId = (int) result.get("participant_id");
			String participantName = (String) result.get("participant_name");
			String affiliation = (String) result.get("affiliation");

			// クラスごとの情報を格納するリストを取得または作成
			List<Map<String, Object>> classList = classDetailsMap.computeIfAbsent(className, k -> new ArrayList<>());

			// クラスごとの情報をマップに格納
			Map<String, Object> participantDetails = new HashMap<>();
			participantDetails.put("participantId", participantId); // participantIdはint型のまま追加
			participantDetails.put("className", className);
			participantDetails.put("participantName", participantName);
			participantDetails.put("affiliation", affiliation);

			// マップをリストに追加
			classList.add(participantDetails);
		}

		Timestamp day = null;
		boolean check = true;
		LocalDate today = LocalDate.now();
		String sql3 = "SELECT event_date FROM tournament_table WHERE tournament_id = ?";
		try {
			day = jdbcTemplate.queryForObject(sql3, Timestamp.class, tournamentID);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからなかった場合の処理（例: ログメッセージの出力、例外のスロー）
			// 結果が見つからなかった場合の特別な値を返すことを検討すると良いでしょう。
		}
		if (day != null) {
			LocalDateTime eventDateTime = day.toLocalDateTime();
			LocalDate eventDate = eventDateTime.toLocalDate();
			System.out.println("イベントのひ" + eventDate);
			System.out.println("今日" + today);

			if (eventDate.isEqual(today) || eventDate.isBefore(today)) {
				// イベント日が今日の日付と同じか過去の場合の処理
				check = false;
			}
			System.out.println(check);
		}
		classDetailsMap.put("reregistration",
				Collections.singletonList(Collections.singletonMap("reregistration", check)));

		return classDetailsMap;
	}

	//classが一つの場合
	@Override
	public Map<String, List<Map<String, Object>>> getClassDetailsByTournament(int userId, int tournamentID,
			String className1) {
		Map<String, List<Map<String, Object>>> classDetailsMap = new HashMap<>();
		int classId1 = retrieveClassId(tournamentID, className1);
		List<Map<String, Integer>> expectedNumbersList = getExpectedNumbers(userId, tournamentID, classId1);

		for (Map<String, Integer> expectedNumberMap : expectedNumbersList) {
			Integer number_1 = expectedNumberMap.get("number_1");
			Integer number_2 = expectedNumberMap.get("number_2");
			Integer number_3 = expectedNumberMap.get("number_3");
			String expectedKey = "expected1"; // ユニークなキーを定義

			String sql = "SELECT participant_id, class_name, participant_name, affiliation FROM participant_table WHERE participant_id IN (?, ?, ?)";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, number_1, number_2, number_3);

			for (Map<String, Object> result : resultList) {
				int participantId = (int) result.get("participant_id");
				String className = (String) result.get("class_name");
				String participantName = (String) result.get("participant_name");
				String affiliation = (String) result.get("affiliation");

				Map<String, Object> participantDetails = new HashMap<>();
				participantDetails.put("participantId", participantId);
				participantDetails.put("className", className);
				participantDetails.put("participantName", participantName);
				participantDetails.put("affiliation", affiliation);

				if (participantId == number_1) {
					participantDetails.put("number", 1);
				}
				if (participantId == number_2) {
					participantDetails.put("number", 2);
				}
				if (participantId == number_3) {
					participantDetails.put("number", 3);
				}

				classDetailsMap.computeIfAbsent(expectedKey, k -> new ArrayList<>()).add(participantDetails);
			}
		}

		String sql2 = "SELECT participant_id,class_name, participant_name, affiliation FROM participant_table WHERE tournament_id = ? AND class_name IN (?)";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql2, tournamentID, className1);

		for (Map<String, Object> result : resultList) {
			String className = (String) result.get("class_name");
			int participantId = (int) result.get("participant_id");
			String participantName = (String) result.get("participant_name");
			String affiliation = (String) result.get("affiliation");

			// クラスごとの情報を格納するリストを取得または作成
			List<Map<String, Object>> classList = classDetailsMap.computeIfAbsent(className, k -> new ArrayList<>());

			// クラスごとの情報をマップに格納
			Map<String, Object> participantDetails = new HashMap<>();
			participantDetails.put("participantId", participantId); // participantIdはint型のまま追加
			participantDetails.put("className", className);
			participantDetails.put("participantName", participantName);
			participantDetails.put("affiliation", affiliation);

			// マップをリストに追加
			classList.add(participantDetails);
		}

		Timestamp day = null;
		boolean check = true;
		LocalDate today = LocalDate.now();
		String sql3 = "SELECT event_date FROM tournament_table WHERE tournament_id = ?";
		try {
			day = jdbcTemplate.queryForObject(sql3, Timestamp.class, tournamentID);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからなかった場合の処理（例: ログメッセージの出力、例外のスロー）
			// 結果が見つからなかった場合の特別な値を返すことを検討すると良いでしょう。
		}
		if (day != null) {
			LocalDateTime eventDateTime = day.toLocalDateTime();
			LocalDate eventDate = eventDateTime.toLocalDate();

			if (eventDate.isBefore(today) || eventDate.isEqual(today)) {
				// イベント日が今日の日付より前、または今日と同じ場合の処理
				check = false;
			}
		}
		classDetailsMap.put("reregistration",
				Collections.singletonList(Collections.singletonMap("reregistration", check)));

		return classDetailsMap;
	}

	public List<Map<String, Integer>> getExpectedNumbers(int userId, int tournamentId, int classId) {
		String sql = "SELECT number_1, number_2, number_3 FROM expected_table WHERE user_id = ? AND tournament_id = ? AND class_id = ?";

		try {
			List<Map<String, Integer>> expectedNumbersList = jdbcTemplate.query(
					sql,
					(rs, rowNum) -> {
						Map<String, Integer> numbers = new HashMap<>();
						numbers.put("number_1", rs.getInt("number_1"));
						numbers.put("number_2", rs.getInt("number_2"));
						numbers.put("number_3", rs.getInt("number_3"));
						return numbers;
					},
					userId, tournamentId, classId);

			return expectedNumbersList;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Expected numbers not found for user ID: " + userId + ", tournament ID: " + tournamentId
					+ ", class ID: " + classId);
			return Collections.emptyList(); // 空のリストを返すことも考えられます
		}
	}

	@Override
	public boolean expectedTournament(int userId, int tournamentId, int classId, int number1, int number2,
			int number3) {
		// ユーザー、トーナメント、クラスごとのレコードが存在するか確認
		if (existsExpected(userId, tournamentId, classId)) {
			// 同じユーザー、トーナメント、クラスのレコードが存在する場合はアップデート
			return updateExpected(userId, tournamentId, classId, number1, number2, number3);
		} else {
			// 同じユーザー、トーナメント、クラスのレコードが存在しない場合は新規にインサート
			return insertNewExpected(userId, tournamentId, classId, number1, number2, number3);
		}
	}

	private boolean existsExpected(int userId, int tournamentId, int classId) {
		String sql = "SELECT COUNT(*) FROM expected_table WHERE user_id = ? AND tournament_id = ? AND class_id = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, userId, tournamentId, classId) > 0;
	}

	private boolean updateExpected(int userId, int tournamentId, int classId, int number1, int number2, int number3) {
		String sql = "UPDATE expected_table SET number_1 = ?, number_2 = ?, number_3 = ? WHERE user_id = ? AND tournament_id = ? AND class_id = ?";
		int affectedRows = jdbcTemplate.update(sql, number1, number2, number3, userId, tournamentId, classId);
		return affectedRows > 0;
	}

	private boolean insertNewExpected(int userId, int tournamentId, int classId, int number1, int number2,
			int number3) {
		String sql = "INSERT INTO expected_table (user_id, tournament_id, class_id, number_1, number_2, number_3) VALUES (?, ?, ?, ?, ?, ?)";
		int affectedRows = jdbcTemplate.update(sql, userId, tournamentId, classId, number1, number2, number3);
		return affectedRows > 0;
	}

	@Override
	public List<Map<String, String>> getPreNumbers(int tournamentId, int classId) {
		try {
			List<Map<String, String>> classParticipantNamesList = new ArrayList<>();

			// number1 の最も多い participant_name を取得するクエリ
			String sql1 = "SELECT p.class_name, p.participant_name, p.affiliation " +
					"FROM participant_table p " +
					"JOIN ( " +
					"    SELECT number_1 AS participant_id, COUNT(*) AS count " +
					"    FROM expected_table " +
					"    WHERE tournament_id = ? AND class_id = ? " +
					"    GROUP BY participant_id " +
					"    ORDER BY COUNT(*) DESC " +
					"    LIMIT 1 " +
					") e ON p.participant_id = e.participant_id";
			Map<String, String> participantName1 = jdbcTemplate.queryForObject(
					sql1,
					(rs, rowNum) -> {
						Map<String, String> names = new HashMap<>();
						names.put("class_name", rs.getString("class_name"));
						names.put("participant_name", rs.getString("participant_name"));
						names.put("affiliation", rs.getString("affiliation"));
						names.put("number", "1");
						return names;
					},
					tournamentId, classId);
			classParticipantNamesList.add(participantName1);

			// number2 の最も多い participant_name を取得するクエリ
			String sql2 = "SELECT p.class_name, p.participant_name, p.affiliation " +
					"FROM participant_table p " +
					"JOIN ( " +
					"    SELECT number_2 AS participant_id, COUNT(*) AS count " +
					"    FROM expected_table " +
					"    WHERE tournament_id = ? AND class_id = ? " +
					"    GROUP BY participant_id " +
					"    ORDER BY COUNT(*) DESC " +
					"    LIMIT 1 " +
					") e ON p.participant_id = e.participant_id";

			Map<String, String> participantName2 = jdbcTemplate.queryForObject(
					sql2,
					(rs, rowNum) -> {
						Map<String, String> names = new HashMap<>();
						names.put("class_name", rs.getString("class_name"));
						names.put("participant_name", rs.getString("participant_name"));
						names.put("affiliation", rs.getString("affiliation"));
						names.put("number", "2");
						return names;
					},
					tournamentId, classId);
			classParticipantNamesList.add(participantName2);

			// number3 の最も多い participant_name を取得するクエリ
			String sql3 = "SELECT p.class_name, p.participant_name, p.affiliation " +
					"FROM participant_table p " +
					"JOIN ( " +
					"    SELECT number_3 AS participant_id, COUNT(*) AS count " +
					"    FROM expected_table " +
					"    WHERE tournament_id = ? AND class_id = ? " +
					"    GROUP BY participant_id " +
					"    ORDER BY COUNT(*) DESC " +
					"    LIMIT 1 " +
					") e ON p.participant_id = e.participant_id";

			Map<String, String> participantName3 = jdbcTemplate.queryForObject(
					sql3,
					(rs, rowNum) -> {
						Map<String, String> names = new HashMap<>();
						names.put("class_name", rs.getString("class_name"));
						names.put("participant_name", rs.getString("participant_name"));
						names.put("affiliation", rs.getString("affiliation"));
						names.put("number", "3");
						return names;
					},
					tournamentId, classId);
			classParticipantNamesList.add(participantName3);

			return classParticipantNamesList;
		} catch (EmptyResultDataAccessException e) {
			System.out.println(
					"Expected numbers not found for tournament ID: " + tournamentId + ", class ID: " + classId);
			return Collections.emptyList();
		}
	}

	@Override
	public int getEventDate(int tournamentId) {
		Timestamp day = null;
		int check = 0;
		LocalDate today = LocalDate.now();
		String sql3 = "SELECT event_date FROM tournament_table WHERE tournament_id = ?";
		try {
			day = jdbcTemplate.queryForObject(sql3, Timestamp.class, tournamentId);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからなかった場合の処理（例: ログメッセージの出力、例外のスロー）
			// 結果が見つからなかった場合の特別な値を返すことを検討すると良いでしょう。
		}
		if (day != null) {
			LocalDateTime eventDateTime = day.toLocalDateTime();
			LocalDate eventDate = eventDateTime.toLocalDate();

			if (eventDate.isBefore(today) || eventDate.isEqual(today)) {
				// イベント日が今日の日付より後、または今日と同じ場合の処理
				check = 1;
			}
		}
		return check;
	}

	@Override
	public int getResultCount(int tournamentId) {
		String sql = "SELECT result_count FROM tournament_table WHERE tournament_id = ?";
		return jdbcTemplate.queryForObject(sql, int.class, tournamentId);
	}

	@Override
	public void insertResult(int tournamentId, int classId, List<ResultDB> list) {
		// リストが空であるか、サイズが0である場合は何もしない
		if (list == null || list.isEmpty()) {
			return;
		}
		// 最初のResultDBオブジェクトから結果情報を取得
		ResultDB firstResult = list.get(0);
		String result1Name = firstResult.getResult1Name();
		String result1Affiliation = firstResult.getResult1Affiliation();
		String result2Name = (list.size() > 1) ? list.get(1).getResult2Name() : null;
		String result2Affiliation = (list.size() > 1) ? list.get(1).getResult2Affiliation() : null;
		String result3Name = (list.size() > 2) ? list.get(2).getResult3Name() : null;
		String result3Affiliation = (list.size() > 2) ? list.get(2).getResult3Affiliation() : null;

		// SQL文の準備
		String sql = "INSERT INTO result_table (tournament_id, class_id, result1_name, result1_affiliation, result2_name, result2_affiliation, result3_name, result3_affiliation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		// SQL実行
		jdbcTemplate.update(sql, tournamentId, classId, result1Name, result1Affiliation, result2Name,
				result2Affiliation, result3Name, result3Affiliation);
	}

	@Override
	public void updateResultCount(int tournamentId) {
		String sql = "UPDATE tournament_table SET result_count = 1 WHERE tournament_id = ?";
		jdbcTemplate.update(sql, tournamentId);
	}

	@Override
	public Map<String, List<Map<String, Object>>> getResult(int tournamentId, int classId, String className) {
		Map<String, List<Map<String, Object>>> resultDetailsMap = new HashMap<>();
		String sql = "SELECT result1_name, result1_affiliation, result2_name, result2_affiliation, result3_name, result3_affiliation FROM result_table WHERE tournament_id = ? AND class_id = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, tournamentId, classId);

		for (Map<String, Object> result : resultList) {
			List<Map<String, Object>> classList = resultDetailsMap.computeIfAbsent(className, k -> new ArrayList<>());

			Map<String, Object> participantDetails1 = new HashMap<>();
			participantDetails1.put("number", "1");
			participantDetails1.put("affiliation", result.get("result1_affiliation"));
			participantDetails1.put("participantName", result.get("result1_name"));
			classList.add(participantDetails1);

			Map<String, Object> participantDetails2 = new HashMap<>();
			participantDetails2.put("number", "2");
			participantDetails2.put("affiliation", result.get("result2_affiliation"));
			participantDetails2.put("participantName", result.get("result2_name"));
			classList.add(participantDetails2);

			Map<String, Object> participantDetails3 = new HashMap<>();
			participantDetails3.put("number", "3");
			participantDetails3.put("affiliation", result.get("result3_affiliation"));
			participantDetails3.put("participantName", result.get("result3_name"));
			classList.add(participantDetails3);
		}

		return resultDetailsMap;
	}

	//予想で当たってた数を求める
	@Override
	public int resultVerification(int userId, int tournamentId, int classId) {
		String sql = "SELECT SUM("
				+ "CASE WHEN (p1.participant_name = r.result1_name) THEN 1 ELSE 0 END + "
				+ "CASE WHEN (p2.participant_name = r.result2_name) THEN 1 ELSE 0 END + "
				+ "CASE WHEN (p3.participant_name = r.result3_name) THEN 1 ELSE 0 END "
				+ ") AS count_matching_results "
				+ "FROM expected_table e "
				+ "LEFT JOIN participant_table p1 ON e.number_1 = p1.participant_id "
				+ "LEFT JOIN participant_table p2 ON e.number_2 = p2.participant_id "
				+ "LEFT JOIN participant_table p3 ON e.number_3 = p3.participant_id "
				+ "LEFT JOIN result_table r ON e.tournament_id = r.tournament_id "
				+ "AND e.class_id = r.class_id "
				+ "WHERE e.user_id = ? AND e.tournament_id = ? AND e.class_id = ?";

		try {
			Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, tournamentId, classId);
			return result != null ? result.intValue() : 0;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	@Override
	public void updateCount(int userId, int countResult) {
		String sql = "UPDATE user_table SET count = count + ? WHERE user_id = ?";
		jdbcTemplate.update(sql, countResult, userId);

	}

	//当たった回数がすでに加算されているかのチェック
	@Override
	public int checkAddition(int userId, int tournamentId, int classId) {
		String sql = "SELECT addition FROM expected_table WHERE user_id = ? AND tournament_id = ? AND class_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class, userId, tournamentId, classId);
		} catch (EmptyResultDataAccessException e) {
			return 1;
		}
	}

	@Override
	public void uppdateAddition(int userId, int tournamentId, int classId) {
		String sql = "UPDATE expected_table SET addition = 1 WHERE user_id = ? AND tournament_id = ? AND class_id = ?";
		jdbcTemplate.update(sql, userId, tournamentId, classId);
	}

	@Override
	public Map<String, List<Map<String, Object>>> getExpectedParticipant(int userId, int tournamentId, int classId,
			String keyName) {
		Map<String, List<Map<String, Object>>> classDetailsMap = new HashMap<>();
		List<Map<String, Integer>> expectedNumbersList = getExpectedNumbers(userId, tournamentId, classId);
		for (Map<String, Integer> expectedNumberMap : expectedNumbersList) {
			Integer number_1 = expectedNumberMap.get("number_1");
			Integer number_2 = expectedNumberMap.get("number_2");
			Integer number_3 = expectedNumberMap.get("number_3");
			String expectedKey = keyName; // ユニークなキーを定義

			String sql = "SELECT participant_id, class_name, participant_name, affiliation FROM participant_table WHERE participant_id IN (?, ?, ?)";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, number_1, number_2, number_3);

			for (Map<String, Object> result : resultList) {
				int participantId = (int) result.get("participant_id");
				String className = (String) result.get("class_name");
				String participantName = (String) result.get("participant_name");
				String affiliation = (String) result.get("affiliation");

				Map<String, Object> participantDetails = new HashMap<>();
				participantDetails.put("participantId", participantId);
				participantDetails.put("className", className);
				participantDetails.put("participantName", participantName);
				participantDetails.put("affiliation", affiliation);

				if (participantId == number_1) {
					participantDetails.put("number", "1");
				}
				if (participantId == number_2) {
					participantDetails.put("number", "2");
				}
				if (participantId == number_3) {
					participantDetails.put("number", "3");
				}

				classDetailsMap.computeIfAbsent(expectedKey, k -> new ArrayList<>()).add(participantDetails);
			}
		}
		return classDetailsMap;
	}

}
