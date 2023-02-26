package com.do8.weatherapi;

import com.do8.weatherapi.domain.Weather;
import com.do8.weatherapi.repository.JdbcWeatherRepository;
import com.do8.weatherapi.repository.WeatherRepository;
import com.do8.weatherapi.service.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.io.IOException;

@SpringBootApplication
public class WeatherApiApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WeatherApiApplication.class, args);
	}

}
