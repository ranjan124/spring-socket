package com.shieldteq.trade;

import org.springframework.boot.SpringApplication;

public class TestTradeApplication {

	public static void main(String[] args) {
		SpringApplication.from(TradeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
