package com.do8.weatherapi.domain;

import javax.persistence.*;

@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nX;
    private int nY;

    private String date;
    private String time;

    private String skyCode; // 하늘상태코드 1(맑음) ~ 10(흐림)
    private String ptyCode; // 강수형태코드 0 (없음), 1 (비)

    private String skyState;
    private String ptyState;

    private String tempHigh; // 최고 기온
    private String tempLow; // 최저 기온

    private String hum; // 습도
    private String tmp; // 기온

    private String rainPer; // 강수확률

    public Weather(){
    }

    public String getRainPer() {
        return rainPer;
    }

    public void setRainPer(String rainPer) {
        this.rainPer = rainPer;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getnX() {
        return nX;
    }

    public void setnX(int nX) {
        this.nX = nX;
    }

    public int getnY() {
        return nY;
    }

    public void setnY(int nY) {
        this.nY = nY;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSkyCode() {
        return skyCode;
    }

    public void setSkyCode(String skyCode) {
        this.skyCode = skyCode;
    }

    public String getPtyCode() {
        return ptyCode;
    }

    public void setPtyCode(String ptyCode) {
        this.ptyCode = ptyCode;
    }

    public String getSkyState() {
        return skyState;
    }

    public void setSkyState(String skyState) {
        this.skyState = skyState;
    }

    public String getPtyState() {
        return ptyState;
    }

    public void setPtyState(String ptyState) {
        this.ptyState = ptyState;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

}
