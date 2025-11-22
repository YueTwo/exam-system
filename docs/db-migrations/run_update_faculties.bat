@echo off
mysql -h114.214.236.207 -P13306 -u yf -pyf20251122 yunfan < "%~dp0\20251122_update_faculties.sql"
exit /b %errorlevel%
