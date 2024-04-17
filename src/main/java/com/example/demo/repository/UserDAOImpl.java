package com.example.demo.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserDB;

@Repository
public class UserDAOImpl implements UserDAO {
	
	private final JdbcTemplate jdbcTemplate;


	  public UserDAOImpl(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }
	
	@Override
	public List<UserDB> getAll(int ID) {
		String sql = "SELECT user_id, user_name FROM user_table WHERE user_id = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, ID);
		List<UserDB> list = new ArrayList<UserDB>();
		for(Map<String, Object> result : resultList) {
			UserDB userDB = new UserDB();
			userDB.setId((int)result.get("user_id"));
			userDB.setName((String)result.get("user_name"));
			list.add(userDB);
		}
		
		return list;
	}

	@Override
	public String selectPass(String name) {
        String sql = "SELECT  CONVERT( AES_DECRYPT(UNHEX(pass), 'key') USING utf8 ) FROM user_table WHERE user_name = ?";
        String hashedPassword = jdbcTemplate.queryForObject(sql, String.class, name);

        return hashedPassword;
    }

	@Override
	public int getId(String name) {
	    int ID;
	    String sql = "SELECT user_id FROM user_table WHERE user_name = ?";
	    ID = jdbcTemplate.queryForObject(sql, int.class, name);
	    return ID;
	}

	@Override
	public boolean insertUser(String name, String pass, Timestamp Date) {
		boolean check = false;
	    try {

	        String sql = "INSERT INTO user_table (user_name, pass, date) VALUES (?, HEX(AES_ENCRYPT(?,'key')), ?)";
	        jdbcTemplate.update(sql, name, pass, Date);
	        check = true;
	    } catch (DuplicateKeyException e) {
	        // ユニーク制約違反が発生した場合の処理
	        // 例えば、エラーメッセージを表示したり、適切なレスポンスを返したりすることができます。
	        // または、任意の例外処理を行うことができます。
	    	check = false;
	    }
	    return check;
	}

	@Override
	public List<UserDB> getRanking() {
	    // ランキング取得用のクエリ
	    String selectQuery = "SELECT " +
	            "  (@rankNumber := IF(@prev_count = count, @rankNumber, @rankNumber + 1)) AS rankNumber, " +
	            "  user_id, user_name, pass, count " +
	            "FROM " +
	            "  (SELECT user_id, user_name, pass, count FROM user_table ORDER BY count DESC LIMIT 20) AS ranked_users " +
	            "JOIN " +
	            "  (SELECT @rankNumber := 0, @prev_count := NULL) AS init;";

	    // ランキングクエリを実行
	    List<Map<String, Object>> resultList = jdbcTemplate.queryForList(selectQuery);

	    List<UserDB> list = new ArrayList<>();
	    for (Map<String, Object> result : resultList) {
	        UserDB userDB = new UserDB();
	        userDB.setName((String) result.get("user_name"));
	        userDB.setCount((int) result.get("count"));

	        // String型からBigInteger型に変換
	        String rankNumberString = (String) result.get("rankNumber");
	        BigInteger rankNumberBigInt = new BigInteger(rankNumberString);

	        // BigIntegerからintに変換
	        int rankNumber = rankNumberBigInt.intValue();
	        userDB.setRank(rankNumber);

	        list.add(userDB);
	    }

	    return list;
	}







}
