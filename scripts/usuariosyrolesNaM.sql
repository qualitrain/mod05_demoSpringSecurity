-- Usar la base de datos
USE ejmBDSeguridad;

DROP TABLE IF EXISTS groups;
CREATE TABLE groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL
);

DROP TABLE IF EXISTS group_authorities;
CREATE TABLE group_authorities (
    group_id BIGINT NOT NULL,
    authority VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id) REFERENCES groups(id)
);

DROP TABLE IF EXISTS group_members;
CREATE TABLE group_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    group_id BIGINT NOT NULL,
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES groups(id)
);
