package com.example.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Region {
	
	@JsonProperty("Ethnicity")
	private String Ethnicity;
	
	@JsonProperty("Countries")
    private String[] Countries;

    
    public Region() {
		super();
	}

	public String[] getCountries ()
    {
        return Countries;
    }

    public void setCountries (String[] Countries)
    {
        this.Countries = Countries;
    }

    public String getEthnicity ()
    {
        return Ethnicity;
    }

    public void setEthnicity (String Ethnicity)
    {
        this.Ethnicity = Ethnicity;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Countries = "+Countries+", Ethnicity = "+Ethnicity+"]";
    }
}