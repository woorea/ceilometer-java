package org.openstack.ceilometer.counters;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainInfo;
import org.libvirt.LibvirtException;
import org.openstack.ceilometer.CeilometerAgent;
import org.openstack.ceilometer.Counter;
import org.openstack.ceilometer.Usage;


public class CpuCounter extends Counter {
	
	private Connect connect;
	
	public CpuCounter(Connect connect) {
		this.connect = connect;
	}
	
	public void run() {
	
        try{
        	
        	for(int sp : connect.listDomains()) {
        		
        		Domain testDomain=connect.domainLookupByID(sp);
                
        		Map<String, Object> properties = new HashMap<String, Object>();
        		DomainInfo info = testDomain.getInfo();
        		properties.put("os_type", testDomain.getOSType());
        		properties.put("cpu_time", info.cpuTime);
        		properties.put("max_mem", info.maxMem);
        		properties.put("memory", info.memory);
        		properties.put("vcpus", info.nrVirtCpu);
        		properties.put("state", info.state);
        		
            	Usage usage = new Usage();
    			usage.setId("");
    			usage.setSource("");
    			usage.setUserId("");
    			usage.setProjectId("");
    			usage.setResourceId(testDomain.getName() + "( "+ testDomain.getID() +" )");
    			usage.setCounterType("instance");
    			usage.setCounterDuration(new BigDecimal("0.0"));
    			usage.setCounterVolume(new BigDecimal("0.0"));
    			usage.setCounterTimestamp(System.currentTimeMillis());
    			usage.setPayload(CeilometerAgent.GSON.toJson(properties));
    			usage.setSignature("");
    			write("cpu",usage);
        	
        	}
        	
        } catch (LibvirtException e){
            System.out.println("exception caught:"+e);
            System.out.println(e.getError());
        } catch (Exception e) {
			e.printStackTrace();
		}
	}

}
