package com.do8.weatherapi.repository;

import com.do8.weatherapi.domain.Region;
import com.do8.weatherapi.domain.Weather;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    Region save(Region region);
    Optional<Region> findById(Long id);
    Optional<Region> findByDong(String dong);

    List<Region> findAll();
}
