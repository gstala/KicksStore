package com.stala.grzegorz.model;

import javax.persistence.*;

@Entity
public class UserShipping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_user_shipping")
	private Long id;
	private String userShippingName;
	private String userShippingStreet;
	private String userShippingCity;
	private String userShippingCountry;
	private String userShippingZipcode;
	private boolean userShippingDefault;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUserShippingName() {
		return userShippingName;
	}


	public void setUserShippingName(String userShippingName) {
		this.userShippingName = userShippingName;
	}


	public String getUserShippingStreet() {
		return userShippingStreet;
	}


	public void setUserShippingStreet(String userShippingStreet) {
		this.userShippingStreet = userShippingStreet;
	}


	public String getUserShippingCity() {
		return userShippingCity;
	}


	public void setUserShippingCity(String userShippingCity) {
		this.userShippingCity = userShippingCity;
	}


	public String getUserShippingCountry() {
		return userShippingCountry;
	}


	public void setUserShippingCountry(String userShippingCountry) {
		this.userShippingCountry = userShippingCountry;
	}


	public String getUserShippingZipcode() {
		return userShippingZipcode;
	}


	public void setUserShippingZipcode(String userShippingZipcode) {
		this.userShippingZipcode = userShippingZipcode;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public boolean isUserShippingDefault() {
		return userShippingDefault;
	}


	public void setUserShippingDefault(boolean userShippingDefault) {
		this.userShippingDefault = userShippingDefault;
	}
	
	
}
