INSERT INTO table_defination (table_id, capacity)
SELECT 'T1', 4
WHERE NOT EXISTS (
    SELECT 1 FROM table_defination WHERE table_id = 'T1'
);

INSERT INTO table_defination (table_id, capacity)
SELECT 'T2', 4
WHERE NOT EXISTS (
    SELECT 1 FROM table_defination WHERE table_id = 'T2'
);

INSERT INTO table_defination (table_id, capacity)
SELECT 'T3', 5
WHERE NOT EXISTS (
    SELECT 1 FROM table_defination WHERE table_id = 'T3'
);


INSERT INTO table_defination (table_id, capacity)
SELECT 'T4', 6
WHERE NOT EXISTS (
    SELECT 1 FROM table_defination WHERE table_id = 'T4'
);

INSERT INTO table_defination (table_id, capacity)
SELECT 'T5', 6
WHERE NOT EXISTS (
    SELECT 1 FROM table_defination WHERE table_id = 'T5'
);


INSERT INTO table_defination (table_id, capacity)
SELECT 'T6', 6
WHERE NOT EXISTS (
    SELECT 1 FROM table_defination WHERE table_id = 'T6'
);





-- Insert into availability by dynamically looking up the true auto-generated ID
INSERT INTO table_availability (table_id, status, cust_id)
VALUES 
    ((SELECT id FROM table_defination WHERE table_id = 'T1'), 'AVAILABLE', NULL),
    ((SELECT id FROM table_defination WHERE table_id = 'T2'), 'AVAILABLE', NULL),
    ((SELECT id FROM table_defination WHERE table_id = 'T3'), 'AVAILABLE', NULL),
    ((SELECT id FROM table_defination WHERE table_id = 'T4'), 'AVAILABLE', NULL),
    ((SELECT id FROM table_defination WHERE table_id = 'T5'), 'AVAILABLE', NULL),
    ((SELECT id FROM table_defination WHERE table_id = 'T6'), 'AVAILABLE', NULL)
ON CONFLICT (table_id) DO NOTHING;


