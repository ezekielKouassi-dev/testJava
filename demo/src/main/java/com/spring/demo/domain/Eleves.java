package com.spring.demo.domain;

import com.spring.demo.socle.model.AbstractEntity;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@Table(name = Eleves.TABLE_NAME)
@Access(AccessType.FIELD)
@BatchSize(size = 50)
public class Eleves extends AbstractEntity {
	public static final String TABLE_NAME = "eleves";

	public static final String TABLE_ID = TABLE_NAME + ID;

	public static final String TABLE_SEQ = TABLE_ID + SEQ;

	@Id
	@SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
	private Long id;

	@Column(name = "nom", nullable = false)
	private String nom;

	@Column(name = "prenoms", nullable = false)
	private String prenoms;

	public Eleves() {
	}

	public Eleves(Long id, String nom, String prenoms) {
		this.id = id;
		this.nom = nom;
		this.prenoms = prenoms;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenoms() {
		return prenoms;
	}

	public void setPrenoms(String prenoms) {
		this.prenoms = prenoms;
	}
}
