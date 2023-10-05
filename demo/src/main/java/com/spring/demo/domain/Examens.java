package com.spring.demo.domain;

import com.spring.demo.socle.model.AbstractEntity;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@Table(name = Examens.TABLE_NAME)
@Access(AccessType.FIELD)
@BatchSize(size = 50)
public class Examens extends AbstractEntity {
	public static final String TABLE_NAME = "eleves";

	public static final String TABLE_ID = TABLE_NAME + ID;

	public static final String TABLE_SEQ = TABLE_ID + SEQ;

	@Id
	@SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
	private Long id;

	@Override
	public Long getId() {
		return id;
	}
}
