package com.acc.assessment.holiday.api.service;

import com.acc.assessment.holiday.api.client.ExternalAPIClientConfig;
import com.acc.assessment.holiday.api.exception.ExternalApiException;
import com.acc.assessment.holiday.api.exception.NoDataException;
import com.acc.assessment.holiday.api.model.CountryHoliday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountryHolidaysService {

    @Autowired
    private ExternalAPIClientConfig clientConfig;

    /**
     * Retrieves the last 3 holidays for the specified country and year.
     *
     * @param countryCode The country for which to retrieve the holidays (e.g., "US", "AT", "NL")
     * @param year        The year for which to retrieve the holidays (e.g., 2025)
     * @return A list containing last 3 celebrated holidays
     */
    public List<CountryHoliday> getLast3CountryHolidays(String countryCode, int year) {
        List<CountryHoliday> holidays = getCountryHolidays(countryCode, year).stream()
                .filter(ch -> LocalDate.now().isAfter(ch.getLocalDate()))
                .collect(Collectors.toCollection(ArrayList::new));

        while (holidays.size() < 3) holidays.addAll(getCountryHolidays(countryCode, --year));

        return holidays.stream()
                .sorted(Comparator.comparing(CountryHoliday::getLocalDate).reversed())
                .limit(3)
                .map(ch -> new CountryHoliday(ch.date(), ch.name()))
                .toList();
    }

    /**
     * Retrieves the count of non-weekend public holidays for multiple countries in the specified year in descending order.
     *
     * @param countryCodes An array of country codes (e.g., "US", "NL", "BE") for which to retrieve holiday count
     * @param year         The year for which to retrieve the public holidays (e.g., 2025)
     * @return A map sorted in descending order where the key is the country code and the value is the number of public holidays
     * in that country for the specified year
     */
    public Map<String, Integer> getPublicHolidays(String[] countryCodes, int year) {
        Map<String, Integer> countryHolidaysMap = new HashMap<String, Integer>();

        for (String countryCode : countryCodes) {
            countryHolidaysMap.put(countryCode, filterNonWeekendPublicHolidays(getCountryHolidays(countryCode, year)).size());
        }

        return countryHolidaysMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * Retrieves the deduplicated common holidays celebrated in both specified countries for the given year.
     *
     * @param countryCode1 The country code for the first country (e.g., "US", "NL")
     * @param countryCode2 The country code for the second country (e.g., "AT", "BE")
     * @param year         The year for which to retrieve the deduplicated common holidays (e.g., 2025)
     * @return A map where the key is the date and the value is another map
     * containing the holiday names in each country with keys corresponding to country codes.
     * For example: {"2025-12-25": {"US": "Christmas Day", "AT": "Christmas Day"}}
     */
    public Map<String, Map<String, String>> getCommonHolidays(String countryCode1, String countryCode2, int year) {
        Map<String, Map<String, String>> commonHolidaysMap = new HashMap<String, Map<String, String>>();

        List<CountryHoliday> holidayList = getCountryHolidays(countryCode1, year);

        for (CountryHoliday ch2 : getCountryHolidays(countryCode2, year)) {
            for (CountryHoliday ch1 : holidayList) {
                if (ch2.date().equalsIgnoreCase(ch1.date())) {
                    commonHolidaysMap
                            .computeIfAbsent(ch2.date(), k -> new HashMap<>())
                            .merge(ch2.countryCode(), ch2.localName(), (oldVal, newVal) -> oldVal.equalsIgnoreCase(newVal) ? oldVal : oldVal + ", " + newVal);

                    commonHolidaysMap.get(ch2.date())
                            .merge(ch1.countryCode(), ch1.localName(), (oldVal, newVal) -> oldVal.equalsIgnoreCase(newVal) ? oldVal : oldVal + ", " + newVal);
                }
            }
        }
        return commonHolidaysMap;
    }

    /**
     * Retrieves all holidays for the specified country and year.
     *
     * @param countryCode The country code (e.g., "US", "AT", "NL")
     *                    representing the country for which to retrieve holidays
     * @param year        The year for which to retrieve the holidays (e.g., 2025)
     * @return A List of all holidays for the specified country and year.
     * @throws NoDataException      If countryCode has no data setup in the external API
     * @throws ExternalApiException If input provided to external API is invalid
     */
    public List<CountryHoliday> getCountryHolidays(String countryCode, int year) {
        try {
            CountryHoliday[] countryHolidays = clientConfig.getCountryHolidays(countryCode, year);
            if (null == countryHolidays || countryHolidays.length == 0)
                throw new NoDataException("No data available for input provided");
            return List.of(countryHolidays);
        } catch (WebClientResponseException e) {
            throw new ExternalApiException("Invalid input");
        }
    }

    /**
     * Filters public holidays which are not falling on weekends (Saturday or Sunday)
     *
     * @param countryHolidays holidays list to be checked
     * @return List of all non-weekend public holidays
     */
    private List<CountryHoliday> filterNonWeekendPublicHolidays(List<CountryHoliday> countryHolidays) {
        return countryHolidays.stream().filter(ch -> ch.isPublicHoliday() && !ch.isWeekend()).toList();
    }
}
