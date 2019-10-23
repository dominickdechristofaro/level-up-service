package com.hussey.levelupservice.controller;

import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * The handler triggered by JSR validations
     * @param e MethodArgumentNotValidException
     * @param request request that did not supply correct input
     * @return VndErrors of all the reasons why the request failed
     */

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<VndErrors> notEnoughValues(MethodArgumentNotValidException e, WebRequest request) {

        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<VndErrors.VndError> vndErrorList = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            VndErrors.VndError vndError = new VndErrors.VndError(request.toString(), fieldError.getDefaultMessage());
            vndErrorList.add(vndError);
        }
        VndErrors errors = new VndErrors(vndErrorList);

        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Used for preventing SQL injections, ID for all possible post, put, delete methods must be a number so any
     * stringy values are rejected, preventing things like where id = 1 or true = true
     * @param e NumberFormatException
     * @param request request that triggered the exception
     * @return message telling user not to send invalid input
     */

    @ExceptionHandler({NumberFormatException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<VndErrors> idMustBeANumber(NumberFormatException e, WebRequest request) {
        VndErrors errors = new VndErrors(request.toString(), "Id must be a number, no other characters are allowed");
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        return responseEntity;
    }

    /**
     * Thrown from the service layer if no entry is found for that id
     * @param e NoSuchElementException
     * @param request request that triggered the exception
     * @return message telling user no entry was found for that id
     */

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<VndErrors> noEntityFoundForThatId(NoSuchElementException e, WebRequest request) {
        VndErrors errors = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

}