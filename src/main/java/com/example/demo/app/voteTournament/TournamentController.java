package com.example.demo.app.voteTournament;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.ClassDB;
import com.example.demo.entity.TournamentDB;
import com.example.demo.service.TournamentService;

@Controller
@RequestMapping("/apiTournament")
@CrossOrigin(origins = "https://oriscore.net")
public class TournamentController {

	private final TournamentService tournamentService;

	public TournamentController(TournamentService tournamentService){
		this.tournamentService = tournamentService;
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<TournamentDB>> getTournament(){
		 List<TournamentDB> tournamentList = tournamentService.getTournament();
	        return ResponseEntity.ok().body(tournamentList);
		
	}
	
	@GetMapping("/getClass")
	public ResponseEntity<List<ClassDB>> getClassName(@RequestParam("tournamentId") int tournamentId) {
	    List<ClassDB> classList = tournamentService.getClassName(tournamentId);
	    return ResponseEntity.ok().body(classList);
	}


	
	@GetMapping("/getParticipant")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getParticipant(@RequestParam("userId") int userId, @RequestParam("tournamentId") int tournamentId, @RequestParam("className1") String className1,@RequestParam("className2") String className2){
		System.out.println("tournamentId"+ tournamentId);
		System.out.println("className1"+ className1);
		System.out.println("className2"+ className2);
		if (className2 == null || className2.isEmpty() || "undefined".equals(className2)) {
	        // className2がnullの場合、className1のみを使用して処理を行う
	        Map<String, List<Map<String, Object>>> tournamentList = tournamentService.getParticipant(userId, tournamentId, className1);
	        return ResponseEntity.ok().body(tournamentList);
	    } else {
	        // className1とclassName2の両方を使用して処理を行う
	        Map<String, List<Map<String, Object>>> tournamentList = tournamentService.getParticipant(userId, tournamentId, className1, className2);
	        return ResponseEntity.ok().body(tournamentList);
	    }
		
	}
	
	@PostMapping("/expected")
	public ResponseEntity<String>  expectedTournament(@RequestBody expectedTournamentForm form){
		boolean check = false;
		
	    // form オブジェクトから必要な値を取得
	    int userId = form.getUserId();
	    int tournamentId = form.getTournamentId();
	    int classId1 = form.getClassId1();
	    int classId2 = form.getClassId2();
	    String number1Str = form.getNumber1();
	    String number2Str = form.getNumber2();
	    String number3Str = form.getNumber3();
	    String number4Str = form.getNumber4();
	    String number5Str = form.getNumber5();
	    String number6Str = form.getNumber6();

	    // 文字列が空の場合は null に変更
	    String[] numberStrArray = {number1Str, number2Str, number3Str, number4Str, number5Str, number6Str};
	    for (int i = 0; i < numberStrArray.length; i++) {
	        if (numberStrArray[i] != null && numberStrArray[i].isEmpty()) {
	            numberStrArray[i] = null;
	        }
	    }

	    // null チェックを含んだ parseInt
	    int number1 = (numberStrArray[0] != null) ? Integer.parseInt(numberStrArray[0]) : 0;
	    int number2 = (numberStrArray[1] != null) ? Integer.parseInt(numberStrArray[1]) : 0;
	    int number3 = (numberStrArray[2] != null) ? Integer.parseInt(numberStrArray[2]) : 0;
	    int number4 = (numberStrArray[3] != null) ? Integer.parseInt(numberStrArray[3]) : 0;
	    int number5 = (numberStrArray[4] != null) ? Integer.parseInt(numberStrArray[4]) : 0;
	    int number6 = (numberStrArray[5] != null) ? Integer.parseInt(numberStrArray[5]) : 0;

	    // これ以降は各変数を使って処理を続ける

	    
	    check = tournamentService.expectedTournament(userId, tournamentId, classId1, classId2, number1, number2, number3, number4, number5, number6);
		
		  if (check) {
	            return ResponseEntity.ok("User created successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("User creation failed");
	        }
	}
	
	//全体予想順位取得
	@GetMapping("/getpreResult")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getPreResult(@RequestParam("userId") int userId, @RequestParam("tournamentId") int tournamentId, @RequestParam("className1") String className1,@RequestParam("className2") String className2){
		int check = 0;
		Map<String, List<Map<String, Object>>> tournamentList = null;
		check = tournamentService.checkResultDate(tournamentId);
		if(check == 0) {
			 tournamentList = tournamentService.getPreResult( tournamentId, className1, className2);
		}else if(check == 1) {
			tournamentList = tournamentService.getResult(userId, tournamentId, className1, className2);
		}	
		if (tournamentList == null || tournamentList.isEmpty()) {
	        // エラーが発生した場合、エラーレスポンスを作成して返す
	        Map<String, Object> errorResponse = new HashMap<>();
	        Map<String, Object> Check = new HashMap<>();
	        if (check == 0) {
	            errorResponse.put("preResultNotFound", 404); // 404は例としてのエラーコードです
	            Check.put("Check", check);
	        } else if (check == 1) {
	            errorResponse.put("resultNotFound", 777); // 404は例としてのエラーコードです
	            Check.put("Check", check);
	        }
	        Map<String, List<Map<String, Object>>> errorMap = new HashMap<>();
	        errorMap.put("error", Collections.singletonList(errorResponse));
	        errorMap.put("check", Collections.singletonList(Check));
	        return ResponseEntity.ok().body(errorMap);
	    }
		 tournamentList.put("check", Collections.singletonList(Collections.singletonMap("Check", check)));
		return ResponseEntity.ok().body(tournamentList);
		    
	}
}
