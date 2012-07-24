package org.openstack.ceilometer.collector;

import org.openstack.ceilometer.collector.handlers.ConsoleMeterEventListener;
import org.openstack.ceilometer.collector.handlers.FloatingIpNotificationHandler;
import org.openstack.ceilometer.collector.handlers.InstanceNotificationHandler;
import org.openstack.ceilometer.collector.handlers.MeterEventListener;
import org.openstack.ceilometer.collector.handlers.MeterEventMessageHandler;
import org.openstack.ceilometer.collector.handlers.NotificationMessageHandler;
import org.openstack.ceilometer.mongodb.MongoDbMeterEventListener;
import org.openstack.ceilometer.rabbitmq.RabbitMqConsumer;

public abstract class Collector {

	public static void main(String[] args) {
		
		MongoDbMeterEventListener mongoDb = new MongoDbMeterEventListener();
		mongoDb.start();
		
		MeterEventListener console = new ConsoleMeterEventListener();
		
		RabbitMqConsumer notificationsConsumer = new RabbitMqConsumer();
		notificationsConsumer.setHost("192.168.1.38");
		notificationsConsumer.setPassword("secret0");
		notificationsConsumer.setExchangeName("nova");
		notificationsConsumer.setRoutingKey("notifications.#");
		notificationsConsumer.setQueueName("notifications_info");
		NotificationMessageHandler notificationMessageHandler = new NotificationMessageHandler();
		notificationMessageHandler.add(new InstanceNotificationHandler());
		notificationMessageHandler.add(new FloatingIpNotificationHandler());
		notificationsConsumer.setMessageHandler(notificationMessageHandler);
		notificationsConsumer.add(mongoDb);
		notificationsConsumer.add(console);
		
		RabbitMqConsumer computeAgentConsumer = new RabbitMqConsumer();
		computeAgentConsumer.setHost("192.168.1.38");
		computeAgentConsumer.setPassword("secret0");
		computeAgentConsumer.setExchangeName("metering");
		computeAgentConsumer.setRoutingKey("ceilometer.#");
		computeAgentConsumer.setQueueName("ceilometer_collector");
		computeAgentConsumer.setMessageHandler(new MeterEventMessageHandler());
		computeAgentConsumer.add(mongoDb);
		computeAgentConsumer.add(console);
		
		new Thread(notificationsConsumer, "notificationsConsumer").start();
		new Thread(computeAgentConsumer, "computeAgentConsumer").start();
		
	}

}
