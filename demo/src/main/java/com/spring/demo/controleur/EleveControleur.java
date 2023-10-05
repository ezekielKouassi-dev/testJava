package com.spring.demo.controleur;

import com.spring.demo.dto.EleveDto;
import com.spring.demo.facade.EleveFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/eleves")
public class EleveControleur {
	private final EleveFacade eleveFacade;

	public EleveControleur(EleveFacade eleveFacade) {
		this.eleveFacade = eleveFacade;
	}

	@PostMapping
	public void enregistreEleve(EleveDto eleveDto) {
		eleveFacade.enregisterEleve(eleveDto);
	}
}
