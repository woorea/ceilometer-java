package org.openstack.ceilometer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;



public abstract class Counter implements Runnable {
	
	protected void write(String counterName, Usage usage) throws IOException {
		IOUtils.write(CeilometerAgent.GSON.toJson(usage), new FileOutputStream(new File(CeilometerAgent.outputDir, counterName+"-"+System.currentTimeMillis()+".json")));
		
	}
	
}
