INSERT INTO Product (maker, model, type) VALUES
('Dell',   'OptiPlex-7080', 'PC'),
('Dell',   'OptiPlex-5090', 'PC'),
('Asus',   'ROG-Strix',    'PC'),
('HP',     'EliteBook-850', 'Laptop'),
('HP',     'ProBook-440',   'Laptop'),
('HP',     'LaserJet-1020', 'Printer'),
('Lenovo', 'ThinkPad-T14',  'Laptop'),
('Canon',  'PIXMA-G3420',   'Printer'),
('A', 'A-PC-100',     'PC'),
('A', 'A-PC-200',     'PC'),
('A', 'A-PC-300',     'PC'),
('A', 'A-Laptop-15',  'Laptop'),
('B', 'B-PC-10',       'PC'),
('B', 'B-Printer-1',   'Printer'),
('B', 'B-ColorJet',    'Printer');

INSERT INTO PC (code, model, speed, ram, hd, cd, price) VALUES
(1,  'OptiPlex-7080', 3200, 8192, 512,  '16x', 1200),
(2,  'ROG-Strix',    3600, 8192, 1024, '24x', 2200),
(3,  'OptiPlex-5090', 1800, 4096, 256, '12x', 450),
(4,  'A-PC-100',     1000,  4096, 256, '12x', 400),
(5,  'A-PC-200',     1000,  4096, 256, '24x', 450),
(6,  'A-PC-300',     1200, 8192, 512, '16x', 900),
(7,  'B-PC-10',      1000, 2048, 256, '24x', 550);

INSERT INTO Laptop (code, model, speed, ram, hd, screen, price) VALUES
(10, 'EliteBook-850', 2800, 8192, 512,  15, 1800),
(11, 'ThinkPad-T14',  3000, 8192, 1024, 14, 2000),
(12, 'ProBook-440',   2000, 8192, 256,  14, 950),
(13, 'A-Laptop-15',   900,  8192, 256,  15, 1100);

INSERT INTO Printer (code, model, color, type, price) VALUES
(20, 'PIXMA-G3420',   'y', 'Jet',    350),
(21, 'LaserJet-1020', 'n', 'Laser',  500),
(22, 'B-ColorJet',    'y', 'Jet',    300),
(23, 'B-Printer-1',   'n', 'Laser',  400);
