CREATE TABLE Product (
    maker VARCHAR(10) NOT NULL,
    model VARCHAR(50) NOT NULL,
    type  VARCHAR(10) NOT NULL
        CHECK (type IN ('PC', 'Laptop', 'Printer')),
    CONSTRAINT pk_product PRIMARY KEY (model)
);

CREATE TABLE PC (
    code  INT NOT NULL,
    model VARCHAR(50) NOT NULL,
    speed SMALLINT NOT NULL,
    ram   SMALLINT NOT NULL,
    hd    REAL NOT NULL,
    cd    VARCHAR(10) NOT NULL,
    price NUMERIC(10,2),
    CONSTRAINT pk_pc PRIMARY KEY (code),
    CONSTRAINT fk_pc_product FOREIGN KEY (model)
        REFERENCES Product(model)
);

CREATE TABLE Laptop (
    code   INT NOT NULL,
    model  VARCHAR(50) NOT NULL,
    speed  SMALLINT NOT NULL,
    ram    SMALLINT NOT NULL,
    hd     REAL NOT NULL,
    price  NUMERIC(10,2),
    screen SMALLINT NOT NULL,
    CONSTRAINT pk_laptop PRIMARY KEY (code),
    CONSTRAINT fk_laptop_product FOREIGN KEY (model)
        REFERENCES Product(model)
);

CREATE TABLE Printer (
    code  INT NOT NULL,
    model VARCHAR(50) NOT NULL,
    color CHAR(1) NOT NULL
        CHECK (color IN ('y', 'n')),
    type  VARCHAR(10) NOT NULL
        CHECK (type IN ('Laser', 'Jet', 'Matrix')),
    price NUMERIC(10,2),
    CONSTRAINT pk_printer PRIMARY KEY (code),
    CONSTRAINT fk_printer_product FOREIGN KEY (model)
        REFERENCES Product(model)
);
