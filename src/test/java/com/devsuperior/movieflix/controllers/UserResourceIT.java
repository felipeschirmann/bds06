package com.devsuperior.movieflix.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;

	private String visitorUsername;
	private String visitorPassword;
	private String memberUsername;
	private String memberPassword;

	@BeforeEach
	void setUp() throws Exception {
		visitorUsername = "bob@gmail.com";
		visitorPassword = "123456";
		memberUsername = "ana@gmail.com";
		memberPassword = "123456";
	}

	@Test
	public void profileShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
		ResultActions result = mockMvc.perform(get("/users/profile")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnauthorized());
	}

	@Test
	public void profileShouldReturnUserProfileWhenVisitorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, visitorUsername, visitorPassword);

		ResultActions result = mockMvc.perform(get("/users/profile")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(1L));
		result.andExpect(jsonPath("$.name").value("Bob"));
		result.andExpect(jsonPath("$.email").value(visitorUsername));
	}

	@Test
	public void profileShouldReturnUserProfileWhenMemberAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, memberUsername, memberPassword);

		ResultActions result = mockMvc.perform(get("/users/profile")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(2L));
		result.andExpect(jsonPath("$.name").value("Ana"));
		result.andExpect(jsonPath("$.email").value(memberUsername));
	}
}
