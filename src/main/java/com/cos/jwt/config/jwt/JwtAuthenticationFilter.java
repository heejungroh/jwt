package com.cos.jwt.config.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.domain.person.Person;
import com.cos.jwt.domain.person.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter implements Filter{
	
	
	private PersonRepository personRepository;
	
	public JwtAuthenticationFilter(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("JwtAuthenticationFilter 작동");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		PrintWriter out = resp.getWriter();
		
		String method = req.getMethod();
		System.out.println(method);
		if(!method.equals("POST")) {
			System.out.println("post요청이 아니기 때문에 거절");
			out.print("required post method");
			out.flush();
			return;
			
		}else {
			System.out.println("post요청이 맞습니다.");
			
			ObjectMapper om = new ObjectMapper();
			try {
				Person person = om.readValue(req.getInputStream(), Person.class);
				System.out.println(person);
				
				//1번 username, password를 DB에 던짐.
				Person PersonEntity = 
				personRepository.findByUsernameAndPassword(person.getUsername(),person.getPassword());
				//2번 값이 있으면 있다? 없다?
				if(PersonEntity ==null) {
					System.out.println("유저네임 혹은 패스워드가 틀렸습니다.");
					out.print("fail");
					out.flush();
				}else {
					System.out.println("인증되었습니다.");
					
					String jwtToken = JWT.create()
							.withSubject("코스토큰")
							.withExpiresAt(new Date(System.currentTimeMillis()+1000*60*10))
							.withClaim("id", PersonEntity.getId())
							.withClaim("username", person.getUsername())
							.sign(Algorithm.HMAC512("코스"));
					
					resp.addHeader("Authorization", "Bearer" + jwtToken);
					out.print("ok");
					out.flush();
							
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
				
			}
		}
	}

}
