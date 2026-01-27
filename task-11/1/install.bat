@echo off
set DB_NAME=hotel_db
set DB_USER=postgres

echo === Creating database (if not exists) ===
psql -U %DB_USER% -tc "SELECT 1 FROM pg_database WHERE datname='%DB_NAME%'" | find "1" > nul
if errorlevel 1 (
    createdb -U %DB_USER% %DB_NAME%
    echo Database %DB_NAME% created
) else (
    echo Database %DB_NAME% already exists
)

echo === Applying DDL ===
psql -U %DB_USER% -d %DB_NAME% -f ddl.sql

echo === Applying DML ===
psql -U %DB_USER% -d %DB_NAME% -f dml.sql

echo === Done ===
pause
