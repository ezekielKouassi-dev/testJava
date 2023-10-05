package com.spring.demo.socle.validation;


/**
 * Erreur de validation sur un champ donné.
 */
public class FieldErreur {

	/** Le champ en erreur */
	private String field;

	/** Le détail de l'erreur */
	private Erreur erreur;

	public FieldErreur(String field, String errorType, String message, Object errorValue) {
		this.field = field;
		this.erreur = new Erreur(errorType, message, errorValue);
	}

	public String getField() {
		return field;
	}


	public Erreur getErreur() {
		return erreur;
	}
}
