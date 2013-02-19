package com.github.dandelion.datatables.entity;


/**
 * A typical Address entity.
 * 
 * @author tduchateau
 */
public class Address {

	private Long id;
	private String street;
	private Town town;

	public Address() {

	}

	public Address(String street) {
		this.street = street;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	

}