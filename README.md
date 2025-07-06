# DevOps CI/CD Pipeline Project - Complete Implementation

## Architecture Overview

This project demonstrates a complete DevOps CI/CD pipeline implementation using:
- **Java Spring Boot** - Main application framework
- **Maven** - Build automation and dependency management
- **JUnit** - Unit testing framework
- **Docker** - Application containerization
- **Jenkins** - Continuous Integration/Continuous Deployment
- **Terraform** - Infrastructure as Code
- **Ansible** - Configuration management and deployment
- **Graphite** - Metrics collection and storage
- **Grafana** - Monitoring dashboards and visualization
- **Git** - Version control system

## Project Structure

```
22BLC1051/
├── src/
│   ├── main/java/com/devops/          # Main application code
│   ├── test/java/com/devops/          # JUnit test suites
│   └── main/resources/                # Application configuration
├── docker/                            # Docker configurations
├── jenkins/                           # Jenkins pipeline scripts
├── infrastructure/
│   ├── terraform/                     # Infrastructure as Code
│   └── ansible/                       # Configuration management
├── monitoring/                        # Monitoring setup
└── scripts/                           # Utility and automation scripts
```

## Step-by-Step Execution Commands

### Prerequisites Installation

```bash
# Update system packages
sudo apt update && sudo apt upgrade -y

# Install Java 11
sudo apt install openjdk-11-jdk -y

# Install Maven
sudo apt install maven -y

# Install Docker
sudo apt install docker.io -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER

# Install Docker Compose
sudo apt install docker-compose -y

# Install Git (if not already installed)
sudo apt install git -y

# Verify installations
java -version
mvn -version
docker --version
docker-compose --version
```

### 1. Build and Test the Application

```bash
# Navigate to project directory
cd /home/nitesh/22BLC1051

# Clean and compile the project
mvn clean compile

# Run unit tests
mvn test

# Package the application
mvn package -DskipTests

# View test results
ls -la target/surefire-reports/
```

### 2. Build and Run Docker Containers

```bash
# Build the Docker image
docker build -t devops-pipeline-app:latest -f docker/Dockerfile .

# Start all services with Docker Compose
cd docker
docker-compose up -d

# Check running containers
docker ps

# View logs
docker-compose logs -f pipeline-app
```

### 3. Test the Application

```bash
# Health check
curl http://localhost:8080/api/health

# Create a test resource
curl -X POST http://localhost:8080/api/resources \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Resource",
    "description": "This is a test resource for the DevOps pipeline.",
    "category": "Testing"
  }'

# Get all resources
curl http://localhost:8080/api/resources

# Get resource count
curl http://localhost:8080/api/resources/count
```

### 4. Access Monitoring Dashboards

```bash
# Grafana Dashboard
echo "Grafana: http://localhost:3000"
echo "Username: admin"
echo "Password: admin123"

# Graphite Web Interface
echo "Graphite: http://localhost:8001"

# Jenkins (if running)
echo "Jenkins: http://localhost:8081"

# Application Health
echo "Application Health: http://localhost:8080/api/health"

# Nginx Load Balancer
echo "Load Balancer: http://localhost:80"
```

### 5. Infrastructure Management with Terraform (Optional)

```bash
# Initialize Terraform
cd infrastructure/terraform
terraform init

# Plan infrastructure changes
terraform plan

# Apply infrastructure (requires AWS credentials)
# terraform apply

# View outputs
# terraform output
```

### 6. Configuration Management with Ansible

```bash
# Install Ansible (if not installed)
sudo apt install ansible -y

# Run the deployment playbook
cd infrastructure/ansible
ansible-playbook -i inventory deploy.yml --ask-become-pass

# Check playbook syntax
ansible-playbook deploy.yml --syntax-check

# Run in dry-run mode
ansible-playbook -i inventory deploy.yml --check
```

### 7. Monitoring and Logging

```bash
# View application logs
docker logs docker_pipeline-app_1

# View Grafana logs
docker logs docker_grafana_1

# View Graphite logs
docker logs docker_graphite_1

# System monitoring script
sudo /usr/local/bin/system-status

# View backup logs
tail -f /var/log/backup.log
```

### 8. Database Operations (H2 Console)

```bash
# Access H2 Database Console
echo "H2 Console: http://localhost:8080/h2-console"
echo "JDBC URL: jdbc:h2:mem:devopsdb"
echo "Username: sa"
echo "Password: (leave empty)"
```

### 9. Performance Testing

```bash
# Simple load test
for i in {1..50}; do
  curl -s http://localhost:8080/api/health > /dev/null &
done
wait
echo "Load test completed"

# Monitor resource usage during load test
htop
```

### 10. Cleanup Commands

```bash
# Stop all containers
docker-compose down

# Remove containers and volumes
docker-compose down -v

# Remove Docker images
docker rmi devops-pipeline-app:latest

# Clean Maven artifacts
mvn clean

# Remove all unused Docker resources
docker system prune -a -f
```

## Troubleshooting Commands

```bash
# Check if ports are available
sudo netstat -tlnp | grep -E ':(8080|3000|8001|80)'

# Check Docker service status
sudo systemctl status docker

# Check container resource usage
docker stats

# Check application logs for errors
docker logs docker_pipeline-app_1 | grep ERROR

# Restart specific service
docker-compose restart pipeline-app

# Check disk space
df -h

# Check memory usage
free -h
```

## Pipeline Flow

1. **Source Code Management**: Git repository with Spring Boot application
2. **Build**: Maven compiles and packages the application
3. **Testing**: JUnit runs unit and integration tests
4. **Code Quality**: Static analysis with Checkstyle and PMD
5. **Containerization**: Docker builds application image
6. **Infrastructure**: Terraform provisions cloud resources
7. **Deployment**: Ansible configures and deploys application
8. **Monitoring**: Graphite collects metrics, Grafana visualizes them
9. **Load Balancing**: Nginx distributes traffic
10. **Health Checks**: Automated monitoring of application status

## Key Features

- **Microservices Architecture**: Spring Boot REST API
- **Containerized Deployment**: Docker and Docker Compose
- **Infrastructure as Code**: Terraform for AWS resources
- **Configuration Management**: Ansible playbooks
- **Continuous Monitoring**: Graphite + Grafana stack
- **Automated Testing**: JUnit with coverage reports
- **Security Scanning**: OWASP dependency check
- **Load Balancing**: Nginx reverse proxy
- **Health Checks**: Built-in application health endpoints
- **Backup & Recovery**: Automated backup scripts

## URLs for Screenshots

- Application: http://localhost:8080/api/resources
- Health Check: http://localhost:8080/api/health
- H2 Console: http://localhost:8080/h2-console
- Grafana: http://localhost:3000 (admin/admin123)
- Graphite: http://localhost:8001
- Load Balancer: http://localhost:80
- Jenkins: http://localhost:8081 (when running)
