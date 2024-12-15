package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.constants.PlaceHolders;
import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

//    private static final String API=
//            "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;
    public WeatherResponse getWeather(String city){
        System.out.println(AppCache.keys.WEATHER_APP);
        System.out.println(PlaceHolders.City);
        System.out.println(PlaceHolders.API_KEY);
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }
        else{
            //        String finalAPI=appCache.APP_CACHE.get(AppCache.keys.WEATHER_APP).replace(PlaceHolders.City,city).replace(PlaceHolders.API_KEY,apiKey);
            String finalAPI=appCache.APP_CACHE.get(AppCache.keys.WEATHER_APP.toString()).replace("<city>",city).replace("<apiKey>",apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body!=null){
                redisService.set("weather_of_"+city,body,300l);
            }
            return body;
        }
    }

//    post is just for learning, not implemented properly
//    public WeatherResponse postWeather(String city){
//        String finalAPI=API.replace("CITY",city).replace("API_KEY",apiKey);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key","value");
//        User user=User.builder().userName("Paritosh").password("123").build();
//        HttpEntity<User>httpEntity=new HttpEntity<>(user,httpHeaders);
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);

//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, httpEntity, WeatherResponse.class);
//        WeatherResponse body = response.getBody();
//        return body;
//    }
}
