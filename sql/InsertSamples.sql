USE AIRSAFE_BATTERY_SERVICE;

INSERT INTO USER
VALUES 
(1, 'First', 'User', 'firstemail@something.cool', '0000000000', 'ngrweuinhtgwveringrwij'),
(2, 'Secode', 'User', 'secondemail@something.cool', '0000000001', 'grnsufgidssiognoi'),
(3, 'Harry', 'Potter', 'harrylovesmagic@ma.gic', '0000000003', 'iuhtgioughreuipogjnerio');

SELECT * FROM USER;

INSERT INTO TRANSACTION
VALUES
(1, NOW(), 150.00, 2),
(2, NOW(), 200.00, 1),
(3, NOW(), 180.00, 3);

SELECT * FROM TRANSACTION;

INSERT INTO POWERBANKRULE
VALUES
(1, 2, 100, false, true, 'https://www.iata.org/contentassets/90f8038b0eea42069554b2f4530f49ea/guidance-to-operators---power-banks.pdf');

SELECT * FROM POWERBANK;

/*
INSERT INTO FORRENTPOWERBANK VALUES
('ALPHA', '20,000 mAh White', 'B20PD', 'สนามบินสุวรรณภูมิ', 1, 50, 100, 20),
('Blue Box', '10000 mAh built-in Lightning/Type-C PD22.5W Cream (CCC)', 'EP-I109', 'สนามบินสุวรรณภูมิ', 2, 20, 50, 14),
('Blue Box', '20000 mAh built-in Lightning/Type-C cable PD22.5W Cream', 'EPI209', 'สนามบินสุวรรณภูมิ', 3, 60, 150, 30),
('QPLUS', '15000 mAh LED Display with 2-in-1 Cable White', 'W1501C', 'สนามบินสุวรรณภูมิ', 4, 55, 120, 25),
('Ugreen', '20000 mAh 130W Two-way Fast Charing Black', '35524B', 'สนามบินสุวรรณภูมิ', 5, 50, 100, 25),
('ALPHA', '20,000 mAh White', 'B20PD', 'สนามบินดอนเมือง', 1, 50, 100, 20),
('Blue Box', '10000 mAh built-in Lightning/Type-C PD22.5W Cream (CCC)', 'EP-I109', 'สนามบินดอนเมือง', 2, 20, 50, 14),
('Ugreen', '20000 mAh 130W Two-way Fast Charing Black', '35524B', 'สนามบินดอนเมือง', 4, 50, 100, 25);
*/

INSERT INTO FORRENTPOWERBANK VALUES
(14, 'สนามบินสุวรรณภูมิ', 1, 50, 100, 20),
(15, 'สนามบินสุวรรณภูมิ', 2, 55, 110, 25),
(19, 'สนามบินสุวรรณภูมิ', 4, 65, 125, 21),
(16, 'สนามบินดอนเมือง', 1, 60, 120, 15),
(20, 'สนามบินดอนเมือง', 2, 60, 110, 30),
(21, 'สนามบินดอนเมือง', 3, 60, 100, 25),
(18, 'สนามบินดอนเมือง', 5, 50, 80, 10),
(22, 'สนามบินดอนเมือง', 8, 45, 110, 28),
(23, 'สนามบินภูเก็ต', 1, 50, 100, 7),
(24, 'สนามบินภูเก็ต', 2, 50, 120, 30),
(25, 'สนามบินภูเก็ต', 3, 40, 80, 21),
(26, 'สนามบินภูเก็ต', 4, 50, 100, 14)
;

SELECT * FROM POWERBANK;

SELECT * FROM FORRENTPOWERBANK;
SELECT * FROM RENTEDPOWERBANK;
SELECT * FROM DEPOSITEDPOWERBANK;

SELECT * FROM POWERBANKINPUT WHERE INPUT_TYPE = 'USB-C';
SELECT * FROM POWERBANKOUTPUT;

SELECT * FROM FORRENTPOWERBANK P NATURAL JOIN POWERBANK WHERE ADDRESS = 'สนามบินดอนเมือง'
AND 'Micro USB' IN (
SELECT INPUT_TYPE FROM POWERBANKINPUT
WHERE POWERBANK_ID = P.POWERBANK_ID
);

SELECT * FROM FORRENTPOWERBANK P NATURAL JOIN POWERBANK;

SELECT * FROM POWERBANKINPUT
WHERE Brand = 'Mofit' AND Name = 'Mofit Power Bank' AND Model = 'M11PD';

/*
DELETE FROM POWERBANKINPUT;
DELETE FROM POWERBANKOUTPUT;
DELETE FROM FORRENTPOWERBANK;
DELETE FROM POWERBANK;
*/

/*
DELETE FROM POWERBANKINPUT
WHERE POWERBANK_ID = 17;
DELETE FROM POWERBANKOUTPUT
WHERE POWERBANK_ID = 17;
DELETE FROM POWERBANK
WHERE POWERBANK_ID = 17;
*/


