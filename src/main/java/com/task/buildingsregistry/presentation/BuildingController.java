package com.task.buildingsregistry.presentation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.task.buildingsregistry.application.BuildingService;
import com.task.buildingsregistry.domain.Building;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api("Operations to retrieve, save and change existing building records also as calculate anual tax per owner")
public class BuildingController {

	@Autowired
	private BuildingService buildingService;

	public BuildingController() {
	}

	public BuildingController(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@ApiOperation(value = "View a list of all building records", response = List.class)
	@ApiResponse(code = 200, message = "Successfully retrieved list")
	@GetMapping("/buildings")
	public List<Building> getAllBuildings() {
		return buildingService.getAllBuildings();
	}

	@ApiOperation(value = "Find building record using address string", response = Building.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Found building record containing the address"),
			@ApiResponse(code = 404, message = "Building record not found") })
	@GetMapping("/buildings/{address}")
	public ResponseEntity<Building> getBuildingByAddress(@PathVariable("address") String address) {
		address = address.replace("-", " ");

		if (buildingService.existsByAddress(address)) {
			Building building = buildingService.getBuildingByAddress(address);
			return new ResponseEntity<Building>(building, HttpStatus.OK);
		}
		return new ResponseEntity<Building>(HttpStatus.NOT_FOUND);
	}

	@ApiOperation(value = "Find building records for specified owener", response = BigDecimal.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Exists atleast one record with an owner and anual tax is calculated"),
			@ApiResponse(code = 404, message = "Building record not found for the specified owner") })
	@GetMapping("/buildings/{owner}/yearlytax")
	public ResponseEntity<BigDecimal> getBuildingsTaxByOwner(@PathVariable("owner") String owner) {
		owner = owner.replace("-", " ");

		if (buildingService.existsByOwner(owner)) {
			BigDecimal yearlyTax = buildingService.getBuildingsTaxByOwner(owner);
			return new ResponseEntity<BigDecimal>(yearlyTax, HttpStatus.OK);
		}
		return new ResponseEntity<BigDecimal>(HttpStatus.NOT_FOUND);
	}

	@ApiOperation(value = "Add building the the record registry", response = Building.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "The building was added to the registry"),
			@ApiResponse(code = 202, message = "Request was accepted but building was not added since it already exists") })
	@PostMapping("/buildings")
	public ResponseEntity<Building> addBuilding(@RequestBody Building building) {
		if (!buildingService.existsByBuilding(building)) {
			buildingService.addBuilding(building);
			return new ResponseEntity<Building>(building, HttpStatus.CREATED);
		}
		return new ResponseEntity<Building>(HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "Change building record", response = Building.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Building record successfully updated"),
			@ApiResponse(code = 404, message = "The building record does not exist to be updated"),
			@ApiResponse(code = 403, message = "Address can not be changed") })
	@PutMapping("/buildings/{address}")
	public ResponseEntity<Building> updateBuilding(@PathVariable("address") String address,
			@RequestBody Building building) {
		address = address.replace("-", " ");

		if (buildingService.existsByAddress(address) && address.equals(building.getAddress())) {
			buildingService.updateBuilding(address, building);
			return new ResponseEntity<Building>(building, HttpStatus.OK);
		} else if (address.equals(building.getAddress())) {
			return new ResponseEntity<Building>(building, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<Building>(HttpStatus.NOT_FOUND);
	}
}
