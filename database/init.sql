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
    sale_type VARCHAR(50),           -- enum name: 'BULK' or 'INDIVIDUAL'
    phone_number VARCHAR(20),        -- contact number for this sale
    company_website VARCHAR(255),    -- BULK only: company webpage
    tax_id VARCHAR(50)               -- BULK only: company tax ID
);

-- Sample data (sale_type must match the SaleType enum names exactly)
INSERT INTO sales (customer_name, product, amount, sale_date, status, sale_type, phone_number, company_website, tax_id) VALUES
('Alice Johnson', 'Widget Pro', 1500.00, '2024-01-15', 'Closed', 'INDIVIDUAL', '212-555-0001', NULL, NULL),
('Bob Smith', 'Gadget Plus', 500.50, '2024-02-20', 'Open', 'BULK', '312-555-0002', 'https://bobscorp.com', '12-3456789'),
('Carol White', 'Super Tool', 800.00, '2024-03-05', 'Pending', 'BULK', '415-555-0003', 'https://carolenterprises.com', '98-7654321');
