package com.do8.weatherapi.repository;

import com.do8.weatherapi.domain.Region;
import com.do8.weatherapi.domain.Weather;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcRegionRepository implements RegionRepository{
    private final DataSource dataSource;

    public JdbcRegionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);

    }


    @Override
    public Region save(Region region) {
        return null;
    }

    @Override
    public Optional<Region> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Region> findByDong(String dong) {
        return Optional.empty();
    }

    @Override
    public List<Region> findAll() {
        return null;
    }
}
