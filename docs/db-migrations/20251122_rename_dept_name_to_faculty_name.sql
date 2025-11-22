-- Migration: rename dept_name -> faculty_name
-- Date: 2025-11-22
-- NOTE: Review and backup your database before running.

-- Option A (safer, compatible with older MySQL):
-- 1) add new column
ALTER TABLE `sys_depart` ADD COLUMN `faculty_name` VARCHAR(255) DEFAULT NULL;

-- 2) copy existing data
UPDATE `sys_depart` SET `faculty_name` = `dept_name` WHERE `dept_name` IS NOT NULL;

-- 3) (optional) drop old column after verification
-- ALTER TABLE `sys_depart` DROP COLUMN `dept_name`;

-- Option B (atomic rename, requires MySQL 8+ / MariaDB 10.5+):
-- ALTER TABLE `sys_depart` RENAME COLUMN `dept_name` TO `faculty_name`;

-- Rollback (if you used Option A and dropped the old column, rollback is not trivial):
-- If you used Option A and keep both columns, rollback is not needed.
-- If you used Option B, you can rename back:
-- ALTER TABLE `sys_depart` RENAME COLUMN `faculty_name` TO `dept_name`;

-- After migration you may want to add an index if you query by this column:
-- ALTER TABLE `sys_depart` ADD INDEX idx_faculty_name (`faculty_name`(50));

-- End of migration
