package com.example.rest.model;

public class Persona {
	private int id;
	private String name;
	private String age;
	private String gender;
	private String country;
	private String mostTopic;
	private String leastTopic;
	private String photo;
	private String city;
	
	private String fbPos;
	private String fbNeg;
	private String twPos;
	private String twNeg;
	
	private int personaSize;
	
	private String personaSizePercent;
	
	
	
	public String getPersonaSizePercent() {
		return personaSizePercent;
	}

	public void setPersonaSizePercent(String personaSizePercent) {
		this.personaSizePercent = personaSizePercent;
	}

	public int getPersonaSize() {
		return personaSize;
	}

	public void setPersonaSize(int personaSize) {
		this.personaSize = personaSize;
	}

	private String personaPower;
	
	
	
	
	
	
	public String getFbPos() {
		return fbPos;
	}

	public void setFbPos(String fbPos) {
		this.fbPos = fbPos;
	}

	public String getFbNeg() {
		return fbNeg;
	}

	public void setFbNeg(String fbNeg) {
		this.fbNeg = fbNeg;
	}

	public String getTwPos() {
		return twPos;
	}

	public void setTwPos(String twPos) {
		this.twPos = twPos;
	}

	public String getTwNeg() {
		return twNeg;
	}

	public void setTwNeg(String twNeg) {
		this.twNeg = twNeg;
	}

	public String getPersonaPower() {
		return personaPower;
	}

	public void setPersonaPower(String personaPower) {
		this.personaPower = personaPower;
	}

	public Persona() {
		super();
	}
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getAge() {
		return age;
	}



	public void setAge(String age) {
		this.age = age;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getMostTopic() {
		return mostTopic;
	}



	public void setMostTopic(String mostTopic) {
		this.mostTopic = mostTopic;
	}



	public String getLeastTopic() {
		return leastTopic;
	}



	public void setLeastTopic(String leastTopic) {
		this.leastTopic = leastTopic;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
	
	

}
