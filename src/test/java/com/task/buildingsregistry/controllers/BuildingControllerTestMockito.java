package com.task.buildingsregistry.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.task.buildingsregistry.application.BuildingService;
import com.task.buildingsregistry.domain.Building;
import com.task.buildingsregistry.presentation.BuildingController;

@RunWith(MockitoJUnitRunner.class)
class BuildingControllerTestMockito {
	@InjectMocks
	private BuildingController buildingController;
	
	@Mock
	private BuildingService buildingService;
	
	Building building1;
	Building building2;
	Building building3;
	
	Building spareBuilding;
	
	List<Building> buildings1;
	List<Building> buildings2;
	
	@BeforeEach
	public void setUp() throws Exception {
		// Mock building service
		buildingService = mock(BuildingService.class);
		buildingController = new BuildingController(buildingService);
		
		building1 = new Building("London Street 15", "Peter Peterson", 400, new BigDecimal(550000), "appartment");
		building2 = new Building("Oxford Street 45", "Josh Peterson", 500, new BigDecimal(70000), "house");
		building3 = new Building("Cambridge Street 1", "Peter Anderson", 330, new BigDecimal(3000000), "industrial");
		
		spareBuilding = new Building("Waka Street 10", "Peter Anderson", 30, new BigDecimal(5000), "industrial");
				
		buildings1 = new ArrayList<>();
		
		buildings1.add(building1);
		buildings1.add(building2);
		buildings1.add(building3);
		
		buildings2 = new ArrayList<>();
		
		buildings2.add(building2);
		buildings2.add(building3);
		
	}
	/*
	 * Test if all buildings are retrieves correctly by Controller
	 */
	@Test
	public void getAllBuildingsTest() {
		when(buildingService.getAllBuildings()).thenReturn(buildings1);
		
		List<Building> buildings = buildingController.getAllBuildings();
		assertNotNull(buildings);
	    assertThat(buildings.equals(buildings1)).isTrue();
	    assertThat(buildings.equals(buildings2)).isFalse();
	}
	/*
	 * Test if correct building is retrieved by using address string
	 */	
	@Test
	public void getBuildingByAddressTest() {
		when(buildingService.getBuildingByAddress(building1.getAddress())).thenReturn(building1);
		when(buildingService.getBuildingByAddress(building2.getAddress())).thenReturn(building2);
		when(buildingService.getBuildingByAddress(building3.getAddress())).thenReturn(building3);		
		when(buildingService.existsByAddress(anyString())).thenReturn(true);		
		ResponseEntity<Building> building = buildingController.getBuildingByAddress(building1.getAddress().replace(" ", "-"));

		assertNotNull(building);
		assertNotNull(building1);
		assertEquals(building.getBody(), building1);
		
		building = buildingController.getBuildingByAddress(building2.getAddress().replace(" ", "-"));
		
		assertNotNull(building);
		assertEquals(building.getBody(), building2);
		
		building = buildingController.getBuildingByAddress(building3.getAddress().replace(" ", "-"));
		
		assertNotNull(building);
		assertEquals(building.getBody(), building3);					
	}
}


