-- Create the database (run this manually if needed)
-- CREATE DATABASE salestracker;

CREATE TABLE IF NOT EXISTS sales (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    product VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    sale_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Open'
);

-- Sample data
INSERT INTO sales (customer_name, product, amount, sale_date, status) VALUES
('Alice Johnson', 'Widget Pro', 1500.00, '2024-01-15', 'Closed'),
('Bob Smith', 'Gadget Plus', 3200.50, '2024-02-20', 'Open'),
('Carol White', 'Super Tool', 800.00, '2024-03-05', 'Pending');
