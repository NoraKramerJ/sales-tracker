-- Create the database (run this manually if needed)
-- CREATE DATABASE salestracker;

-- Sales table: sale_type is 'BULK' or 'INDIVIDUAL'; phone_number entered per sale
CREATE TABLE IF NOT EXISTS sales (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    product VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    sale_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Open',
    sale_type VARCHAR(50),      -- enum name: 'BULK' or 'INDIVIDUAL'
    phone_number VARCHAR(20)    -- contact number for this sale
);

-- Sample data (sale_type must match the SaleType enum names exactly)
INSERT INTO sales (customer_name, product, amount, sale_date, status, sale_type, phone_number) VALUES
('Alice Johnson', 'Widget Pro', 1500.00, '2024-01-15', 'Closed', 'INDIVIDUAL', '555-100-0001'),
('Bob Smith', 'Gadget Plus', 320.50, '2024-02-20', 'Open', 'BULK', '555-100-0002'),
('Carol White', 'Super Tool', 800.00, '2024-03-05', 'Pending', 'INDIVIDUAL', '555-100-0003');
