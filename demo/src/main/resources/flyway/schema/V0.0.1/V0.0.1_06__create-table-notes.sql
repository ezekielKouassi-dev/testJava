CREATE SEQUENCE notes_id_seq START WITH 1 INCREMENT BY 50;

create table notes (
                          id int8 not null primary key,
                          audit_date_creation TIMESTAMP WITHOUT TIME ZONE not null,
                          audit_login_creation varchar(255) not null,
                          audit_date_modification TIMESTAMP WITHOUT TIME ZONE,
                          audit_login_modification varchar(255),
                          version int8 default 0,
                          valeur numeric,
                          epreuves_id int8 not null,
                          eleve_id int8 not null,
                          CONSTRAINT notes_epreuves_fk FOREIGN KEY (epreuves_id) REFERENCES cours(id),
                          CONSTRAINT notes_eleve_fk FOREIGN KEY (eleve_id) REFERENCES examen(id)
);

ALTER TABLE notes ALTER COLUMN id SET DEFAULT nextval('notes_id_seq');
