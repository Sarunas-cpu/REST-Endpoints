package com.task.buildingsregistry.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.task.buildingsregistry.application.BuildingService;
import com.task.buildingsregistry.domain.Building;
import com.task.buildingsregistry.persistance.BuildingRepository;


@RunWith(MockitoJUnitRunner.class)
class BuildingServiceTest {

	@InjectMocks
	private BuildingService buildingService;

	@Mock
	private BuildingRepository buildingRepository;

	Building building1;
	Building building2;
	Building building3;

	List<Building> buildings1;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		building1 = new Building("London Street 15", "Peter Peterson", 400, new BigDecimal(500), "apartment");
		building2 = new Building("Cambridge Street 1", "Peter Peterson", 330, new BigDecimal(30), "house");
		building3 = new Building("Waka Street 10", "Peter Peterson", 30, new BigDecimal(500), "industrial");

		buildings1 = new ArrayList<>();

		buildings1.add(building1);
		buildings1.add(building2);
		buildings1.add(building3);
	}
	
	/*
	 * Test to see if tax are being calculated correctly for all buildings per owner
	 */
	@Test
	public void getBuildingsTaxByOwnerTest() {
		when(buildingRepository.findAllByOwner(anyString())).thenReturn(new HashSet<>(buildings1));

		BigDecimal yearlyTax = buildingService.getBuildingsTaxByOwner("Peter Peterson");	
		BigDecimal yearlyTaxExpected = new BigDecimal(0.0);
				   yearlyTaxExpected = yearlyTaxExpected.add(building1.getMarketValue().multiply(buildingService.getAPPARTMENT_TAX()));
				   yearlyTaxExpected = yearlyTaxExpected.add(building2.getMarketValue().multiply(buildingService.getHOUSE_TAX()));
				   yearlyTaxExpected = yearlyTaxExpected.add(building3.getMarketValue().multiply(buildingService.getINDUSTRIAL_TAX()));
	 
		assertThat(yearlyTaxExpected.equals(yearlyTax)).isTrue();
	}
}
