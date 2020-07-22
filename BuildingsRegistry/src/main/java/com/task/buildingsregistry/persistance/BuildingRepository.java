package com.task.buildingsregistry.persistance;

import com.task.buildingsregistry.domain.Building;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * class CRUD repository to access its functionality 
 */
@Repository
public interface BuildingRepository extends CrudRepository<Building, String> {
	Set<Building> findAllByOwner(String owner);
	Building findByAddress(String address);
}
