package com.io.github.msj.msinscricao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsInscricaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsInscricaoApplication.class, args);
	}

}
