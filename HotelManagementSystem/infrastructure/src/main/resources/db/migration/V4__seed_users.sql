INSERT INTO users (username, password, role)
SELECT 'admin',
       '$2a$10$Dow1RrZQK3Yk9Yk9Yk9YkO8p8P3Yk9Yk9Yk9Yk9Yk9Yk9Yk9Yk',
       'ADMIN' WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

INSERT INTO users (username, password, role)
SELECT 'user',
       '$2a$10$u8l5jFZ1pD5k9F8j9s5LkeN0VJp7eM0tQwZfH9aB5dH4G8k1L0mC',
       'USER' WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'user'
);