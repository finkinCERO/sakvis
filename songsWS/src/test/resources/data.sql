DROP TABLE IF EXISTS users;

CREATE TABLE users (
  username VARCHAR(50) PRIMARY KEY,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  password VARCHAR(100) DEFAULT NULL
);


