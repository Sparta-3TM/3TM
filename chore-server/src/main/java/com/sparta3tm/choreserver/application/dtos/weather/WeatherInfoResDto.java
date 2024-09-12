package com.sparta3tm.choreserver.application.dtos.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoResDto implements Serializable {

    private WeatherResponse response;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherResponse{
        private WeatherResponseHeader header;
        private WeatherResponseBody body;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherResponseHeader{
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherResponseBody{
        private String dataType;
        private WeatherResponseItems items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherResponseItems{
        private List<WeatherInfo> item;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherInfo{
        private String category;
        private String fcstTime;
        private String fcstValue;
    }

    public String getInfos(){
        StringBuilder sb = new StringBuilder();
        for(WeatherInfo info : this.response.body.items.item){
            sb.append("Category: " + info.category + "\n");
            sb.append("FcstTime: " + info.fcstTime+ "\n");
            sb.append("FcstValue: " + info.fcstValue+ ",\n");
        }
        return sb.toString();
    }

}
