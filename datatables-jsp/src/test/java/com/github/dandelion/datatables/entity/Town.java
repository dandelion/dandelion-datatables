package com.github.dandelion.datatables.entity;


/**
 * A typical Town entity.
 * 
 * @author tduchateau
 */
public class Town {

	private Long id;
	private String name;
	private String postcode;

	public Town() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Town(String label) {
		this.name = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String label) {
		this.name = label;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}