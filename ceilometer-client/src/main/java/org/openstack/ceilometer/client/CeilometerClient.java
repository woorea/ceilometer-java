package org.openstack.ceilometer.client;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;

public class CeilometerClient {

	public static void main(String[] args) {
		Client client = ClientFactory.newClient();
		WebTarget endpoint = client.target("http://localhost:8989/ceilometer/v1");
		endpoint.configuration().register(new LoggingFilter(Logger.getLogger("ceilometer"), true));
		
		//version
		Response response = endpoint.request(MediaType.APPLICATION_XML).get();
		response = endpoint.path("extensions").request(MediaType.APPLICATION_XML).get();
		response = endpoint.path("extensions/alias").request(MediaType.APPLICATION_XML).get();
		response = endpoint.path("sources").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/list/resources").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/users").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/users/2").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/users/2/instance").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/users/2/instance/volume").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/users/2/instance/duration").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/users/2/resources").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/projects").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/projects/1").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/projects/1/instance").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/projects/1/instance/volume").request(MediaType.APPLICATION_JSON).get();
		response = endpoint.path("sources/source/projects/1/instance/duration").request(MediaType.APPLICATION_JSON).get();
		
		System.exit(0);
	
	}
	
}
