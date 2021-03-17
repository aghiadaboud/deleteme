
-- SERIAL is an alias for BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE.
-- wenn mysql dann benutze serial


create table if not exists uebung
(
    id               BIGINT primary key AUTO_INCREMENT,
    name             varchar(100) NOT NULL,
    gruppenanmeldung BOOLEAN      NOT NULL,
    anmeldungfristvon              DATE NOT NULL,
    anmeldungfristbis              DATE NOT NULL
);



create table if not exists zeitslot
(
    id         BIGINT primary key AUTO_INCREMENT,
    kapazitaet INTEGER,
    datum DATE NOT NULL,
    uhrzeit VARCHAR(100) NOT NULL,
    reserviert BOOLEAN,
    uebung     BIGINT references uebung (id)
);


-- create table if not exists organisator
-- (
--     id         BIGINT primary key AUTO_INCREMENT,
--     githubname VARCHAR(100) NOT NULL
-- );



create table if not exists gruppe
(
    id       BIGINT primary key AUTO_INCREMENT,
    name     varchar(100) NOT NULL,
--     min      INTEGER,
--     max      INTEGER,
    zeitslot      BIGINT references zeitslot (id)
);



create table if not exists tutor
(
    id         BIGINT primary key AUTO_INCREMENT,
    githubname VARCHAR(100) NOT NULL,
    zeitslot      BIGINT references zeitslot (id),
    gruppeid     BIGINT
);



create table if not exists student
(
    id         BIGINT primary key AUTO_INCREMENT,
    githubname VARCHAR(100) NOT NULL,
    gruppe     BIGINT references gruppe (id)
);


create table if not exists repo
(
    gruppe BIGINT primary key references gruppe (id) on DELETE cascade,
    name   varchar(100) NOT NULL,
    link   varchar(255) NOT NULL

);





