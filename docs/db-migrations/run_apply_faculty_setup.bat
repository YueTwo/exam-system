@echo off
REM Check existence of columns and count existing FACULTY rows
mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 -N -e "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='yunfan' AND TABLE_NAME='sys_depart' AND COLUMN_NAME='category';" > "%~dp0cat_exists.txt"
mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 -N -e "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='yunfan' AND TABLE_NAME='sys_depart' AND COLUMN_NAME='faculty_name';" > "%~dp0fac_exists.txt"
mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 -N -e "SELECT COUNT(*) FROM sys_depart WHERE category='FACULTY';" yunfan > "%~dp0existing_fac_count.txt"
echo Checks written to %~dp0*.txt

if "%1"=="apply" (
  echo Applying ALTERs (errors ignored)...
  mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 -e "ALTER TABLE sys_depart ADD COLUMN category VARCHAR(64) DEFAULT 'FACULTY';" yunfan 2>nul
  mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 -e "ALTER TABLE sys_depart ADD COLUMN faculty_name VARCHAR(255) DEFAULT NULL;" yunfan 2>nul
  echo Running insert script...
  mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 yunfan < "%~dp0\20251122_insert_default_faculties.sql"
  echo Done apply.
) else (
  echo Run with argument "apply" to perform ALTER + INSERT.
)
