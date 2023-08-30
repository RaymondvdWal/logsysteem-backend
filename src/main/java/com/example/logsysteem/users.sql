INSERT INTO users (username, password, firstname, lastname, enabled, apikey, email) VALUES ('Gekke', '$2a$10$J.ysVldhrvyvkNCZgspNKuocbFFkHLlNUfwx/Xjbrxe8QGACSjMMK', 'Ha', 'Doe', true, '123456', 'GH@novi.nl');
INSERT INTO authorities (username, authority) VALUES ('Gekke', 'ADMIN'); /*password = "Henkie"*/

INSERT INTO users (username, password, firstname, lastname, enabled, apikey, email) VALUES ('Rare', '$2a$10$frA9wrrTefSSmKwa8ERRG.vE1PIrUBx4SXQ7Yot04jJDpEzlSPKA2', 'Ja', 'Dag', true, '123456', 'RH@novi.nl');
INSERT INTO authorities (username, authority) VALUES ('Rare', 'MODERATOR'); /*password = "Henkie"*/

INSERT INTO users (username, password, firstname, lastname, enabled, apikey, email) VALUES ('Malle', '$2a$10$tPSVd8E9qpOm1NX3k4mOye/Yz9vqkOvvxneKVq26q9jq305NF6Noy', 'Dikke', 'Doei', true, '123456', 'MH@novi.nl');
INSERT INTO authorities (username, authority) VALUES ('Malle', 'USER'); /*password = "Henkie"*/