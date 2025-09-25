-- Create target database if not exists (uses Flyway placeholder ${db})
CREATE DATABASE IF NOT EXISTS `${db}`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Create roles table
CREATE TABLE IF NOT EXISTS `${db}`.`roles` (
                                               `role_id` BIGINT NOT NULL AUTO_INCREMENT,
                                               `name` VARCHAR(100) NOT NULL,
                                               `description` VARCHAR(255) NULL,
                                               PRIMARY KEY (`role_id`)
) ENGINE=InnoDB;

-- Create users table (aligned with UserEntity / UserDTO)
CREATE TABLE IF NOT EXISTS `${db}`.`users` (
                                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                                               `document_number` VARCHAR(50) NOT NULL,
                                               `first_name` VARCHAR(100) NOT NULL,
                                               `last_name` VARCHAR(100) NOT NULL,
                                               `birth_date` DATE NULL,
                                               `address` VARCHAR(200) NULL,
                                               `phone_number` VARCHAR(50) NULL,
                                               `email` VARCHAR(150) NOT NULL,
                                               `base_salary` DECIMAL(15,2) NULL,
                                               `role_id` BIGINT NULL,
                                               PRIMARY KEY (`id`),
                                               CONSTRAINT `fk_users_roles` FOREIGN KEY (`role_id`)
                                                   REFERENCES `${db}`.`roles`(`role_id`)
                                                   ON UPDATE CASCADE
                                                   ON DELETE SET NULL
) ENGINE=InnoDB;

-- Helpful index for foreign key
CREATE INDEX `idx_users_role_id` ON `${db}`.`users` (`role_id`);

-- Ensure email is unique (business rule: no two users with same email)
CREATE UNIQUE INDEX `uk_users_email` ON `${db}`.`users` (`email`);

-- Ensure document_number is unique (business rule: no two users with same document)
CREATE UNIQUE INDEX `uk_users_document_number` ON `${db}`.`users` (`document_number`);
