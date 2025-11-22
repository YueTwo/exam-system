-- Update existing FACULTY rows to standardized faculty names and codes
-- Generated: 2025-11-22
-- NOTE: This file only updates specific IDs found in the database. Review before running.

-- Rule applied: keyword-based mapping
-- Keywords mapping -> faculty_name / dept_code
-- (前端|后端|技术|产品|测试|开发|工程) -> 计算机系 / CS
-- (自动化) -> 自动化系 / AUTO
-- (电子) -> 电子工程系 / EE
-- (电气) -> 电气工程系 / EEE
-- (能源|动力) -> 能源与动力系 / ENERGY
-- (数|数学) -> 数学系 / MATH
-- (物理) -> 物理系 / PHYS
-- (化学) -> 化学系 / CHEM
-- (医|医学) -> 医学系 / MED
-- (体|体育) -> 体育系 / SPORT
-- Fallback -> 计算机系 / CS

-- The following explicit updates were generated for current FACULTY records:

UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302853644578000898';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302855994843676674';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302856266567467010';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1318103313740320770';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302855940200284161';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302856320602685442';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1318103339229106178';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302856017283203073';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1318103362494910465';
UPDATE sys_depart SET faculty_name='计算机系', dept_code='CS', category='FACULTY' WHERE id='1302856084475953154';

-- End of updates
