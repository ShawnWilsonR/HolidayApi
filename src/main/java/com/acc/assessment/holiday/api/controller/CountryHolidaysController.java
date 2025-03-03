package com.acc.assessment.holiday.api.controller;

import com.acc.assessment.holiday.api.exception.InvalidInputException;
import com.acc.assessment.holiday.api.model.CountryHoliday;
import com.acc.assessment.holiday.api.service.CountryHolidaysService;
import com.acc.assessment.holiday.api.validation.Validation;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;
import java.util.Map;

@RestController
public class CountryHolidaysController {

    @Autowired
    CountryHolidaysService countryHolidaysService;

    /**
     * Retrieves the last 3 public holidays for a specified country.
     *
     * @param countryCode The country code (e.g., "US", "NL") to fetch holidays for.
     * @return A list of the last 3 holidays for the specified country.
     */
    @GetMapping("/country/last3Holidays")
    public List<CountryHoliday> last3CountryHolidays(@RequestParam String countryCode) {
        if (StringUtils.isNotBlank(countryCode))
            return countryHolidaysService.getLast3CountryHolidays(countryCode, Year.now().getValue());
        throw new InvalidInputException("Invalid country code");
    }

    /**
     * Retrieves the number of public holidays (non-weekend) for a given list of countries in a year in descending order.
     *
     * @param year         The year to check holidays for.
     * @param countryCodes The array of country codes (e.g., "NL", "US").
     * @return A map with the country code as the key and the holiday count as the value.
     */
    @GetMapping("/country/publicHolidays")
    public Map<String, Integer> publicHolidays(@RequestParam int year, String[] countryCodes) {
        if (!Validation.isArrayNullOrEmpty(countryCodes))
            return countryHolidaysService.getPublicHolidays(countryCodes, year);
        throw new InvalidInputException("Invalid request");
    }

    /**
     * Retrieves deduplicated common holidays between two countries for a specified year.
     *
     * @param countryCode1 The first country code (e.g., "US").
     * @param countryCode2 The second country code (e.g., "NL").
     * @param year         The year to check holidays for.
     * @return A map with holiday dates as key, and value is another map with country codes as keys and holiday local names as values.
     */
    @GetMapping("/countries/commonHolidays")
    public Map<String, Map<String, String>> commonHolidays(@RequestParam String countryCode1, String countryCode2, int year) {
        if (StringUtils.isNotBlank(countryCode1) && StringUtils.isNotBlank(countryCode2) && !countryCode1.equalsIgnoreCase(countryCode2))
            return countryHolidaysService.getCommonHolidays(countryCode1, countryCode2, year);
        throw new InvalidInputException("Invalid country code parameters");
    }
}
