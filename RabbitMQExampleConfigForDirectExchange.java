import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding bindingA(final Queue queueA, final DirectExchange directExchange){
        return BindingBuilder.bind(queueA).to(directExchange).with(routingKeyA);
    }
    
    @Bean
    public Binding bindingB(final Queue queueB, final DirectExchange directExchange){
        return BindingBuilder.bind(queueB).to(directExchange).with(routingKeyB);
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
