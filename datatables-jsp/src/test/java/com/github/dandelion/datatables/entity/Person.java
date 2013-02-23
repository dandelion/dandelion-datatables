package com.github.dandelion.datatables.entity;


/**
 * A typical Person entity.
 * 
 * @author tduchateau
 */
public class Person {

	private Long id;
	private String firstName;
	private String lastName;
	private String mail;
	private Address address;

	public Person() {

	}

	public Person(Long id, String firstName, String lastName, String mail, Address address){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.address = address;
	}
	public Person(Long id, String firstName, String lastName, String mail) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", mail=" + mail + ", address=" + address + "]";
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}