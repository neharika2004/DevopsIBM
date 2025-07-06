-- Task Management Database Initialization Script

CREATE DATABASE IF NOT EXISTS taskmanager_db;

USE taskmanager_db;

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    due_date TIMESTAMP NULL,
    assigned_to VARCHAR(100)
);

-- Insert sample data
INSERT INTO tasks (title, description, status, priority, assigned_to) VALUES
('Setup CI/CD Pipeline', 'Configure Jenkins, Docker, and deployment automation', 'IN_PROGRESS', 'HIGH', 'devops-team'),
('Implement User Authentication', 'Add JWT-based authentication to the API', 'PENDING', 'HIGH', 'backend-dev'),
('Create API Documentation', 'Generate OpenAPI/Swagger documentation', 'PENDING', 'MEDIUM', 'tech-writer'),
('Setup Monitoring', 'Configure Grafana dashboards and alerting', 'COMPLETED', 'MEDIUM', 'sre-team'),
('Database Migration', 'Migrate from H2 to MySQL in production', 'PENDING', 'LOW', 'dba-team');

-- Create user with appropriate permissions
CREATE USER IF NOT EXISTS 'taskmanager'@'%' IDENTIFIED BY 'taskmanager123';
GRANT ALL PRIVILEGES ON taskmanager_db.* TO 'taskmanager'@'%';
FLUSH PRIVILEGES;
