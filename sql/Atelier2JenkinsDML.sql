INSERT INTO contract (id, type, margin) VALUES (1,'contrat 1', 5);
INSERT INTO contract (id, type, margin) VALUES (2,'contrat 2', 10);
INSERT INTO contract (id, type, margin) VALUES (3,'contrat 3', 15);
INSERT INTO contract (id, type, margin) VALUES (4,'contrat 4', 20);
INSERT INTO contract (id, type, margin) VALUES (5,'contrat 5', 25);

drop procedure if exists load_customer;

delimiter #
create procedure load_customer()
begin

declare v_max int unsigned default 26;
declare v_counter int unsigned default 1;

truncate table customer;
start transaction;
while v_counter < v_max do
    insert into customer (username, email, password, contract_id) values
    (CONCAT('customer', v_counter), CONCAT('customer', v_counter, '@epsi.fr'), 'password123',
    ( select FLOOR( RAND() * (6-1) + 1)) );
    set v_counter=v_counter+1;
end while;
commit;
end #

delimiter ;

call load_customer();

drop procedure if exists load_product;

delimiter #
create procedure load_product()
begin

declare v_max int unsigned default 11;
declare v_counter int unsigned default 1;

truncate table product;
start transaction;
while v_counter < v_max do
    insert into product (name, price) values 
    (CONCAT('product', v_counter), ( select FLOOR( RAND() * (500 - 51) + 50))  );
    set v_counter=v_counter+1;
end while;
commit;
end #

delimiter ;

call load_product();