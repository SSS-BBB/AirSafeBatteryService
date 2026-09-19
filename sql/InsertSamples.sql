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

SELECT * FROM USER;

SELECT * FROM TRANSACTION;



