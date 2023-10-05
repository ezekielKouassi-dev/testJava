package com.spring.demo.socle.validation;

/**
 * Erreur de validation.
 */
public class Erreur {

	/** Message de l'erreur */
	private String message;

	/** Le type de l'erreur (i.e. 'NotNull', 'NotBlank' etc.) */
	private String errorType;

	/** La valeur ayant entraîné l'erreur */
	private Object errorValue;

	public Erreur(String errorType, String message, Object errorValue) {
		this.errorType = errorType;
		this.message = message;
		this.errorValue = errorValue;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorType() {
		return errorType;
	}


	public Object getErrorValue() {
		return errorValue;
	}
}