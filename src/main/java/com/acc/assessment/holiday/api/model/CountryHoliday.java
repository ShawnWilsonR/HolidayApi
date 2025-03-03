package com.acc.assessment.holiday.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CountryHoliday(String date, String localName, String name, String countryCode, Boolean fixed,
                             Boolean global, String[] counties, Integer launchYear, String[] types) {

    public CountryHoliday(String date, String name) {
        this(date, null, name, null, null, null, new String[0], null, new String[0]);
    }

    /**
     * Converts Date String to LocalDate format
     *
     * @return LocalDate
     */
    @JsonIgnore
    public LocalDate getLocalDate() {
        return LocalDate.parse(this.date);
    }

    /**
     * Checks if the holiday is a public holiday
     *
     * @return Boolean true if its public holiday
     */
    @JsonIgnore
    public boolean isPublicHoliday() {
        return !Arrays.stream(this.types()).filter(p -> p.equalsIgnoreCase("public")).toList().isEmpty();
    }

    /**
     * Checks if the date falls on the weekend
     *
     * @return Boolean true if its weekend date
     */
    @JsonIgnore
    public boolean isWeekend() {
        return this.getLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY || this.getLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
