package com.example.demo.app.LoginLogoutUser;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.UserDB;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "https://oriscore.net")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	 @PostMapping("/authLogin")
	 public ResponseEntity<List<UserDB>> loginUser(@RequestBody  userCreateForm authRequest) {
	        String username = authRequest.getName();
	        String password = authRequest.getPass();
	        
	        List<UserDB> userList = userService.authLogin(username, password);
	        return ResponseEntity.ok().body(userList);
	    }
	
	@PostMapping("/create")
	    public ResponseEntity<String> createUser(@RequestBody @Valid userCreateForm form, BindingResult bindingResult) {
		boolean check = false;
	        // フォームのバリデーションエラーを確認
	        if (bindingResult.hasErrors()) {
	            // エラーがある場合の処理
	            // ...
	        	 return ResponseEntity.status(HttpStatus.CONFLICT).body("User creation failed");
	        }

	        String username = form.getName();
	        String password = form.getPass();
	        check = userService.userCreate(username, password);
	        
	        if (check) {
	            return ResponseEntity.ok("User created successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("User creation failed");
	        }
	}
	
	@GetMapping("/ranking")
	public ResponseEntity<List<UserDB>> getRankingUser(){
		 List<UserDB> userList = userService.getRanking();
	        return ResponseEntity.ok().body(userList);
		
	}
}
