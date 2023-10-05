package com.spring.demo.repository;

import com.spring.demo.domain.Eleves;
import com.spring.demo.socle.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class EleveRepository implements CrudRepository<Eleves> {
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<Eleves> getClazz() {
		return Eleves.class;
	}
}
