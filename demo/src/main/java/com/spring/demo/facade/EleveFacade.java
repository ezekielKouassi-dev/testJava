package com.spring.demo.facade;

import com.spring.demo.domain.Eleves;
import com.spring.demo.dto.EleveDto;
import com.spring.demo.repository.EleveRepository;
import org.springframework.stereotype.Service;

@Service
public class EleveFacade {
	private final EleveRepository eleveRepository;

	public EleveFacade(EleveRepository eleveRepository) {
		this.eleveRepository = eleveRepository;
	}

	/**
	 * Permet d'enregister un éleve.
	 *
	 * @param eleveDto le dto de l'eleve.
	 */
	public void enregisterEleve(EleveDto eleveDto) {
		Eleves eleves = new Eleves(eleveDto);
		eleveRepository.save(eleves);
	}
}
