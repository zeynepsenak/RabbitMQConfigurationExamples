import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${example.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${example.rabbitmq.queue.a}")
    private String queueNameA;
    
    @Value("${example.rabbitmq.queue.b}")
    private String queueNameB;
  
    @Value("${example.rabbitmq.queue.all}")
    private String allQueue;

    @Value("${example.rabbitmq.routingkey.a}")
    private String routingKeyA;
    
    @Value("${example.rabbitmq.routingkey.a}")
    private String routingKeyB;

    @Bean
    public Queue queueA() {
        return new Queue(queueNameA, false);
    }
    
    @Bean
    public Queue queueB() {
        return new Queue(queueNameB, false);
    }
  
    @Bean
	  public Queue allQueue() {
		  return new Queue(allQueue, false);
	  }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding bindingA(final Queue queueA, final TopicExchange topicExchange){
        return BindingBuilder.bind(queueA).to(topicExchange).with(routingKeyA);
    }
    
    @Bean
    public Binding bindingB(final Queue queueB, final TopicExchange topicExchange){
        return BindingBuilder.bind(queueB).to(topicExchange).with(routingKeyB);
    }
  
    @Bean
	  public Binding allBinding(Queue allQueue, TopicExchange topicExchange) {
		  return BindingBuilder.bind(allQueue).to(topicExchange).with("queue.*");
	  }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }
}
