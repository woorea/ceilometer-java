package org.openstack.ceilometer;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.MediaSize.Other;

import org.libvirt.Connect;
import org.openstack.ceilometer.counters.CpuCounter;
import org.openstack.ceilometer.counters.InstanceCounter;

import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;


public class CeilometerAgent {
	
	public static final Properties CONFIG_PROPERTIES = new Properties();
	
	public static final Gson GSON = new Gson();
	
	public static final ConnectionFactory RABBITMQ_CONNECTION_FACTORY = new ConnectionFactory();
	
	private static InstanceCounter instanceCounter;
	
	private static CpuCounter cpuCounter;
	
	public static File outputDir = new File(CeilometerAgent.CONFIG_PROPERTIES.getProperty("output.dir", "/tmp/ceilometer"));

	public static void main(String[] args) throws Exception {
		
		File propertiesFile = new File("ceilometer.properties");
		propertiesFile.createNewFile();

		CONFIG_PROPERTIES.load(new FileInputStream(propertiesFile));
		
		RABBITMQ_CONNECTION_FACTORY.setHost(CONFIG_PROPERTIES.getProperty("rabbitmq.host", "localhost"));
		RABBITMQ_CONNECTION_FACTORY.setUsername(CONFIG_PROPERTIES.getProperty("rabbitmq.username", "woorea"));
		RABBITMQ_CONNECTION_FACTORY.setPassword(CONFIG_PROPERTIES.getProperty("rabbitmq.password", "secret0"));
		
		String libvirtUri = CONFIG_PROPERTIES.getProperty("libvirt.uri", "qemu:///system");
		
		Connect connect = new Connect(libvirtUri, true);
		
		instanceCounter = new InstanceCounter();
		
		cpuCounter = new CpuCounter(connect);
		
		outputDir.mkdirs();
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
		
		executorService.execute(instanceCounter);
		
		scheduledExecutorService.scheduleAtFixedRate(cpuCounter, 0, 10, TimeUnit.SECONDS);
		
	}
	
}
