package com.do8.weatherapi.service;

import com.do8.weatherapi.domain.Weather;
import com.do8.weatherapi.repository.WeatherRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }



    /**
     * 기상청 예보 api 호출 - 지역 조회 X ver
     * */
    public List<Weather> getApiData() throws IOException {

        List<Weather> weatherList = new ArrayList<>();

        String servicekey = "KIPCztE7n%2B2A41eMsgmwYNJ7Z4cZo9%2FQgevdSKUsAWH30gSpwOFtnKwP7BuzoYGsJ8QnDX3taOuyFAc17t2POA%3D%3D";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fDate = DateTimeFormatter.ofPattern("yyyyMMdd");

        String nDate = now.format(fDate);
        String baseTime = "0500";

        // 서울시 노원구 공릉2동
        int nX = 62;
        int nY = 128;

        System.out.println("## 조회 일자 :"+nDate);
        System.out.println("## 기준 시간 :"+baseTime);

        // api 종류
        // getUltraSrtFcst -> 초단기예보조회
        // getVilageFcst -> 단기예보조회
        String apiapp = "getVilageFcst"; // 단기예보조회

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"+apiapp); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+servicekey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("300", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(nDate, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(String.valueOf(nX), "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(String.valueOf(nY), "UTF-8")); /*예보지점의 Y 좌표값*/

        URL url = new URL(urlBuilder.toString());
        System.out.println("Request URL: " + url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        JSONObject jObject = new JSONObject(sb.toString());
        JSONObject response = jObject.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray jArray = items.getJSONArray("item");
        //int nX, int nY, String date, String time, String skyCode, String ptyCode, String skyState, String ptyState, String tempHigh, String tempLow, String hum

        Weather weather = new Weather();

        String tHigh = "";
        String tLow = "";

        for(int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String category = obj.getString("category");
            String fcstDate = obj.getString("fcstDate");
            String fcstTime = obj.getString("fcstTime");
            String obsrValue = obj.getString("fcstValue");

            if (fcstDate.equals(nDate)){ // 해당 일자에 대한 예보일때만
                weather.setDate(nDate);
                // System.out.println(weather.getTime());
                if (weather.getTime() == null){
                    weather.setnX(nX);
                    weather.setnY(nY);
                    weather.setTime(fcstTime);
                } else if (!weather.getTime().equals(fcstTime)){ // 다음 시간
                    weatherList.add(weather);
                    weather = new Weather();
                    weather.setnX(nX);
                    weather.setnY(nY);
                    weather.setTime(fcstTime);
                }
                switch (category) {
                    case "POP": // 강수 확률
                        weather.setRainPer(obsrValue);
                        break;
                    case "PTY": // 강수 형태
                        weather.setPtyCode(obsrValue);
                        break;
                    case "SKY": // 하늘 상태
                        weather.setSkyCode(obsrValue);
                        break;
                    case "REH": // 습도
                        weather.setHum((obsrValue));
                        break;
                    case "TMP": // 온도
                        // 최고, 최저기온
                        if (tLow.isEmpty()){
                            tLow = obsrValue;
                        } else if (!tLow.isEmpty()){
                            if (Double.parseDouble(tLow)>Double.parseDouble(obsrValue)){
                                tLow = obsrValue;
                            }
                        }
                        if (tHigh.isEmpty()){
                            tHigh = obsrValue;
                        } else if (!tHigh.isEmpty()){
                            if (Double.parseDouble(tHigh)<Double.parseDouble(obsrValue)){
                                tHigh = obsrValue;
                            }
                        }
                        weather.setTmp((obsrValue));
                        break;
                }

            }

        }
        weatherList.add(weather);

        for (Weather w: weatherList){
            w.setTempLow(tLow);
            w.setTempHigh(tHigh);
            w.setSkyState(setSkyState(w.getSkyCode()));
            w.setPtyState(setPtyState(w.getPtyCode()));
        }

        return weatherList;
    }

    private String setSkyState(String code){
        String res = "";
        switch (code){
            case "1":
                res = "맑음";
                break;
            case "3":
                res = "구름많음";
                break;
            case "4":
                res = "흐림";
                break;
        }
        return res;
    }

    private String setPtyState(String code){
        String res = "";

        switch (code){
            case "0":
                res = "없음";
                break;
            case "1":
                res = "비";
                break;
            case "2":
                res = "비/눈";
                break;
            case "3":
                res = "눈";
                break;
            case "4":
                res = "소나기";
                break;
        }
        return res;
    }

    /**
     * 요청하는 날짜에 해당하는 데이터 적재 (DB)
     * */
    public List<Weather> saveWeatherData (){
        List<Weather> weatherList = new ArrayList<>();

        try {
            weatherList = getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Weather w: weatherList) {
            weatherRepository.save(w);
        }

        return weatherList;
    }

    /**
     * 전체 데이터 조회
     * @return List<Weather>
     */
    public List<Weather> findAllWeathers(){
        return weatherRepository.findAllWeathers();
    }

    /**
     * 날짜, 시간로 날씨 조회
     * @return Weather
     */
    public List<Weather> findWeatherByDateTime(String date, String time){
        List<Weather> res = weatherRepository.findWeatherByDateTime(date, time);
        // System.out.println(res.size());
        return weatherRepository.findWeatherByDateTime(date, time);
    }

    /**
     * 날짜로 회원 조회
     * @return Optional<Member>
     */
    public List<Weather> findByDate(String date){
        return weatherRepository.findByDate(date);
    }


}
