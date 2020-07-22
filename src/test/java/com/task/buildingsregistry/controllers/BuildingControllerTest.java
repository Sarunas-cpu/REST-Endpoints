package com.task.buildingsregistry.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.buildingsregistry.application.BuildingService;
import com.task.buildingsregistry.domain.Building;
import com.task.buildingsregistry.presentation.BuildingController;

@RunWith(SpringRunner.class)
@WebMvcTest(BuildingController.class)
@Import(BuildingService.class)
class BuildingControllerTest {

	@InjectMocks
	private BuildingController buildingController;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BuildingService buildingService;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	/*
	 * Checking response for the path
	 */
	@Test
	public void getAllBuildingsTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/buildings").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());
	}

	/*
	 * Checking post response and building created
	 */
	@Test
	public void addBuildingTestNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/buildings")
				.content(objectMapper.writeValueAsString(
						new Building("London Street", "Peter Peterson", 400, new BigDecimal(500000), "appartment")))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.address").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.owner").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.marketValue").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").exists());
	}

	@Test
	public void updateBuildingTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.put("/buildings/{address}", "London_Street_15")
				.content(objectMapper.writeValueAsString(
						new Building("London Street 15", "Peter Patarson", 399, new BigDecimal(40000), "house")))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("London Street 14"))
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.owner").value("Peter Patarson"))
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(399))
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.marketValue").value(40000.0))
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("house"));
	}

	@Test
	public void getBuildingByAddressTestNotFound() throws Exception {
		when(buildingService.getAllBuildings()).thenReturn(
				Arrays.asList(new Building("London Street", "Pater Cart", 400, new BigDecimal(500000), "house")));
		mvc.perform(MockMvcRequestBuilders.get("/buildings/{address}", "London_Street_15")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
	}

}
