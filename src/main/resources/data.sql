INSERT INTO account (id, user_id, balance, currency) VALUES (1, 11, 100, 'PLN');
INSERT INTO account (id, user_id, balance, currency) VALUES (2, 22, 200.10, 'PLN');
INSERT INTO account (id, user_id, balance, currency) VALUES (3, 33, 333.33333, 'PLN');

INSERT INTO exchange_rate (id, base_currency, target_currency, average_rate, date) VALUES (1, 'PLN', 'USD', 3.33, '2020-05-01 10:15:30.55');
INSERT INTO exchange_rate (id, base_currency, target_currency, average_rate, date) VALUES (2, 'PLN', 'USD', 3.77, '2021-05-01 11:11:11.55');