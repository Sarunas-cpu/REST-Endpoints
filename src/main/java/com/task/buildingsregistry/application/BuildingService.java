package com.task.buildingsregistry.application;

import com.task.buildingsregistry.domain.Building;
import com.task.buildingsregistry.persistance.BuildingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BuildingService {

	@Autowired
	private BuildingRepository buildingRepository;

	// Tax rates for different types of buildings
	final BigDecimal APPARTMENT_TAX = new BigDecimal(0.1);
	final BigDecimal HOUSE_TAX = new BigDecimal(0.05);
	final BigDecimal INDUSTRIAL_TAX = new BigDecimal(0.2);

	/*
	 * @return all buildings records
	 */
	public List<Building> getAllBuildings() {
		List<Building> buildings = new ArrayList<>();
		buildingRepository.findAll().forEach(buildings::add);
		return buildings;
	}

	/*
	 * @param address of the building
	 * 
	 * @return building at the address
	 */
	public Building getBuildingByAddress(String address) {
		return buildingRepository.findByAddress(address);
	}

	/*
	 * Create record of the building
	 */
	public void addBuilding(Building building) {
		buildingRepository.save(building);
	}

	/*
	 * Update the record of the building
	 */
	public void updateBuilding(String address, Building building) {
		buildingRepository.save(building);
	}

	/*
	 * @return total yearly real estate tax (market value times tax rate) for all
	 * properties owned by a particular owner
	 */
	public BigDecimal getBuildingsTaxByOwner(String owner) {
		Set<Building> ownedBuildings = buildingRepository.findAllByOwner(owner);

		BigDecimal totalTax = new BigDecimal(0.0);

		for (Building building : ownedBuildings) {
			// adjust tax depending on property type
			switch (building.getType()) {
			case "apartment":
				totalTax = totalTax.add(building.getMarketValue().multiply(APPARTMENT_TAX));
				break;
			case "house":
				totalTax = totalTax.add(building.getMarketValue().multiply(HOUSE_TAX));
				break;
			case "industrial":
				totalTax = totalTax.add(building.getMarketValue().multiply(INDUSTRIAL_TAX));
				break;
			default:
			}
		}
		return totalTax;
	}

	public BigDecimal getAPPARTMENT_TAX() {
		return APPARTMENT_TAX;
	}

	public BigDecimal getHOUSE_TAX() {
		return HOUSE_TAX;
	}

	public BigDecimal getINDUSTRIAL_TAX() {
		return INDUSTRIAL_TAX;
	}

	/*
	 * @return returns true if building exists in the repository
	 */
	public boolean existsByBuilding(Building building) {
		return buildingRepository.existsById(building.getAddress());
	}

	/*
	 * @return returns true if building exists in the repository
	 */
	public boolean existsByAddress(String address) {
		return buildingRepository.existsById(address);
	}
	/*
	 * @return true if owner has any buildings
	 */
	public boolean existsByOwner(String owner) {
		return !buildingRepository.findAllByOwner(owner).isEmpty();
	}

}
