@echo off
setlocal EnableExtensions EnableDelayedExpansion

REM ==============================
REM Paths
REM ==============================
set SCRIPT_DIR=%~dp0
set ENV_FILE=%SCRIPT_DIR%.env

REM ==============================
REM Default flags
REM ==============================
set RECREATE_DB=false
set DDL_ONLY=false
set DML_ONLY=false
set SILENT=false

REM ==============================
REM Parse arguments
REM ==============================
for %%A in (%*) do (
    if "%%A"=="--recreate-db" set RECREATE_DB=true
    if "%%A"=="--ddl-only" set DDL_ONLY=true
    if "%%A"=="--dml-only" set DML_ONLY=true
    if "%%A"=="--silent" set SILENT=true
)

REM ==============================
REM Load .env
REM ==============================
if not exist "%ENV_FILE%" (
    echo ERROR: .env file not found
    pause
    exit /b 1
)

for /f "usebackq tokens=1,2 delims==" %%A in ("%ENV_FILE%") do (
    set %%A=%%B
)

REM ==============================
REM Resolve paths
REM ==============================
set DDL_PATH=%SCRIPT_DIR%%DDL_FILE%
set DML_PATH=%SCRIPT_DIR%%DML_FILE%
set LOG_PATH=%SCRIPT_DIR%%LOG_FILE%

REM ==============================
REM Logging helper
REM ==============================
if "%SILENT%"=="true" (
    set ECHO_CMD=REM
) else (
    set ECHO_CMD=echo
)

REM ==============================
REM Ask password
REM ==============================
%ECHO_CMD% Enter PostgreSQL password for %DB_USER%:
set /p PGPASSWORD=

REM ==============================
REM Log header
REM ==============================
echo =============================== >> "%LOG_PATH%"
echo Install started at %DATE% %TIME% >> "%LOG_PATH%"
echo Flags: recreate=%RECREATE_DB% ddl-only=%DDL_ONLY% dml-only=%DML_ONLY% silent=%SILENT% >> "%LOG_PATH%"
echo =============================== >> "%LOG_PATH%"

REM ==============================
REM File checks
REM ==============================
if "%DML_ONLY%"=="false" if not exist "%DDL_PATH%" (
    echo ERROR: ddl.sql not found
    exit /b 1
)

if "%DDL_ONLY%"=="false" if not exist "%DML_PATH%" (
    echo ERROR: dml.sql not found
    exit /b 1
)

REM ==============================
REM DB create / recreate
REM ==============================
if "%DDL_ONLY%"=="false" if "%DML_ONLY%"=="false" (
    if "%RECREATE_DB%"=="true" (
        %ECHO_CMD% Recreating database %DB_NAME%...
        set START_TIME=%TIME%

        dropdb -U %DB_USER% --if-exists %DB_NAME% >> "%LOG_PATH%" 2>&1
        createdb -U %DB_USER% %DB_NAME% >> "%LOG_PATH%" 2>&1

        set END_TIME=%TIME%
        echo DB recreate time: %START_TIME% - %END_TIME% >> "%LOG_PATH%"
    ) else (
        %ECHO_CMD% Creating database if not exists...
        psql -U %DB_USER% -tc "SELECT 1 FROM pg_database WHERE datname='%DB_NAME%'" | find "1" > nul
        if errorlevel 1 (
            createdb -U %DB_USER% %DB_NAME% >> "%LOG_PATH%" 2>&1
        )
    )
)

REM ==============================
REM Apply DDL
REM ==============================
if "%DML_ONLY%"=="false" (
    %ECHO_CMD% Applying DDL...
    set START_TIME=%TIME%

    psql -U %DB_USER% -d %DB_NAME% -f "%DDL_PATH%" >> "%LOG_PATH%" 2>&1
    if errorlevel 1 exit /b 1

    set END_TIME=%TIME%
    echo DDL time: %START_TIME% - %END_TIME% >> "%LOG_PATH%"
)

REM ==============================
REM Apply DML
REM ==============================
if "%DDL_ONLY%"=="false" (
    %ECHO_CMD% Applying DML...
    set START_TIME=%TIME%

    psql -U %DB_USER% -d %DB_NAME% -f "%DML_PATH%" >> "%LOG_PATH%" 2>&1
    if errorlevel 1 exit /b 1

    set END_TIME=%TIME%
    echo DML time: %START_TIME% - %END_TIME% >> "%LOG_PATH%"
)

REM ==============================
REM Finish
REM ==============================
%ECHO_CMD% Done successfully
echo Finished at %DATE% %TIME% >> "%LOG_PATH%"
echo =============================== >> "%LOG_PATH%"

if "%SILENT%"=="false" pause
