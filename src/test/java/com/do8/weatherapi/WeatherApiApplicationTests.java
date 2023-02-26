package com.do8.weatherapi;

import com.do8.weatherapi.domain.Weather;
import com.do8.weatherapi.repository.WeatherRepository;
import com.do8.weatherapi.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeatherApiApplicationTests {

	@Autowired
	WeatherService weatherService;
	@Autowired
	WeatherRepository weatherRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void apiTest() throws IOException {

		String nDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String nTime = LocalDateTime.now().getHour()+"00";

		List<Weather> today = weatherService.findWeatherByDateTime(nDate,nTime);

		System.out.println("1: "+today.size());

		if (today.isEmpty()){
			today = weatherService.saveWeatherData();
//			System.out.println("2(DB적재): "+today.size());
		}


		today = weatherService.findWeatherByDateTime(nDate,nTime);

		System.out.println("3(find): "+today.get(0).getSkyState());


		//assertThat(pre).isEqualTo(pst);
	}

}
