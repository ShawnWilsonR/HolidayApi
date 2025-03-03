package com.acc.assessment.holiday.api.client;

import com.acc.assessment.holiday.api.model.CountryHoliday;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ExternalAPIClientConfig {

    @Value("${country.holidays.api.url}")
    private String url;

    /**
     * Creates and returns an instance of {@code WebClient}.
     *
     * @return A configured {@code WebClient} instance for making web requests.
     */
    private WebClient getWebClient() {
        return WebClient.builder().baseUrl(url).build();
    }

    /**
     * Retrieves an array of holidays for a given country and year.
     *
     * @param countryCode The code representing the country (e.g., "US" for the United States).
     * @param year        The year for which holidays are to be retrieved.
     * @return An array of {@code CountryHoliday} objects representing the holidays in the specified country and year.
     */
    public CountryHoliday[] getCountryHolidays(String countryCode, int year) {
        return getWebClient().get().uri("/{year}/{countryCode}", year, countryCode).retrieve().bodyToMono(CountryHoliday[].class).block();
    }

}