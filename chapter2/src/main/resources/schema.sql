CREATE TABLE Customer(
	ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(255) DEFAULT NULL,
    contact VARCHAR(255) DEFAULT NULL,
    telephone VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    remark Text,
    PRIMARY KEY(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO TEST VALUES(1, 'Customer1','Jack','15639749320','Jack@gmail.com',null);
INSERT INTO TEST VALUES(2, 'Customer1','rose','15639749321','rose@gmail.com',null);