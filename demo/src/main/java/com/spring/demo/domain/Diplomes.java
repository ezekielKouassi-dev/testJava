package com.spring.demo.domain;

import com.spring.demo.socle.model.AbstractEntity;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

import static com.spring.demo.socle.model.JpaConstants.ID;
import static com.spring.demo.socle.model.JpaConstants.SEQ;

@Entity
@Table(name = Diplomes.TABLE_NAME)
@Access(AccessType.FIELD)
@BatchSize(size = 50)
public class Diplomes extends AbstractEntity {
	public static final String TABLE_NAME = "diplomes";

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
