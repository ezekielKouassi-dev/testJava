package com.spring.demo.dto;

/**
 * le Dto des eleves.
 */
public class EleveDto {
	private String nom;

	private String prenoms;

	public EleveDto() {
	}

	public EleveDto(String nom, String prenoms) {
		this.nom = nom;
		this.prenoms = prenoms;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenoms() {
		return prenoms;
	}
}
