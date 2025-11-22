-- Insert default faculties (院系) into sys_depart
-- Date: 2025-11-22
-- NOTE: 请先备份数据库，再运行此脚本。

INSERT INTO `sys_depart` (`id`,`dept_type`,`parent_id`,`faculty_name`,`dept_code`,`category`,`sort`)
VALUES
  (UUID(), 2, '0', '计算机系', 'CS', 'FACULTY', 1),
  (UUID(), 2, '0', '自动化系', 'AUTO', 'FACULTY', 2),
  (UUID(), 2, '0', '电子工程系', 'EE', 'FACULTY', 3),
  (UUID(), 2, '0', '电气工程系', 'EEE', 'FACULTY', 4),
  (UUID(), 2, '0', '能源与动力系', 'ENERGY', 'FACULTY', 5),
  (UUID(), 2, '0', '数学系', 'MATH', 'FACULTY', 6),
  (UUID(), 2, '0', '物理系', 'PHYS', 'FACULTY', 7),
  (UUID(), 2, '0', '化学系', 'CHEM', 'FACULTY', 8),
  (UUID(), 2, '0', '医学系', 'MED', 'FACULTY', 9),
  (UUID(), 2, '0', '体育系', 'SPORT', 'FACULTY', 10);

-- 如果你希望使用中文编码或其他编码，请编辑 `dept_code` 字段为你需要的值。
-- 运行示例（PowerShell）：
-- mysql -h<host> -P<port> -u<user> -p<password> <database> < .\docs\db-migrations\20251122_insert_default_faculties.sql

-- 运行后，可在管理界面查看是否已添加并按需调整排序或编码。
