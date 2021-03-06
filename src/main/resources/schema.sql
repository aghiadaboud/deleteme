create TABLE IF NOT EXISTS test(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    s varchar (55)
);

create TABLE IF NOT EXISTS test2(
    id  INTEGER PRIMARY KEY AUTO_INCREMENT,
    s2 varchar (55),
    test_id integer references test (id)
);