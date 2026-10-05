USE AIRSAFE_BATTERY_SERVICE;

INSERT INTO USER
VALUES 
('ABCDE12345', 'First', 'User', 'firstemail@something.cool', '0000000000', 'ngrweuinhtgwveringrwij'),
('VWXYZ12345', 'Secode', 'User', 'secondemail@something.cool', '0000000001', 'grnsufgidssiognoi'),
('ABCDE67890', 'Harry', 'Potter', 'harrylovesmagic@ma.gic', '0000000003', 'iuhtgioughreuipogjnerio');

INSERT INTO TRANSACTION
VALUES
('PAY012ABC8', NOW(), 150.00, 'VWXYZ12345'),
('XYZ123GHI0', NOW(), 200.00, 'ABCDE67890'),
('OXP987KXR5', NOW(), 180.00, 'VWXYZ12345');

INSERT INTO POWERBANKRULE
VALUES
('RULE000001', 2, 100, false, true, 'https://www.iata.org/contentassets/90f8038b0eea42069554b2f4530f49ea/guidance-to-operators---power-banks.pdf');

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
('REMAX', '10000mAh Black', 'RPP-37', 'สนามบินสุวรรณภูมิ', 1, 50, 100, 20),
('REMAX', '10000mAh Gray', 'WP-117', 'สนามบินสุวรรณภูมิ', 2, 55, 110, 25),
('Anker', 'PowerCore 10000', 'A1263', 'สนามบินสุวรรณภูมิ', 3, 40, 90, 14),
('Anker', 'ZOLO White', 'A110EH21', 'สนามบินสุวรรณภูมิ', 4, 65, 125, 21),
('REMAX', '20000mAh Gray', 'CP-17', 'สนามบินดอนเมือง', 1, 60, 120, 15),
('Aukey', 'Black Basix Mini', 'PB-N83S', 'สนามบินดอนเมือง', 2, 60, 110, 30),
('Mofit', 'Mofit Power Bank', 'M11PD', 'สนามบินดอนเมือง', 3, 60, 100, 25),
('Xiaomi', '10000 MAH (Integrated Cable)', 'P15ZM', 'สนามบินดอนเมือง', 5, 50, 80, 10),
('Anker', 'NANO POWER BANK', 'A1638H11', 'สนามบินดอนเมือง', 8, 45, 110, 28)
;

SELECT * FROM POWERBANK;

SELECT * FROM POWERBANKINPUT;
SELECT * FROM POWERBANKOUTPUT;

SELECT * FROM FORRENTPOWERBANK NATURAL JOIN POWERBANK;


