-- Create the database (run this manually if needed)
-- CREATE DATABASE salestracker;

-- Reference table: user types for sales reps
CREATE TABLE IF NOT EXISTS user_types (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL UNIQUE,  -- e.g. 'Inside Sales', 'Field Sales', 'Account Manager'
    phone_number VARCHAR(20)                  -- contact number for this user type / team
);

-- Insert reference data
INSERT INTO user_types (type_name, phone_number) VALUES
('Inside Sales', '555-100-0001'),
('Field Sales', '555-100-0002'),
('Account Manager', '555-100-0003')
ON CONFLICT (type_name) DO NOTHING;

-- Sales table now references user_types (Many sales -> One user_type)
CREATE TABLE IF NOT EXISTS sales (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    product VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    sale_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Open',
    user_type_id INT REFERENCES user_types(id)  -- FK: many sales belong to one user type
);

-- Sample data
INSERT INTO sales (customer_name, product, amount, sale_date, status, user_type_id) VALUES
('Alice Johnson', 'Widget Pro', 1500.00, '2024-01-15', 'Closed', 1),
('Bob Smith', 'Gadget Plus', 3200.50, '2024-02-20', 'Open', 2),
('Carol White', 'Super Tool', 800.00, '2024-03-05', 'Pending', 3);
