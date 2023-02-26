package com.do8.weatherapi;

import com.do8.weatherapi.domain.Weather;
import com.do8.weatherapi.repository.JdbcWeatherRepository;
import com.do8.weatherapi.repository.JpaWeatherRepository;
import com.do8.weatherapi.repository.WeatherRepository;
import com.do8.weatherapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Configuration
public class SpringConfig {

//    private final DataSource dataSource;
//
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    /* return new JpaMemberRepository(em) */
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em){
        this.em =em;
    }

    @Bean
    public WeatherRepository weatherRepository() {
        return new JpaWeatherRepository(em);
    }

    @Bean
    public WeatherService memberService() {
        return new WeatherService(weatherRepository());
    }

}
