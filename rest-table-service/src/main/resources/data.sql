INSERT INTO table_definition (table_id, capacity)
VALUES 
    ('T1', 4),
    ('T2', 4),
    ('T3', 5),
    ('T4', 6),
    ('T5', 6),
    ('T6', 6)
ON CONFLICT (table_id) DO NOTHING;

INSERT INTO table_availability (customer_id, capacity, status)
VALUES
    (NULL, 4, 'AVAILABLE'),
    (NULL, 4, 'AVAILABLE'),
    (NULL, 5, 'AVAILABLE'),
    (NULL, 6, 'AVAILABLE'),
    (NULL, 6, 'AVAILABLE'),
    (NULL, 6, 'AVAILABLE');