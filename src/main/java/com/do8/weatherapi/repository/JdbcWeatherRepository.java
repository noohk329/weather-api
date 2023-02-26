package com.do8.weatherapi.repository;

import com.do8.weatherapi.domain.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcWeatherRepository implements WeatherRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcWeatherRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Weather save(Weather weather) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("weather").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("nX", weather.getnX());
        parameters.put("nY", weather.getnY());

        parameters.put("date", weather.getDate());
        parameters.put("time", weather.getTime());

        parameters.put("sky_code", weather.getSkyCode());
        parameters.put("pty_code", weather.getPtyCode());

        parameters.put("sky_state", weather.getSkyState());
        parameters.put("pty_state", weather.getPtyState());

        parameters.put("hum", weather.getHum());
        parameters.put("tmp", weather.getTmp());
        parameters.put("rain_per", weather.getRainPer());

        parameters.put("temp_high", weather.getTempHigh());
        parameters.put("temp_low", weather.getTempLow());

        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        weather.setId(key.longValue());

        return weather;

    }

    @Override
    public Optional<Weather> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Weather> findByDate(String date) {
        return jdbcTemplate.query("select * from weather where date = ?"
                , weatherRowMapper()
                , date)
                ;
    }

    @Override
    public List<Weather> findWeatherByDateTime(String date, String time) {
        return jdbcTemplate.query("select * from weather where date = ? and time = ?"
                , weatherRowMapper()
        , date, time)
                ;
    }

    @Override
    public List<Weather> findAllWeathers() {
        return jdbcTemplate.query("select * from weather"
                , weatherRowMapper());
    }


    private RowMapper<Weather> weatherRowMapper(){
        return (rs, rowNum) -> {
            Weather weather = new Weather();

            weather.setId(rs.getLong("id"));
            weather.setnX(rs.getInt("nX"));
            weather.setnY(rs.getInt("nY"));

            weather.setDate(rs.getString("date"));
            weather.setTime(rs.getString("time"));

            weather.setTmp(rs.getString("tmp"));
            weather.setHum(rs.getString("hum"));
            weather.setRainPer(rs.getString("rain_per"));

            weather.setTempHigh(rs.getString("temp_high"));
            weather.setTempLow(rs.getString("temp_low"));

            weather.setSkyCode(rs.getString("sky_code"));
            weather.setPtyCode(rs.getString("pty_code"));
            weather.setSkyState(rs.getString("sky_state"));
            weather.setPtyState(rs.getString("pty_state"));

            return weather;
        };
    }




}
