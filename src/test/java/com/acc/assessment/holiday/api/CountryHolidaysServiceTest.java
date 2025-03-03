package com.acc.assessment.holiday.api;

import com.acc.assessment.holiday.api.client.ExternalAPIClientConfig;
import com.acc.assessment.holiday.api.exception.ExternalApiException;
import com.acc.assessment.holiday.api.exception.NoDataException;
import com.acc.assessment.holiday.api.model.CountryHoliday;
import com.acc.assessment.holiday.api.service.CountryHolidaysService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CountryHolidaysServiceTest {

    private final int currentYear = Year.now().getValue();

    @Autowired
    CountryHolidaysService service;

    /**
     * Unit test for getLast3CountryHolidays method.
     * This test verifies that the method correctly returns the last three holidays for a given country.
     */
    @Test
    public void getLast3CountryHolidaysTest() {

        /// Initial setup
        ExternalAPIClientConfig mockConfig = Mockito.mock(ExternalAPIClientConfig.class);
        ExternalAPIClientConfig clientConfig = (ExternalAPIClientConfig) ReflectionTestUtils.getField(service, "clientConfig");
        ReflectionTestUtils.setField(service, "clientConfig", mockConfig);
        List<String> actualResult = new ArrayList<String>();
        List<String> expectedResult = new ArrayList<String>(List.of("2025-01-01", "2025-01-06", "2024-12-26"));
        Collections.sort(expectedResult);
        Mockito.doReturn(createCountryHolidayArray()).when(mockConfig).getCountryHolidays("AT", currentYear);

        /// method call and dates extraction
        for (CountryHoliday ch : service.getLast3CountryHolidays("AT", currentYear)) actualResult.add(ch.date());
        Collections.sort(actualResult);

        /// verify if results match
        assertEquals(actualResult, expectedResult);

        /// clear mock data for further integration tests
        ReflectionTestUtils.setField(service, "clientConfig", clientConfig);

    }

    /**
     * Unit test for the getPublicHolidays method.
     * This test verifies that the method correctly retrieves count of public holidays for a given list of countries in a year in descending order.
     */
    @Test
    public void getPublicHolidaysTest() {

        /// Initial setup
        String[] countryCodes = new String[]{"BE", "US", "NL"};
        Map<String, Integer> expectedResult = new LinkedHashMap<>(Map.of("US", 13, "BE", 9, "NL", 7));

        /// method call
        Map<String, Integer> actualResult = service.getPublicHolidays(countryCodes, currentYear);

        /// verify if results match
        assertEquals(actualResult, expectedResult);

    }

    /**
     * Unit test for the getCommonHolidays method.
     * This test verifies that the method correctly retrieves deduplicated common holidays between two countries for a given year.
     */
    @Test
    public void getCommonHolidaysTest() {

        /// Initial setup
        Map<String, String> date1Map = new HashMap<>(Map.of("US", "New Year's Day", "NL", "Nieuwjaarsdag"));
        Map<String, String> date2Map = new HashMap<>(Map.of("US", "Christmas Day", "NL", "Eerste Kerstdag"));
        Map<String, String> date3Map = new HashMap<>(Map.of("US", "Good Friday", "NL", "Goede Vrijdag"));
        Map<String, Map<String, String>> expectedResult = new HashMap<>(Map.of("2025-01-01", date1Map, "2025-12-25", date2Map, "2025-04-18", date3Map));

        /// method call
        Map<String, Map<String, String>> actualResult = service.getCommonHolidays("US", "NL", currentYear);

        /// verify if results match
        assertEquals(actualResult, expectedResult);

    }

    /**
     * Unit test for the getCountryHolidays method.
     * This test verifies that the method correctly retrieves country holidays for a country for a given year.
     * It also verifies if exceptions thrown are as expected in case of invalid inputs.
     */
    @Test
    public void getCountryHolidaysTest() {

        ///  verify if holiday list returned is as expected
        assertEquals(11, service.getCountryHolidays("NL", currentYear).size());
        ///  verify if valid exceptions thrown
        assertThrows(NoDataException.class, () -> service.getLast3CountryHolidays("SS", currentYear));
        assertThrows(ExternalApiException.class, () -> service.getLast3CountryHolidays("AT", 0));
    }

    /**
     * Creates an array of CountryHoliday objects.
     * This method simulates the creation of holiday data.
     *
     * @return An array of CountryHoliday objects corresponding to the specified country.
     */
    private CountryHoliday[] createCountryHolidayArray() {
        return new CountryHoliday[]{
                new CountryHoliday(currentYear + "-12-28", "Holiday 1"),
                new CountryHoliday(currentYear + "-01-01", "Holiday 2"),
                new CountryHoliday(currentYear + "-01-06", "Holiday 3"),
                new CountryHoliday((currentYear - 1) + "-12-26", "Holiday 4"),
                new CountryHoliday((currentYear - 1) + "-11-06", "Holiday 5")};
    }
}
