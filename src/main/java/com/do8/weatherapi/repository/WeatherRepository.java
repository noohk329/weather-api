package com.do8.weatherapi.repository;

import com.do8.weatherapi.domain.Weather;

import java.util.List;
import java.util.Optional;

public interface WeatherRepository {
    Weather save(Weather weather);
    Optional<Weather> findById(Long id);
    List<Weather> findByDate(String date);
    List<Weather> findWeatherByDateTime(String date, String time);
    List<Weather> findAllWeathers();
}
