package com.acc.assessment.holiday.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles InvalidInputException and returns a structured error response.
     *
     * @param invalidInputException The InvalidInputException that was thrown.
     * @return An ErrorResponse object containing error details.
     */
    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(InvalidInputException invalidInputException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), invalidInputException.getMessage());
    }

    /**
     * Handles ExternalApiException and returns a structured error response.
     *
     * @param externalApiException The ExternalApiException that was thrown.
     * @return An ErrorResponse object containing error details.
     */
    @ExceptionHandler(value = ExternalApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(ExternalApiException externalApiException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), externalApiException.getMessage());
    }

    /**
     * Handles NoDataException and returns a structured error response.
     *
     * @param noDataException The NoDataException that was thrown.
     * @return An ErrorResponse object containing error details.
     */
    @ExceptionHandler(value = NoDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleException(NoDataException noDataException) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), noDataException.getMessage());
    }

    /**
     * Handles Exception and returns a structured error response.
     *
     * @param exception The Exception that was thrown.
     * @return An ErrorResponse object containing error details.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(Exception exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
