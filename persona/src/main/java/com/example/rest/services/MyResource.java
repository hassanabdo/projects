package com.example.rest.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.rest.controller.Controller;
import com.example.rest.controller.DataInitializer;
import com.example.rest.model.AddFanResponse;
import com.example.rest.model.Persona;
import com.example.rest.model.UpdateFanResponse;
import com.example.rest.utils.RequestType;

@Path("/system")
public class MyResource {

	@POST
	@Path("autoBest")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Persona> getPersonaTwitter() {
		System.out.println("get persona SVD");
		return Controller.getPersona(0, RequestType.SVD);
	}

	@POST
	@Path("allSocial")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Persona> getPersonaAllSocial(@FormParam("num") long n) {
		System.out.println("get persona ALL_SOCIAL");
		return Controller.getPersona((int) n, RequestType.NMF);
	}
	
	
	@POST
	@Path("run")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public boolean run(@FormParam("isCityKey") long isCityKey) {
		System.out.println("isCityKey "+isCityKey);
		if (isCityKey == 0) {
			return DataInitializer.collectData(false);
		}
		return DataInitializer.collectData(true);
	}
	
	@POST
	@Path("addFan")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public AddFanResponse addFan(@FormParam("facebook") String facebook, 
								@FormParam("twitter") String twitter, 
								@FormParam("country") long country, 
								@FormParam("city") long city,
								@FormParam("gender") long gender,
								@FormParam("age") long age) {
		System.out.println("addFan "+facebook+" "+twitter);
		return Controller.addFan(facebook, twitter, country, city, gender, age);
	}
	
	@POST
	@Path("updateFan")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateFanResponse updateFan(@FormParam("facebook") String facebook,
			@FormParam("twitter") String twitter,
			@FormParam("pos") long pos,
			@FormParam("neg") long neg) {
		
		return Controller.updateFan(facebook, twitter, pos, neg);
	}
	
	
	
}
