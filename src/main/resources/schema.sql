-- create table if not exists root
-- (
--     id int primary key AUTO_INCREMENT,
--     r  varchar(50)
--
-- );
--
--
-- create table if not exists test
-- (
--     id int primary key AUTO_INCREMENT,
--     s  varchar(50)
-- --     root int references root (id)
--
-- );
--
-- create table if not exists test2
-- (
--     test int primary key references test (id) on delete CASCADE,
--     s2   varchar(50)
--
-- );
--
--
-- create table if not exists test3
-- (
--     id int primary key AUTO_INCREMENT,
--     s3   varchar(50),
--     test int references test (id)
-- );


-- SERIAL is an alias for BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE.
-- wenn mysql dann benutze serial


create table if not exists uebung
(
    id               BIGINT primary key AUTO_INCREMENT,
    name             varchar(100) NOT NULL,
    gruppenanmeldung BOOLEAN      NOT NULL
);


create table if not exists anmeldungfrist
(
    uebung           BIGINT primary key references uebung (id),
    von              DATE NOT NULL,
    bis              DATE NOT NULL
);


create table if not exists zeitslot
(
    id         BIGINT primary key AUTO_INCREMENT,
    kapazitaet INTEGER,
    uebung     BIGINT references uebung (id)
);


create table if not exists tutor
(
    id         BIGINT primary key AUTO_INCREMENT,
    githubname VARCHAR(100) NOT NULL,
    zeitslot      BIGINT references zeitslot (id)
);


create table if not exists organisator
(
    id         BIGINT primary key AUTO_INCREMENT,
    githubname VARCHAR(100) NOT NULL
);


create table if not exists termin
(
    zeitslot      BIGINT primary key references zeitslot (id) on delete CASCADE,
    zeitstempel TIMESTAMP,
    reserviert BOOLEAN
);

create table if not exists gruppe
(
    id       BIGINT primary key AUTO_INCREMENT,
    name     varchar(100) NOT NULL,
--     min      INTEGER,
--     max      INTEGER,
    termin BIGINT references termin (id)
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





