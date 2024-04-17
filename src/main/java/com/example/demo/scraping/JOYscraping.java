package com.example.demo.scraping;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.example.demo.entity.ClassDB;
import com.example.demo.entity.ParticipantDB;
import com.example.demo.entity.TournamentDB;

@Component
public class JOYscraping {

	public List<TournamentDB> getJOY() throws ParseException {
		List<TournamentDB> list = new ArrayList<TournamentDB>();
		try {
			// タイムアウトを10秒に設定
	        Document document = Jsoup.connect("https://japan-o-entry.com")
	                                  .timeout(10000)  // タイムアウト時間をミリ秒で指定
	                                  .get();
			String format = "yyyy/M/d";

			// 現在の年を取得
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);

			// スタイルに基づいて複数の要素を取得
			Elements elementsWithEventId = document.select("[id^=event]");
			for (Element element : elementsWithEventId) {
				// 各要素に対する処理を行う
				Element dateElement = element.select("td[style*=width: 15%]").first();
				
				Element occupationElement = element.select("span.event_icon").first();
			
				Element eventNameElement = element.select("td a[href*=event/view]").first();

				// 要素が存在するか確認し、結果の出力
				if (dateElement != null && occupationElement != null && eventNameElement != null) {
					String date = dateElement.ownText();
					String occupation = occupationElement.text();
					String eventName = eventNameElement.ownText();

					if (date.matches("\\d{1,2}/\\d{1,2} \\(\\S+\\)") && "OL".equals(occupation)) {
						date = currentYear + "/" + date;
						SimpleDateFormat dateFormat = new SimpleDateFormat(format);
						java.util.Date utilDate = dateFormat.parse(date);
						Timestamp timestamp = new Timestamp(utilDate.getTime());
						TournamentDB db = new TournamentDB();
						db.setTournamentName(eventName);
						db.setEventDate(timestamp);
						list.add(db);
					}
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			  // スクレイピングが失敗した場合はnullを返す
            return null;
		}
		return list;
	}

	public List<ParticipantDB> getParticipant(int tournamentId, String tournamentName, String className1,
			String className2) {
		List<ParticipantDB> list = new ArrayList<ParticipantDB>();
		String hrefValue = null;
		try {
			Document document = Jsoup.connect("https://japan-o-entry.com")
                    .timeout(10000)  // タイムアウト時間をミリ秒で指定
                    .get();
			// スタイルに基づいて複数の要素を取得
			Elements aElements = document.select("a[href]");
			for (Element aElement : aElements) {
				// リンクを取得
				String text = aElement.text();
				if (tournamentName.equals(text)) {
					hrefValue = aElement.attr("href");
					break; // 必要に応じてループを抜けます
				}
			}
			   // URLがnullの場合は処理を中止してnullを返す
	        if (hrefValue == null) {
	            System.out.println("URLが見つかりませんでした。");
	            return null;
	        }
			Document document3 = Jsoup.connect(hrefValue + "/show_detail#entrylist").get();
			Elements rows = document3.select("h3:contains(全リスト) + table tbody tr");
			for (Element row : rows) {
				// <td> 要素を取得
				Elements cells = row.select("td");

				if (cells.size() >= 3) { // 最低限の要素数を確認
					String className = cells.get(0).text();
					String playerName = cells.get(1).text();
					String affiliation = cells.get(2).text();

					if (className1.equals(className) || className2.equals(className)) {
						ParticipantDB pDB = new ParticipantDB();

						pDB.setTournamentId(tournamentId);
						pDB.setClassName(className);
						pDB.setParticipantName(playerName);
						pDB.setAffiliation(affiliation);
						list.add(pDB);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	//classが一つの場合
	public List<ParticipantDB> getParticipant(int tournamentId, String tournamentName, String className1) {
		List<ParticipantDB> list = new ArrayList<ParticipantDB>();
		String hrefValue = null;
		try {
			Document document = Jsoup.connect("https://japan-o-entry.com")
                    .timeout(10000)  // タイムアウト時間をミリ秒で指定
                    .get();
			// スタイルに基づいて複数の要素を取得
			Elements aElements = document.select("a[href]");
			for (Element aElement : aElements) {
				// リンクを取得
				String text = aElement.text();
				if (tournamentName.equals(text)) {
					hrefValue = aElement.attr("href");
					break; // 必要に応じてループを抜けます
				}
			}
			   // URLがnullの場合は処理を中止してnullを返す
	        if (hrefValue == null) {
	            System.out.println("URLが見つかりませんでした。");
	            return null;
	        }
			Document document3 = Jsoup.connect(hrefValue + "/show_detail#entrylist").get();
			Elements rows = document3.select("h3:contains(全リスト) + table tbody tr");
			for (Element row : rows) {
				// <td> 要素を取得
				Elements cells = row.select("td");

				if (cells.size() >= 3) { // 最低限の要素数を確認
					String className = cells.get(0).text();
					String playerName = cells.get(1).text();
					String affiliation = cells.get(2).text();

					if (className1.equals(className)) {
						ParticipantDB pDB = new ParticipantDB();

						pDB.setTournamentId(tournamentId);
						pDB.setClassName(className);
						pDB.setParticipantName(playerName);
						pDB.setAffiliation(affiliation);
						list.add(pDB);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public List<ClassDB> getJOYClass(String tournamentName) {
		List<ClassDB> list = new ArrayList<ClassDB>();
		String url = null;
		try {
			Document document = Jsoup.connect("https://japan-o-entry.com")
                    .timeout(10000)  // タイムアウト時間をミリ秒で指定
                    .get();
			// スタイルに基づいて複数の要素を取得
			Elements aElements = document.select("a[href]");
			for (Element aElement : aElements) {
				// リンクを取得
				String text = aElement.text();
				if (tournamentName.equals(text)) {
					url = aElement.attr("href");
					break; // 必要に応じてループを抜けます
				}
			}
			   // URLがnullの場合は処理を中止してnullを返す
	        if (url == null) {
	            System.out.println("URLが見つかりませんでした。");
	            return null;
	        }
			Document document3 = Jsoup.connect(url + "/show_detail#entrylist").get();
			Elements rows = document3.select("h3:contains(全リスト) + table tbody tr");

			Map<String, Integer> classNameCountMap = new HashMap<>();

			for (Element row : rows) {
				Elements cells = row.select("td");

				if (cells.size() >= 3) {
					String className = cells.get(0).text();

					// クラス名の出現回数をカウント
					classNameCountMap.put(className, classNameCountMap.getOrDefault(className, 0) + 1);
				}
			}

			// クラス名を出現回数の降順でソート
			List<Map.Entry<String, Integer>> sortedClassNames = classNameCountMap.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.collect(Collectors.toList());

			// 上から1つ目のMの部分位置で一番多いクラス名を取得
	        String topMClassName = sortedClassNames.stream()
	                .filter(entry -> entry.getKey().startsWith("M"))
	                .findFirst()
	                .map(Map.Entry::getKey)
	                .orElse(null);

	        // 上から2つ目のWの部分一致で一番多いクラス名を取得
	        String topWClassName = sortedClassNames.stream()
	                .filter(entry -> entry.getKey().contains("W"))
	                .findFirst()
	                .map(Map.Entry::getKey)
	                .orElse(null);

	        // もし取得できなかった場合は、出現回数の多い上位2つのクラス名を取得
	        if (topMClassName == null || topWClassName == null) {
	            List<String> top2ClassNames = sortedClassNames.stream()
	                    .limit(2)
	                    .map(Map.Entry::getKey)
	                    .collect(Collectors.toList());

	            for (String className : top2ClassNames) {
	                ClassDB classDB = new ClassDB();
	                classDB.setClassName(className);
	                list.add(classDB);
	            }
	        } else {
	            // 取得できた場合は、それぞれのクラスDBオブジェクトを生成してlistに追加
	            if (topMClassName != null) {
	                ClassDB classDBM = new ClassDB();
	                classDBM.setClassName(topMClassName);
	                list.add(classDBM);
	            }

	            if (topWClassName != null) {
	                ClassDB classDBW = new ClassDB();
	                classDBW.setClassName(topWClassName);
	                list.add(classDBW);
	            }
	        }

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;

	}

}
