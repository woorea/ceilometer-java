package org.openstack.ceilometer.api;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.json.JsonJacksonBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

	public static final String CONFIG_FILE_PATH = "/etc/ceilometer/ceilometer-api.properties";

	public static void main(String[] args) throws Exception {
		
		File propertiesFile = new File(CONFIG_FILE_PATH);
		
		Properties properties = new Properties();
		if(!propertiesFile.exists()) {
			System.out.println(String.format("%s not found, using default values", CONFIG_FILE_PATH));
		} else {
			properties.load(new FileInputStream(propertiesFile));
		}
		
		ResourceConfig config = new ResourceConfig()
			//.packages("org.openstack.ceilometer.api")
			.addClasses(CeilometerApiV1.class)
			.addBinders(new JsonJacksonBinder())
			.addBinders(new MongoDbBinder());
			//.addModules(new JsonJacksonModule())
			//.addSingletons(api);
		
		URI baseUri = URI.create(properties.getProperty("base_uri","http://localhost:8989/")) ;
		
		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
		
		System.out.println(String.format("Application started.%nTry out %s%nHit enter to stop it...", baseUri));
        System.in.read();
        server.stop();

	}

}
