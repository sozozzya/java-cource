-- 1
SELECT model, speed, hd
FROM pc
WHERE price < 500;

-- 2
SELECT DISTINCT maker
FROM product
WHERE type = 'Printer';

-- 3
SELECT model, ram, screen
FROM laptop
WHERE price > 1000;

-- 4
SELECT *
FROM printer
WHERE color = 'y';

-- 5
SELECT model, speed, hd
FROM pc
WHERE cd IN ('12x', '24x')
  AND price < 600;

-- 6
SELECT p.maker, l.speed
FROM laptop l
JOIN product p ON l.model = p.model
WHERE l.hd >= 100;

-- 7
SELECT p.model,
       COALESCE(pc.price, l.price, pr.price) AS price
FROM product p
LEFT JOIN pc pc       ON p.model = pc.model
LEFT JOIN laptop l    ON p.model = l.model
LEFT JOIN printer pr  ON p.model = pr.model
WHERE p.maker = 'B';

-- 8
SELECT DISTINCT maker
FROM product
WHERE type = 'PC'
  AND maker NOT IN (
      SELECT maker
      FROM product
      WHERE type = 'Laptop'
  );

-- 9
SELECT DISTINCT p.maker
FROM pc
JOIN product p ON pc.model = p.model
WHERE pc.speed >= 450;

-- 10
SELECT model, price
FROM printer
WHERE price = (SELECT MAX(price) FROM printer);

-- 11
SELECT AVG(speed) AS avg_speed
FROM pc;

-- 12
SELECT AVG(speed) AS avg_speed
FROM laptop
WHERE price > 1000;

-- 13
SELECT AVG(pc.speed) AS avg_speed
FROM pc
JOIN product p ON pc.model = p.model
WHERE p.maker = 'A';

-- 14
SELECT speed, AVG(price) AS avg_price
FROM pc
GROUP BY speed;

-- 15
SELECT hd
FROM pc
GROUP BY hd
HAVING COUNT(*) >= 2;

-- 16
SELECT p1.model AS model_high,
       p2.model AS model_low,
       p1.speed,
       p1.ram
FROM pc p1
JOIN pc p2
  ON p1.speed = p2.speed
 AND p1.ram   = p2.ram
 AND p1.model > p2.model;

-- 17
SELECT 'Laptop' AS type, model, speed
FROM laptop
WHERE speed < ALL (SELECT speed FROM pc);

-- 18
SELECT p.maker, pr.price
FROM printer pr
JOIN product p ON pr.model = p.model
WHERE pr.color = 'y'
  AND pr.price = (
      SELECT MIN(price)
      FROM printer
      WHERE color = 'y'
  );

-- 19
SELECT p.maker, AVG(l.screen) AS avg_screen
FROM laptop l
JOIN product p ON l.model = p.model
GROUP BY p.maker;

-- 20
SELECT p.maker, COUNT(*) AS model_count
FROM pc
JOIN product p ON pc.model = p.model
GROUP BY p.maker
HAVING COUNT(*) >= 3;

-- 21
SELECT p.maker, MAX(pc.price) AS max_price
FROM pc
JOIN product p ON pc.model = p.model
GROUP BY p.maker;

-- 22
SELECT speed, AVG(price) AS avg_price
FROM pc
WHERE speed > 600
GROUP BY speed;

-- 23
SELECT DISTINCT p.maker
FROM product p
WHERE p.maker IN (
    SELECT p1.maker
    FROM pc pc
    JOIN product p1 ON pc.model = p1.model
    WHERE pc.speed >= 750
)
AND p.maker IN (
    SELECT p2.maker
    FROM laptop l
    JOIN product p2 ON l.model = p2.model
    WHERE l.speed >= 750
);

-- 24
SELECT model
FROM (
    SELECT model, price FROM pc
    UNION ALL
    SELECT model, price FROM laptop
    UNION ALL
    SELECT model, price FROM printer
) t
WHERE price = (
    SELECT MAX(price)
    FROM (
        SELECT price FROM pc
        UNION ALL
        SELECT price FROM laptop
        UNION ALL
        SELECT price FROM printer
    ) x
);

-- 25
SELECT DISTINCT p.maker
FROM product p
WHERE p.type = 'Printer'
AND p.maker IN (
    SELECT p2.maker
    FROM pc pc
    JOIN product p2 ON pc.model = p2.model
    WHERE pc.ram = (SELECT MIN(ram) FROM pc)
      AND pc.speed = (
          SELECT MAX(speed)
          FROM pc
          WHERE ram = (SELECT MIN(ram) FROM pc)
      )
);

