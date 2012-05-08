package org.openstack.ceilometer.counters;
import java.math.BigDecimal;

import org.openstack.ceilometer.CeilometerAgent;
import org.openstack.ceilometer.Counter;
import org.openstack.ceilometer.Usage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;


public class InstanceCounter extends Counter {
	
	private static final String EXCHANGE_NAME = "nova";

	private static final String EXCHANGE_TYPE = "topic";

	private static final String ROUTING_KEY = "notifications.#";

	public void run() {
		
		System.out.println("[Starting Instance Counter] ...");
		
		try {
			
			Connection connection = CeilometerAgent.RABBITMQ_CONNECTION_FACTORY.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
			String queueName = channel.queueDeclare().getQueue();

			channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
			
			System.out.println(" [Instance Counter] waiting for messages ...");
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			while (true) {
				try {
					QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					String message = new String(delivery.getBody());
					String routingKey = delivery.getEnvelope().getRoutingKey();
					
					System.out.println(" [Instance Counter] Received '" + routingKey + "':'" + message + "'");
					
					JsonObject jsonObject = CeilometerAgent.GSON.fromJson(message, JsonElement.class).getAsJsonObject();
					String eventType = jsonObject.get("event_type").getAsString();
					
					if("compute.instance.create.end".equals(eventType) || "compute.instance.delete.end".equals(eventType)) {
						JsonObject payload = jsonObject.get("payload").getAsJsonObject();
						
						Usage usage = new Usage();
						usage.setId(jsonObject.get("message_id").getAsString());
						usage.setSource(jsonObject.get("publisher_id").getAsString());
						usage.setUserId(payload.get("user_id").getAsString());
						usage.setProjectId(payload.get("tenant_id").getAsString());
						usage.setResourceId(payload.get("instance_id").getAsString());
						usage.setCounterType("instance");
						usage.setCounterVolume(new BigDecimal("0.0"));
						usage.setCounterDuration(new BigDecimal("0.0"));
						usage.setCounterTimestamp(System.currentTimeMillis());
						usage.setPayload("{}");
						usage.setSignature("");
						write("instance",usage);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
