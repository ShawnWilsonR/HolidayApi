package com.acc.assessment.holiday.api.validation;

public class Validation {

    /**
     * Checks if the given array is either null or empty.
     *
     * @param theArray The array to check.
     * @return true if the array is null or has no elements, false otherwise.
     */
    public static boolean isArrayNullOrEmpty(String[] theArray) {
        return theArray == null || theArray.length == 0;
    }
}
