import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Main {
		
	public static void main(String[] args) throws IOException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setTopologyRecoveryEnabled(true);
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare("test330", "direct");
		
		
		Connection connection2 = connectionFactory.newConnection();
		Channel channel2 = connection2.createChannel();
		channel2.queueDeclare("test330", true, false, false, null);
		channel2.queueBind("test330", "test330", "");
		
		channel2.basicConsume("test330", false, new DefaultConsumer(channel2) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {				
				System.out.println("Message: " + new String(body));
				this.getChannel().basicAck(envelope.getDeliveryTag(), false);
			}
		});
		
		int i = 0;
		while(true) {
			i++;
			try {
				channel.basicPublish("test330", "", null, ("" + i).getBytes());
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
