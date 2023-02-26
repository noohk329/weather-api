package com.do8.weatherapi.repository;

import com.do8.weatherapi.domain.Weather;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaWeatherRepository implements WeatherRepository{
    private final EntityManager em;

    public JpaWeatherRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Weather save(Weather weather) {
        em.persist(weather);
        return weather;
    }

    @Override
    public Optional<Weather> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Weather> findByDate(String date) {
        List<Weather> result = em.createQuery("select m from Weather m where m.date = :date", Weather.class)
                .setParameter("date", date)
                .getResultList();

        return result;
    }

    @Override
    public List<Weather> findWeatherByDateTime(String date, String time) {
        List<Weather> result = em.createQuery("select m from Weather m where m.date = :date and m.time = :time", Weather.class)
                .setParameter("date", date)
                .setParameter("time", time)
                .getResultList();

        return result;
    }

    @Override
    public List<Weather> findAllWeathers() {
        return em.createQuery("select m from Weather m", Weather.class)
                .getResultList();
    }
}
