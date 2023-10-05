CREATE SEQUENCE examen_id_seq START WITH 1 INCREMENT BY 50;

create table examen (
    id int8 not null primary key,
    audit_date_creation TIMESTAMP WITHOUT TIME ZONE not null,
    audit_login_creation varchar(255) not null,
    audit_date_modification TIMESTAMP WITHOUT TIME ZONE,
    audit_login_modification varchar(255),
    version int8 default 0,
    resultat varchar(255) not null,
    eleve_id int8 not null,
    diplome_id int8 not null,
    CONSTRAINT examen_eleve_fk FOREIGN KEY (eleve_id) REFERENCES eleve(id),
    CONSTRAINT examen_diplome_fk FOREIGN KEY (diplome_id) REFERENCES diplome(id)
);

ALTER TABLE examen ALTER COLUMN id SET DEFAULT nextval('examen_id_seq');