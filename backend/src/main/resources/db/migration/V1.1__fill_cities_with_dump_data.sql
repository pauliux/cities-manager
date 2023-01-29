INSERT INTO city (id, name, photo)
SELECT id, name, photo
FROM CSVREAD('classpath:db/cities.csv', null, 'charset=UTF-8 fieldSeparator=,');