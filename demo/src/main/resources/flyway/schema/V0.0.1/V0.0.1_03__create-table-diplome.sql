CREATE SEQUENCE diplome_id_seq START WITH 1 INCREMENT BY 50;

create table diplome (
    id int8 not null primary key,
    audit_date_creation TIMESTAMP WITHOUT TIME ZONE not null,
    audit_login_creation varchar(255) not null,
    audit_date_modification TIMESTAMP WITHOUT TIME ZONE,
    audit_login_modification varchar(255),
    version int8 default 0,
    designation varchar(255) not null
);

ALTER TABLE diplome ALTER COLUMN id SET DEFAULT nextval('diplome_id_seq');