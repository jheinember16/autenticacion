-- Create target database if not exists (uses Flyway placeholder ${db})
CREATE DATABASE IF NOT EXISTS `${db}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create roles table
CREATE TABLE IF NOT EXISTS `${db}`.`roles` (
                                               `rol_id` BIGINT NOT NULL AUTO_INCREMENT,
                                               `nombre` VARCHAR(100) NOT NULL,
                                               `descripcion` VARCHAR(255) NULL,
                                               PRIMARY KEY (`rol_id`)
) ENGINE=InnoDB;

-- Create users table (aligned with UserEntity)
CREATE TABLE IF NOT EXISTS `${db}`.`users` (
                                               `user_id` BIGINT NOT NULL AUTO_INCREMENT,
                                               `document_number` VARCHAR(50) NOT NULL,
                                               `first_name` VARCHAR(100) NOT NULL,
                                               `last_name` VARCHAR(100) NOT NULL,
                                               `birth_date` DATE NULL,
                                               `address` VARCHAR(200) NULL,
                                               `phone_number` VARCHAR(50) NULL,
                                               `email` VARCHAR(150) NULL,
                                               `base_salary` DECIMAL(15,2) NULL,
                                               `rol_id` BIGINT NULL,
                                               PRIMARY KEY (`user_id`),
                                               CONSTRAINT `fk_users_roles` FOREIGN KEY (`rol_id`)
                                                   REFERENCES `${db}`.`roles`(`rol_id`)
                                                   ON UPDATE CASCADE
                                                   ON DELETE SET NULL
) ENGINE=InnoDB;

-- Helpful index for FK
CREATE INDEX `idx_users_rol_id` ON `${db}`.`users` (`rol_id`);
