INSERT IGNORE INTO bank_account(account_number, bank_name, balance, account_type, ifsc_code, opening_date, mobile_number)
VALUES
(512345678, 'HDFCBank', 15000.00, 'SAVINGS', 'HDFC00001', '2022-01-10', '9876987431'),
(512345679, 'ICICIBank', 23000.00, 'CURRENT', 'ICIC00001', '2021-08-15', '9876987431'),
(512345680, 'AxisBank', 9000.00, 'SAVINGS', 'AXIS00001', '2020-06-11', '9123456780'),
(512345681, 'SBIIndia', 30000.00, 'SAVINGS', 'SBIN00001', '2019-03-21', '9876987431');

INSERT IGNORE INTO digital_bank_account(digital_banking_id, mobile_number, account_number, account_type)
VALUES
('W_1001', '9876987431', 512345678, 'SAVINGS');

INSERT IGNORE INTO transactions(transaction_id, mode_of_transaction, paid_to, receiver_account_number, amount, transaction_date_time, remarks, paid_from, sender_account_number)
VALUES
(1234543, 'Fund Transfer', '9123456780', 512345680, 500.00, '2026-01-12 10:00:00', 'Electricity split', '9876987431', 512345678);
