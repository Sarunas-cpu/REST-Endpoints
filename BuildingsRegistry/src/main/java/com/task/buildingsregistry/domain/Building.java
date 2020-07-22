package com.task.buildingsregistry.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * class to represent data regarding building
 *
 */
@Entity
@Table(name = "buldings")
public class Building {
	
	@Id
    @ApiModelProperty(notes = "Uniques address used as ID")
	private String address;	
	@Column
	@ApiModelProperty(notes = "Owner of the building")
	private String owner;
	@Column
	@ApiModelProperty(notes = "Size of the building in square meters")
	private int size;
	@Column
	@ApiModelProperty(notes = "Building market value")
	private BigDecimal marketValue;
	@Column
	@ApiModelProperty(notes = "Type of the building: apartment, house, industrial")
	private String type;

	public Building() {
	}
	
	public Building(String address, String owner, int size, BigDecimal marketValue, String type) {
		this.address = address;
		this.owner = owner;
		this.size = size;
		this.marketValue = marketValue;
		this.type = type;
	}
	
	public String getAddress() {
		return address;
	}

	public String getOwner() {
		return owner;
	}

	public int getSize() {
		return size;
	}

	public BigDecimal getMarketValue() {
		return marketValue;
	}

	public String getType() {
		return type;
	}	
}
