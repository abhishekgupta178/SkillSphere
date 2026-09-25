CREATE DATABASE IF NOT EXISTS skillsphere;
USE skillsphere;

CREATE TABLE IF NOT EXISTS user_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'STUDENT'
);

CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    name VARCHAR(150) NOT NULL,
    contact VARCHAR(50),
    bio VARCHAR(1000),
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    github_url VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS student_skill (
    student_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    level VARCHAR(50),
    PRIMARY KEY (student_id, skill_id),
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_project (
    student_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, project_id),
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);
