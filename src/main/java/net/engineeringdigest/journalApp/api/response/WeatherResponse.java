package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse{
    private Current current;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Current{
        private int temperature;

        @JsonProperty("observation_time")
        private String weatherDescriptions;
        private int feelslike;
    }
}


