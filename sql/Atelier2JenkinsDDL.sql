SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS contract;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS `contract` (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    type varchar(255) NOT NULL,
    margin int
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `customer` (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    contract_id int NOT NULL,
    FOREIGN KEY (contract_id) REFERENCES contract(id)
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `product` (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    price float
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;