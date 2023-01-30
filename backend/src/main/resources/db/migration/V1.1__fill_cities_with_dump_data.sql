INSERT INTO city (name, photo)
SELECT name, photo
FROM CSVREAD('classpath:db/cities.csv', null, 'charset=UTF-8 fieldSeparator=,');