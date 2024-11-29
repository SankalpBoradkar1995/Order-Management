package com.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	public static final String INVENTORY_EXCHANGE = "inventory-events-exchange";
	public static final String STOCK_DEDUCTION_QUEUE = "inventory-stock-deduction-queue";
	public static final String STOCK_REVERT_QUEUE = "inventory-stock-revert-queue";
	public static final String STOCK_DEDUCTION_ROUTING_KEY = "stock.deduction";
	public static final String STOCK_REVERT_ROUTING_KEY = "stock.revert";

    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Queue stockDeductionQueue() {
        return new Queue(STOCK_DEDUCTION_QUEUE, true);
    }

    @Bean
    public Queue stockRevertQueue() {
        return new Queue(STOCK_REVERT_QUEUE, true);
    }

    @Bean
    public Binding stockDeductionBinding(Queue stockDeductionQueue, DirectExchange inventoryExchange) {
        return BindingBuilder.bind(stockDeductionQueue).to(inventoryExchange).with(STOCK_DEDUCTION_ROUTING_KEY);
    }

    @Bean
    public Binding stockRevertBinding(Queue stockRevertQueue, DirectExchange inventoryExchange) {
        return BindingBuilder.bind(stockRevertQueue).to(inventoryExchange).with(STOCK_REVERT_ROUTING_KEY);
    }

}
