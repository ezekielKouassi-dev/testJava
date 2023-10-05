CREATE SEQUENCE epreuves_id_seq START WITH 1 INCREMENT BY 50;

create table epreuves (
    id int8 not null primary key,
    audit_date_creation TIMESTAMP WITHOUT TIME ZONE not null,
    audit_login_creation varchar(255) not null,
    audit_date_modification TIMESTAMP WITHOUT TIME ZONE,
    audit_login_modification varchar(255),
    version int8 default 0,
    cours_id int8 not null,
    examen_id int8 not null,
    CONSTRAINT epreuves_cours_fk FOREIGN KEY (cours_id) REFERENCES cours(id),
    CONSTRAINT epreuves_examen_fk FOREIGN KEY (examen_id) REFERENCES examen(id)
);

ALTER TABLE examen ALTER COLUMN id SET DEFAULT nextval('epreuves_id_seq');
