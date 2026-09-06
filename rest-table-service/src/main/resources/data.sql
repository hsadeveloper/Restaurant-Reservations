-- Seed data for table definitions
INSERT INTO table_definition (table_id, capacity) 
VALUES 
    ('T1', 4), 
    ('T2', 4), 
    ('T3', 5), 
    ('T4', 6), 
    ('T5', 6), 
    ('T6', 6) 
ON CONFLICT (table_id) DO NOTHING;

-- Seed data for table availability (with created_at explicitly tracked)
INSERT INTO table_availability (customer_id, capacity, status, created_at) 
VALUES 
    (NULL, 4, 'AVAILABLE', CURRENT_TIMESTAMP),
    (NULL, 4, 'AVAILABLE', CURRENT_TIMESTAMP),
    (NULL, 5, 'AVAILABLE', CURRENT_TIMESTAMP),
    (NULL, 6, 'AVAILABLE', CURRENT_TIMESTAMP),
    (NULL, 6, 'AVAILABLE', CURRENT_TIMESTAMP),
    (NULL, 6, 'AVAILABLE', CURRENT_TIMESTAMP);
