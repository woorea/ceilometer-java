package org.openstack.ceilometer.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.openstack.ceilometer.model.MeterEvent;

@Path("v1")
@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
public class CeilometerApiV1 {
	
	private MongoDbService service;
	
	public CeilometerApiV1(MongoDbService service) {
		this.service = service;
	}
	
	@GET
	public String version() {
		return "version";
	}
	
	@GET
	@Path("/extensions")
	@Produces(MediaType.TEXT_PLAIN)
	public List<String> getExtensions() {
		return new ArrayList<String>();
	}
	
	@GET
	@Path("/extensions/{alias}")
	public String getExtension(@PathParam("alias") String alias) {
		return alias;
	}
	
	@GET
	@Path("/sources")
	public List<String> getSources(@QueryParam("start") String start, @QueryParam("end") String end) {
		return new ArrayList<String>();
	}
	
	@GET
	@Path("/sources/{source}/list/resources")
	public List<Map<String, Object>> getResources(@PathParam("source") String source, @QueryParam("start") String start, @QueryParam("end") String end) {
		return service.getResources(source, null, null);
	}
	
	@GET
	@Path("/sources/{source}/users")
	public Object getUsers(@PathParam("source") String source, @QueryParam("start") String start, @QueryParam("end") String end) {
		return service.getUsers(source);
	}
	
	@GET
	@Path("/sources/{source}/users/{user}")
	public List<MeterEvent> getRawEventsByUser(@PathParam("source") String source, @PathParam("user") String user, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setUser(user);
		return service.getRawEvents(filter);
	}
	
	@GET
	@Path("/sources/{source}/users/{user}/{meter}")
	public List<MeterEvent> getRawEventsByUserAndMeter(@PathParam("source") String source, @PathParam("user") String user, @PathParam("meter") String meter, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setUser(user);
		filter.setMeter(meter);
		return service.getRawEvents(filter);
	}
	
	@GET
	@Path("/sources/{source}/users/{user}/{meter}/volume")
	public List<Map<String, Object>> getVolumeSumByUserAndMeter(@PathParam("source") String source, @PathParam("user") String user, @PathParam("meter") String meter, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setUser(user);
		filter.setMeter(meter);
		return service.getVolumeSum(filter);
	}
	
	@GET
	@Path("/sources/{source}/users/{user}/{meter}/duration")
	public List<Map<String, Object>> getDurationSumByUserAndMeter(@PathParam("source") String source, @PathParam("user") String user, @PathParam("meter") String meter, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setUser(user);
		filter.setMeter(meter);
		return service.getDurationSum(filter);
	}
	
	@GET
	@Path("/sources/{source}/users/{user}/resources")
	public List<Map<String, Object>> getResourcesByUser(@PathParam("source") String source, @PathParam("user") String user, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setUser(user);
		System.out.println(service.getResources(source, null, user));
		return service.getResources(source, null, user);
	}
	
	
	@GET
	@Path("/sources/{source}/projects")
	public Object getProjects(@PathParam("source") String source, @QueryParam("start") String start, @QueryParam("end") String end) {
		return service.getProjects(source);
	}
	
	@GET
	@Path("/sources/{source}/projects/{project}")
	public List<MeterEvent> getRawEventsByProject(@PathParam("source") String source, @PathParam("project") String project, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setProject(project);
		return service.getRawEvents(filter);
	}
	
	@GET
	@Path("/sources/{source}/projects/{project}/{meter}")
	public List<MeterEvent> getRawEventsByProjectAndMeter(@PathParam("source") String source, @PathParam("project") String project, @PathParam("meter") String meter, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setProject(project);
		return service.getRawEvents(filter);
	}
	
	@GET
	@Path("/sources/{source}/projects/{project}/{meter}/volume")
	public List<Map<String, Object>> getVolumeSumByProjectAndMeter(@PathParam("source") String source, @PathParam("project") String project, @PathParam("meter") String meter, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setProject(project);
		filter.setMeter(meter);
		return service.getVolumeSum(filter);
	}
	
	@GET
	@Path("/sources/{source}/projects/{project}/{meter}/duration")
	public List<Map<String, Object>> getDurationSumByProjectAndMeter(@PathParam("source") String source, @PathParam("project") String project, @PathParam("meter") String meter, @QueryParam("start") String start, @QueryParam("end") String end) {
		EventFilter filter = new EventFilter();
		//filter.setSource(source);
		filter.setProject(project);
		filter.setMeter(meter);
		return service.getDurationSum(filter);
	}

}
