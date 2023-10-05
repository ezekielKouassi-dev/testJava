package com.spring.demo.socle.validation;

import ci.monvisa.backend.socle.exception.HasCodeErreur;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Objet stockant le détail des erreurs de validation obtenues par BeanValidation.
 */
public class ValidationErreur {

	/**
	 * Nombre d'erreurs
	 */
	private long countErreurs;

	/**
	 * Code d'erreur.
	 */
	private String code;

	/**
	 * Liste des erreurs avec :
	 * - Clé : Le champ en erreur
	 * - Valeur : L'erreur.
	 */
	private Map<String, Erreur> erreurs;

	/**
	 * Constructeur prenant en paramètre les erreurs trouvées.
	 *
	 * @param erreurs la liste des erreurs.
	 */
	public ValidationErreur(List<FieldErreur> erreurs) {
		this.countErreurs = erreurs.size();
		this.erreurs = erreurs.stream()
				.collect(toMap(FieldErreur::getField, FieldErreur::getErreur));
	}

	/**
	 * Constructeur prenant en paramètre les erreurs trouvées et le code d'erreur associé.
	 *
	 * @param erreurs la liste des erreurs.
	 * @param code le code d'erreur.
	 */
	public ValidationErreur(List<FieldErreur> erreurs, HasCodeErreur code) {
		this(erreurs);
		this.code = String.valueOf(code.getCode());
	}

	public String getType() {
		return "VALIDATION";
	}

	public Map<String, Erreur> getErreurs() {
		return erreurs;
	}

	public String getCode() {
		return code;
	}

	public long getCountErreurs() {
		return countErreurs;
	}
}