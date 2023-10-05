package com.spring.demo.socle.model;

import ci.monvisa.backend.socle.utils.SecurityUtils;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractEntity implements JpaConstants {

	@Version
	protected long version;

	/**
	 * Contient les informations concernant la création de l'objet:
	 * <ul>
	 *     <li>le login de l'utilisateur ayant créé la donnée</li>
	 *     <li>la date de création de la donnée</li>
	 * </ul>
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "date", column = @Column(name = "audit_date_creation", updatable = false)),
			@AttributeOverride(name = "login", column = @Column(name = "audit_login_creation", updatable = false))
	})
	private DateEtLogin creation;

	/**
	 * Contient les informations concernant la création de l'objet:
	 * <ul>
	 *     <li>le login de l'utilisateur ayant créé la donnée</li>
	 *     <li>la date de création de la donnée</li>
	 * </ul>
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "date", column = @Column(name = "audit_date_modification", updatable = false)),
			@AttributeOverride(name = "login", column = @Column(name = "audit_login_modification", updatable = false))
	})
	private DateEtLogin modification;

	public AbstractEntity() {
	}

	@Transient
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * recupère l'Id de l'entités
	 *
	 * @return travail sur
	 */
	public abstract Long getId();

	/**
	 * Initialise les informations de traçabilité sur la demande.
	 * Valorise les informations concernant le login de la date de création.
	 * Valorise les informations concernant le login de la date de modification
	 */
	@PrePersist
	private void initialiserInformationsTracabilite() {
		String login = SecurityUtils.lireLoginUtilisateurConnecte();
		Instant now = Instant.now();
		if (creation == null) {
			creation = new DateEtLogin(now, login);
		}
		if (modification == null) {
			modification = new DateEtLogin(now, login);
		}
	}


	private void mettreAJourInformationsTracabilite() {
		if (modification == null) {
			modification = new DateEtLogin();
		}
		modification.update(Instant.now(), SecurityUtils.lireLoginUtilisateurConnecte());
	}

	public long getVersion() {
		return version;
	}

	public DateEtLogin getCreation() {
		return creation;
	}

	public DateEtLogin getModification() {
		return modification;
	}
}
