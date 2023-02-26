package com.do8.weatherapi.controller;

import com.do8.weatherapi.domain.Weather;
import com.do8.weatherapi.service.WeatherService;
import com.do8.weatherapi.vo.TodayWeatherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class WeatherController {

    private final WeatherService weatherService ;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/today/all")
    @ResponseBody
    public List<TodayWeatherVO> getTodayAllWeather(){
        String nDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<Weather> weathers = weatherService.findByDate(nDate);

        if (weathers.isEmpty()){
            weatherService.saveWeatherData();
            weathers = weatherService.findByDate(nDate);
//			System.out.println("2(DB적재): "+today.size());
        }

        //weathers = weatherService.findByDate(nDate);

        List<TodayWeatherVO> res = new ArrayList<>();

        for (Weather w: weathers){
            TodayWeatherVO t = new TodayWeatherVO(w);
            res.add(t);
        }

        return res;
    }

    @GetMapping("/weather/all")
    @ResponseBody
    public List<Weather> getAllWeather(){
        List<Weather> weathers = weatherService.findAllWeathers();

        if (weathers.isEmpty()){
            weatherService.saveWeatherData();
            weathers = weatherService.findAllWeathers();
//			System.out.println("(DB적재): "+today.size());
        }

        //weathers = weatherService.findAllWeathers();

        return weathers;
    }

    @GetMapping("/weather/today/now")
    @ResponseBody
    public TodayWeatherVO getTodayWeather(){
        String nDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nTime = "";

        if (LocalDateTime.now().getHour()<5){
            nTime = "0500";
        }else{
            nTime = LocalDateTime.now().getHour()+"00";
        }

        List<Weather> today = weatherService.findWeatherByDateTime(nDate,nTime);

        if (today.isEmpty()){
            weatherService.saveWeatherData();
            today = weatherService.findWeatherByDateTime(nDate,nTime);
//			System.out.println("(DB적재): "+today.size());
        }

        //today = weatherService.findWeatherByDateTime(nDate,nTime);

        TodayWeatherVO res = new TodayWeatherVO(today.get(0));

        return res;
    }
}
