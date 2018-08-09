package com.redhat.rest.example.demorest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRestApplication.class, args);
	}

	// Serves as 'remote URL' for the Camel invocaton
	@GetMapping(path = "remoteURL")
	public ResponseEntity<?> getData(@RequestParam("code") String code, @RequestParam("name") String name) {
		return ResponseEntity.status(HttpStatus.OK).body("Code: " + code + " Name: " + name);
	}


	@Bean
	public RouteBuilder routeBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				restConfiguration()
						.component("servlet")
						.bindingMode(RestBindingMode.auto)
						.producerComponent("http4").host("localhost:8080");

				rest("/localURL").get().to("rest:get:remoteURL?bridgeEndpoint=true&host=localhost:8080&queryParameters=code=foo&name=bar");

			}
		};
	}
}
