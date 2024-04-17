package com.example.demo.scraping;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.example.demo.entity.ResultDB;

@Component
public class LapCenterscraping {

	public List<ResultDB> getLapCenter(String tournamentName, String classNameDB) throws ParseException {
		List<ResultDB> list = new ArrayList<ResultDB>();
		String recordListUrl = null;
		 String recordListUrl1 = null;
		try {
			// タイムアウトを10秒に設定
			Document document = Jsoup.connect("https://mulka2.com/lapcenter/index.jsp")
					.timeout(10000) // タイムアウト時間をミリ秒で指定
					.get();

			//大会のページの取得
			// イベントアイテム1クラスを持つすべての要素を取得
			Elements eventItems = document.select(".event-item-1");
			// イベントアイテム1ごとに処理
			for (Element eventItem : eventItems) {
				// 大会名を取得
				String eventName = eventItem.select(".event-name-1").text();

				// 大会名が指定したものと一致する場合のみ、URLを取得
				if (eventName.equals(tournamentName)) {
					// 記録一覧のURLを取得
					Element eventFile = eventItem.select(".event-file").first();
					recordListUrl = "https://mulka2.com/lapcenter/" + eventFile.select("a").attr("href");
					System.out.println("大会名: " + eventName);
					System.out.println("記録一覧のURL: " + recordListUrl);
					break; // 大会名が見つかったらループを終了
				}
			}
			
			 if (recordListUrl == null) {
		            // URLが存在しない場合はnullを返す
		            return null;
		        }
			Document document2 = Jsoup.connect(recordListUrl)
					.timeout(10000) // タイムアウト時間をミリ秒で指定
					.get();
			
			// tr要素をすべて取得
            Elements trElements = document2.select("tbody tr");
            
            //クラスの取得
         // tr要素ごとに処理
            for (Element trElement : trElements) {
                // tr要素内のクラス名を取得
                String className = trElement.select("td b").text();

                // クラス名が指定したものと一致する場合のみ、記録一覧のURLを取得
                if (className.equals(classNameDB)) {
                    // 記録一覧のURLを取得
                    Element recordListElement = trElement.select("td a[href]").first();
                    recordListUrl1 = "https://mulka2.com/lapcenter/lapcombat2/" + recordListElement.attr("href");
                    System.out.println("クラス名: " + className);
                    System.out.println("記録一覧のURL: " + recordListUrl1);
                    break; // クラス名が見つかったらループを終了
                }
            }
            //順位の取得
            Document document3 = Jsoup.connect(recordListUrl1)
					.timeout(10000) // タイムアウト時間をミリ秒で指定
					.get();
            
         // 各行のデータを含むtr要素を取得
            Elements trElements2 = document3.select("tbody tr");

         // 各行のデータを処理
            for (Element trElement : trElements2) {
                // 各列のデータを取得
                Elements tdElements = trElement.select("td");

                // tdElementsが空でないことを確認
                if (!tdElements.isEmpty()) {
                    // 最初のtd要素のテキストが数字であるかをチェック
                    String numberText = tdElements.get(0).text().trim();
                    if (numberText.equals("1") || numberText.equals("2") || numberText.equals("3")) {
                        // 数字、名前、所属を取得
                        String number = numberText;
                        String name = tdElements.get(1).text().trim();
                        String affiliation = tdElements.get(3).text().trim();

                        // 取得したデータを出力
                        System.out.println("数字: " + number);
                        System.out.println("名前: " + name);
                        System.out.println("所属: " + affiliation);
                        
                     // ResultDBオブジェクトを作成し、リストに追加
                        ResultDB resultDB = new ResultDB();
                        // 各数字の名前をセット
                        if (number.equals("1")) {
                            resultDB.setResult1Name(name);
                            resultDB.setResult1Affiliation(affiliation);
                        } else if (number.equals("2")) {
                            resultDB.setResult2Name(name);
                            resultDB.setResult2Affiliation(affiliation);
                        } else if (number.equals("3")) {
                            resultDB.setResult3Name(name);
                            resultDB.setResult3Affiliation(affiliation);
                        }
                        list.add(resultDB);
                    }
                }
            }

		} catch (IOException e) {
			e.printStackTrace();
			// スクレイピングが失敗した場合はnullを返す
			return null;
		}
		return list;

	}
}
