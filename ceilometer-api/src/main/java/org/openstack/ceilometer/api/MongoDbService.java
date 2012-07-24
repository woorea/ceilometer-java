package org.openstack.ceilometer.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openstack.ceilometer.model.MeterEvent;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;

public class MongoDbService {

	//# JavaScript function for doing map-reduce to get a counter volume total.
	private String mapCounterVolume = "function() { emit(this.resource_id, this.volume); }";
	
	//# JavaScript function for doing map-reduce to get a counter duration total.
	private String mapCounterDuration = "function() { emit(this.resource_id, this.duration); }";
	
	//# JavaScript function for doing map-reduce to get a maximum value from a range.  (from http://cookbook.mongodb.org/patterns/finding_max_and_min/)
	private String reduceMax = "function (key, values) { return Math.max.apply(Math, values); }";
	
	//# JavaScript function for doing map-reduce to get a sum.
	private String reduceSum = "function (key, values) { var total = 0; for (var i = 0; i < values.length; i++) { total += values[i]; } return total; }";
	
	private String host = "192.168.1.38";
	
	private int port = 27017;

	private String dbname = "ceilometer";
	
	private String username;
	
	private String password;
	
	private Mongo mongo;
	
	private DB db;
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void start() {
		try {
			MongoDbService service = new MongoDbService();
			service.mongo = new Mongo(host, port);
			service.db = service.mongo.getDB(dbname);
			/*
			if(!db.authenticate(username, password.toCharArray())) {
				throw new RuntimeException("auth");
			}
			*/
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}
	
	public static final DBObject createFilter(EventFilter filter) {
		
		BasicDBObjectBuilder builder = new BasicDBObjectBuilder();
		
		if(filter != null) {
			if(filter.getUser() != null) {
				builder.add("user_id", filter.getUser());
			} else if(filter.getProject() != null) {
				builder.add("project_id", filter.getProject());
			}
			if(filter.getMeter() != null) {
				builder.add("name", filter.getMeter());
			}
			if(filter.getStart() != null) {
				builder.add("start", new BasicDBObject("$gte", filter.getStart()));
			}
			if(filter.getEnd() != null) {
				builder.add("end", new BasicDBObject("$lt", filter.getEnd()));
			}
			if(filter.getResource() != null) {
				builder.add("resource_id", filter.getResource());
			}
			if(filter.getSource() != null) {
				builder.add("source", filter.getSource());
			}
		}
		
		return builder.get();
	
	}
	
	public void store(MeterEvent data) {
		//# Make sure we know about the user and project
		db.getCollection("user").update(
			new BasicDBObject("_id", data.getUserId()), 
			BasicDBObjectBuilder.start()
				.push("$addToSet")
					.add("source", data.getSource())
				.pop()
			.get(), 
			true, 
			false
		);
		db.getCollection("project").update(
			new BasicDBObject("_id", data.getProjectId()), 
			BasicDBObjectBuilder.start()
				.push("$addToSet")
					.add("source", data.getSource())
				.pop()
			.get(), 
			true, 
			false
		);
		//# Record the updated resource metadata
		long timestamp = System.currentTimeMillis();
		db.getCollection("resource").update(
			new BasicDBObject("_id", data.getResourceId()), 
			BasicDBObjectBuilder.start()
				.push("$set")
					.add("project_id", data.getProjectId())
					.add("user_id", data.getUserId())
					.add("timestamp", timestamp)
					.add("metadata", data.getMetadata())
				.pop()
				.push("$addToSet")
					.push("meter")
						.add("counter_name", data.getName())
						.add("counter_type", data.getType())
					.pop()
				.pop()
			.get(), 
			true, 
			false
		);
		
		db.getCollection("meter").insert(
			new BasicDBObjectBuilder()
				.add("source", data.getSource())
				.add("name", data.getName())
				.add("type", data.getType())
				.add("volume", data.getVolume())
				.add("project_id", data.getProjectId())
				.add("user_id", data.getUserId())
				.add("resource_id", data.getResourceId())
				.add("timestamp", data.getTimestamp())
				.add("metadata", data.getMetadata())
			.get()
		);
	}
	
	/**
	 * Return an iterable of user id strings.
	 * 
	 * @param source
	 */
	public List<?> getUsers(String source) {
		DBObject query = null;
		if(source != null) {
			query = new BasicDBObject("source", source);
		}
		return db.getCollection("user").distinct("_id");
		
	}
	
	public List<?> getProjects(String source) {
		DBObject query = null;
		if(source != null) {
			query = new BasicDBObject("source", source);
		}
		return db.getCollection("project").distinct("_id");

	}
	/**
	 * Return an iterable of dictionaries containing resource information.
	 * 
	 * @param user
	 * @param project
	 * @param source
	 */
	public List<Map<String, Object>> getResources(String source, String project, String user) {
		
		BasicDBObjectBuilder dboBuilder = new BasicDBObjectBuilder();
		
		if(source != null) {
			//dboBuilder.add("source", user);
		} 
		
		if(project != null) {
			dboBuilder.add("project_id", user);
		}

		if(user != null) {
			dboBuilder.add("user_id", user);
		}
		
		
		DBCursor dbCursor = db.getCollection("resource").find(dboBuilder.get());
		List<Map<String, Object>> resources = new ArrayList<Map<String, Object>>();
		for(DBObject dbo : dbCursor) {
			Map<String, Object> resource = new HashMap<String, Object>();
			resource.put("resource_id", dbo.get("_id"));
			resource.put("project_id", dbo.get("project_id"));
			resource.put("user_id", dbo.get("user_id"));
			resource.put("timestamp", dbo.get("timestamp"));
			resource.put("metadata", dbo.get("metadata"));
			resource.put("meter", dbo.get("meter"));
			resources.add(resource);
		}
		return resources;
	}
	
	public List<MeterEvent> getRawEvents(EventFilter eventFilter) {
		
		List<MeterEvent> events = new ArrayList<MeterEvent>();
		
		DBObject query = createFilter(eventFilter);
		
		System.out.println(query);
		
		DBCursor dbCursor = db.getCollection("meter").find(query);
		
		for(DBObject dbo : dbCursor) {
			MeterEvent m = new MeterEvent();
			m.setName((String) dbo.get("name")); 
			m.setType((String) dbo.get("type"));
			m.setResourceId((String) dbo.get("resource_id"));
			m.setVolume((Number) dbo.get("volume"));
			events.add(m);
		}
		
		return events;

	}
	
	public List<Map<String, Object>> getVolumeSum(EventFilter eventFilter) {
		
		DBObject query = createFilter(eventFilter);
		
		MapReduceOutput output = db.getCollection("meter").mapReduce(mapCounterVolume, reduceSum, null, MapReduceCommand.OutputType.INLINE, query);
		
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		for(DBObject dbo : output.results()) {
			result.put("resource_id", dbo.get("_id"));
			result.put("value", dbo.get("value"));
			results.add(result);
		}
		
		return results;

	}
	
	public List<Map<String, Object>> getVolumeMax(EventFilter eventFilter) {
		
		DBObject query = createFilter(eventFilter);
		
		MapReduceOutput output = db.getCollection("meter").mapReduce(mapCounterVolume, reduceMax, null, MapReduceCommand.OutputType.INLINE, query);
			
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		for(DBObject dbo : output.results()) {
			result.put("resource_id", dbo.get("_id"));
			result.put("value", dbo.get("value"));
			results.add(result);
		}
		
		return results;

	}
	
	public List<Map<String, Object>> getDurationSum(EventFilter eventFilter) {
		
		DBObject query = createFilter(eventFilter);
		
		MapReduceOutput output = db.getCollection("meter").mapReduce(mapCounterDuration, reduceMax, null, MapReduceCommand.OutputType.INLINE, query);
		
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		for(DBObject dbo : output.results()) {
			result.put("resource_id", dbo.get("_id"));
			result.put("value", dbo.get("value"));
			results.add(result);
		}
		
		return results;
		
	}
	
	public static void main(String[] args) {
		
		MongoDbService service = new MongoDbService();
		service.setHost("192.168.1.38");
		service.setPort(27017);
		service.setDbname("ceilometer");
		service.start();
		
		for(final String resourceId : new String[]{"resource.1","resource.2"}) {
			for(int i = 0; i < 10; i++) {
				MeterEvent m = new MeterEvent();
				m.setName("instance");
				m.setProjectId("1");
				m.setUserId("2");
				m.setResourceId(resourceId);
				m.setSource("?");
				m.setType("delta");
				m.setVolume(1);
				m.setMetadata("{\"key1\" : \"val1\"}");
				m.setTimestamp(System.currentTimeMillis());
				service.store(m);
			}
		}
		
		System.out.println(service.getUsers(null));
		System.out.println(service.getProjects(null));
		System.out.println(service.getResources(null, null, null));
		System.out.println(service.getRawEvents(new EventFilter(){{
			setUser("2");
		}}));
		/*
		System.out.println(service.getVolumeSum(new EventFilter(){{
			//setResource("3");
		}}));
		System.out.println(service.getVolumeMax(new EventFilter(){{
			//setResource("3");
		}}));
		System.out.println(service.getDurationSum(new EventFilter(){{
			//setResource("3");
		}}));
		*/
	}

}
