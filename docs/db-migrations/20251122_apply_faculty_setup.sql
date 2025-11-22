-- Apply faculty setup: add columns if missing, then insert default faculties
-- Date: 2025-11-22

ALTER TABLE sys_depart ADD COLUMN category VARCHAR(64) DEFAULT 'FACULTY';
ALTER TABLE sys_depart ADD COLUMN faculty_name VARCHAR(255) DEFAULT NULL;

-- Insert default faculties
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
