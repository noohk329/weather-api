package com.do8.weatherapi.vo;

import com.do8.weatherapi.domain.Weather;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodayWeatherVO {
    private String 위치;

    private String 날짜;
    private String 현재시간;

    private String 현재_날씨;

    private String 최고_기온;
    private String 최저_기온;

    private String 현재_기온;
    private String 현재_습도;
    private String 강수_확률;


    public TodayWeatherVO(Weather weather) {
        this.위치 = this.getRegion(weather.getnX(), weather.getnY());
        this.날짜=weather.getDate();
        this.현재시간=weather.getTime();

        this.최고_기온 = weather.getTempHigh()+"°C";
        this.최저_기온 = weather.getTempLow()+"°C";
        this.현재_기온 = weather.getTmp()+"°C";
        this.현재_습도 = weather.getHum();
        this.강수_확률 = weather.getRainPer()+"%";

        if (weather.getPtyCode().equals("0")){
            this.현재_날씨 = weather.getSkyState();
        }else{
            this.현재_날씨 = weather.getPtyState();
        }

    }

    private String getRegion (int nX, int nY){
        String res = "";

        if (nX == 62 && nY == 128){
            res = "서울시 노원구 공릉2동";
        }

        return res;
    }




}
