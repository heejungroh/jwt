package com.cos.jwt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.cos.jwt.domain.person.Person;
import com.cos.jwt.domain.person.PersonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class IndexController {
	
	private final PersonRepository personRepository;
	
	
	@GetMapping({"","/"})
	public String index() {
		return "index";
	}
	
	@PostMapping("/joinProc")
	public String 회원가입(@RequestBody Person person) {
		personRepository.save(person);
		return "ok";
	}
	
	//@CrossOrigin(origins = "http://127.0.0.1:5500", methods = RequestMethod.GET)
		@GetMapping("/person/{id}")
		public ResponseEntity<?> 회원정보(@PathVariable int id, HttpServletResponse response, HttpServletRequest request) {
			//response.setHeader("Access-Control-Allow-Origin", "*");
			String jwtToken = request.getHeader("Authorization");
			System.out.println("회원정보요청 : "+jwtToken);
			
		      if(jwtToken == null) {
		          return new ResponseEntity<String>("인증안됨",HttpStatus.FORBIDDEN);
		       } else {
		          jwtToken = jwtToken.replace("Bearer", "");
		          
		          int personId = 
		                JWT.require(Algorithm.HMAC512("비밀키")).build().verify(jwtToken).getClaim("id").asInt();         
		          System.out.println("personId : "+personId);
		       
		          return new ResponseEntity<Person>(personRepository.findById(id).get(), HttpStatus.OK);
		       }
		    }
		 }

