# Complete DevOps CI/CD Pipeline Documentation

## Project Overview

This project demonstrates a complete DevOps CI/CD pipeline for a **Task Management Application** using industry-standard tools and practices. The application provides comprehensive task management capabilities with full CRUD operations, task filtering, search functionality, and real-time monitoring.

### Student ID: 22BLC1051

---

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Developer     │───▶│   Git Repository │───▶│   Jenkins CI/CD │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Terraform     │    │   Ansible       │    │   Docker Build  │
│ (Infrastructure)│    │ (Configuration) │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Grafana       │◄───│   Graphite      │◄───│  Application    │
│ (Visualization) │    │ (Metrics)       │    │ (Spring Boot)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🛠️ Technology Stack

### Core Application
- **Language**: Java 11
- **Framework**: Spring Boot 2.7.0
- **Database**: H2 In-Memory
- **Build Tool**: Maven
- **Testing**: JUnit 5

### DevOps Tools
- **Version Control**: Git
- **CI/CD**: Jenkins
- **Containerization**: Docker & Docker Compose
- **Infrastructure as Code**: Terraform
- **Configuration Management**: Ansible
- **Monitoring**: Graphite + Grafana
- **Load Balancing**: Nginx

---

## 📋 Prerequisites

Before running the pipeline, ensure you have the following tools installed:

```bash
# Java 11
java -version

# Maven
mvn -version

# Docker
docker --version

# Docker Compose
docker compose version

# Git
git --version
```

---

## 🚀 Quick Start

### Option 1: Run Complete Pipeline Script

```bash
cd /home/nitesh/22BLC1051
./run-pipeline.sh
```

### Option 2: Manual Step-by-Step Execution

#### Step 1: Build and Test
```bash
cd /home/nitesh/22BLC1051
mvn clean compile
mvn test
mvn package -DskipTests
```

#### Step 2: Docker Build
```bash
docker build -t devops-pipeline-app:latest -f docker/Dockerfile .
```

#### Step 3: Start Infrastructure
```bash
cd docker
docker compose up -d
```

#### Step 4: Verify Deployment
```bash
curl http://localhost:8080/api/tasks/health
```

---

## 📊 Service URLs

| Service | URL | Credentials |
|---------|-----|-------------|
| Task Manager Application | http://localhost:8080/api/tasks | - |
| Health Check | http://localhost:8080/api/tasks/health | - |
| Task Statistics | http://localhost:8080/api/tasks/statistics | - |
| H2 Database Console | http://localhost:8080/h2-console | sa / (empty) |
| Grafana Dashboard | http://localhost:3000 | admin / admin123 |
| Graphite | http://localhost:8001 | - |
| Jenkins | http://localhost:8081 | - |
| Load Balancer | http://localhost:80 | - |

---

## 🔧 Task Manager Backend & API Documentation

### What the Task Manager Does

The Task Manager is a comprehensive task management system that provides:

#### Core Functionality
- **Task Creation & Management**: Create, update, delete, and retrieve tasks
- **Status Tracking**: Track tasks through different lifecycle states (Pending, In Progress, Completed, Cancelled)
- **Priority Management**: Assign and filter tasks by priority levels (Low, Medium, High, Urgent)
- **Assignment Management**: Assign tasks to specific users and track ownership
- **Due Date Management**: Set and track task deadlines with overdue detection
- **Search & Filtering**: Advanced search capabilities by title, description, status, priority, and assignee
- **Statistics & Reporting**: Real-time task statistics and metrics
- **Audit Trail**: Automatic tracking of creation and modification timestamps

#### Business Logic Features
- **Overdue Task Detection**: Automatically identifies tasks past their due date
- **High Priority Alerts**: Special endpoint for critical pending tasks
- **Task Completion Workflow**: Streamlined process for marking tasks as complete
- **Comprehensive Validation**: Input validation for all task fields
- **Logging & Monitoring**: Detailed logging for all operations with metrics collection

### Task Model Structure

The Task entity contains the following fields:
- **id**: Unique identifier (auto-generated)
- **title**: Task title (required, 1-100 characters)
- **description**: Detailed description (optional, max 500 characters)
- **status**: Current status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- **priority**: Task priority (LOW, MEDIUM, HIGH, URGENT)
- **createdAt**: Automatic creation timestamp
- **updatedAt**: Automatic last modification timestamp
- **dueDate**: Optional due date for the task
- **assignedTo**: User assigned to the task (optional, max 100 characters)

### Complete API Endpoints

#### Health Check
```bash
# Check if the Task Manager API is running
GET /api/tasks/health
curl http://localhost:8080/api/tasks/health
```

#### Basic CRUD Operations

##### Create a New Task
```bash
POST /api/tasks
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implement CI/CD Pipeline",
    "description": "Set up Jenkins pipeline for automated deployment",
    "priority": "HIGH",
    "status": "PENDING",
    "dueDate": "2024-07-15 18:00:00",
    "assignedTo": "john.doe@company.com"
  }'
```

##### Get All Tasks
```bash
GET /api/tasks
curl http://localhost:8080/api/tasks
```

##### Get Task by ID
```bash
GET /api/tasks/{id}
curl http://localhost:8080/api/tasks/1
```

##### Update Task
```bash
PUT /api/tasks/{id}
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Task Title",
    "description": "Updated description",
    "status": "IN_PROGRESS",
    "priority": "URGENT"
  }'
```

##### Delete Task
```bash
DELETE /api/tasks/{id}
curl -X DELETE http://localhost:8080/api/tasks/1
```

#### Filtering & Search Operations

##### Get Tasks by Status
```bash
GET /api/tasks/status/{status}
# Available statuses: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
curl http://localhost:8080/api/tasks/status/PENDING
curl http://localhost:8080/api/tasks/status/IN_PROGRESS
curl http://localhost:8080/api/tasks/status/COMPLETED
```

##### Get Tasks by Priority
```bash
GET /api/tasks/priority/{priority}
# Available priorities: LOW, MEDIUM, HIGH, URGENT
curl http://localhost:8080/api/tasks/priority/HIGH
curl http://localhost:8080/api/tasks/priority/URGENT
```

##### Get Tasks by Assigned User
```bash
GET /api/tasks/assigned/{assignedTo}
curl http://localhost:8080/api/tasks/assigned/john.doe@company.com
```

##### Search Tasks by Title
```bash
GET /api/tasks/search/title?keyword={keyword}
curl "http://localhost:8080/api/tasks/search/title?keyword=pipeline"
curl "http://localhost:8080/api/tasks/search/title?keyword=deployment"
```

##### Search Tasks by Description
```bash
GET /api/tasks/search/description?keyword={keyword}
curl "http://localhost:8080/api/tasks/search/description?keyword=jenkins"
curl "http://localhost:8080/api/tasks/search/description?keyword=docker"
```

#### Special Operations

##### Get Overdue Tasks
```bash
GET /api/tasks/overdue
curl http://localhost:8080/api/tasks/overdue
```

##### Get High Priority Pending Tasks
```bash
GET /api/tasks/high-priority-pending
curl http://localhost:8080/api/tasks/high-priority-pending
```

##### Mark Task as Completed
```bash
PATCH /api/tasks/{id}/complete
curl -X PATCH http://localhost:8080/api/tasks/1/complete
```

##### Get Task Statistics
```bash
GET /api/tasks/statistics
curl http://localhost:8080/api/tasks/statistics

# Returns comprehensive statistics:
{
  "totalTasks": 25,
  "pendingTasks": 8,
  "inProgressTasks": 5,
  "completedTasks": 10,
  "cancelledTasks": 2
}
```

### Example API Responses

#### Single Task Response
```json
{
  "id": 1,
  "title": "Implement CI/CD Pipeline",
  "description": "Set up Jenkins pipeline for automated deployment",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "createdAt": "2024-07-06 10:30:00",
  "updatedAt": "2024-07-06 14:45:00",
  "dueDate": "2024-07-15 18:00:00",
  "assignedTo": "john.doe@company.com"
}
```

#### Task List Response
```json
[
  {
    "id": 1,
    "title": "Setup Docker Environment",
    "status": "COMPLETED",
    "priority": "HIGH",
    "assignedTo": "alice@company.com"
  },
  {
    "id": 2,
    "title": "Configure Grafana Dashboards",
    "status": "PENDING",
    "priority": "MEDIUM",
    "assignedTo": "bob@company.com"
  }
]
```

---

## 🐳 Docker Commands

### Start Services
```bash
cd docker
docker compose up -d
```

### Stop Services
```bash
docker compose down
```

### View Logs
```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f pipeline-app
```

### Restart Services
```bash
docker compose restart
```

### Check Service Status
```bash
docker compose ps
```

---

## 🏗️ Infrastructure as Code

### Terraform (AWS Infrastructure)

#### Initialize Terraform
```bash
cd infrastructure/terraform
terraform init
```

#### Plan Infrastructure Changes
```bash
terraform plan
```

#### Apply Infrastructure
```bash
terraform apply
```

#### Destroy Infrastructure
```bash
terraform destroy
```

### Ansible (Configuration Management)

#### Run Deployment Playbook
```bash
cd infrastructure/ansible
ansible-playbook -i inventory deploy.yml --ask-become-pass
```

#### Check Playbook Syntax
```bash
ansible-playbook deploy.yml --syntax-check
```

#### Dry Run
```bash
ansible-playbook -i inventory deploy.yml --check
```

---

## 📈 Monitoring and Observability

### Grafana Setup

1. Access Grafana at http://localhost:3000
2. Login with admin/admin123
3. Add Graphite datasource:
   - URL: http://graphite:80
   - Access: Server (default)

### Metrics Collection

The application automatically sends metrics to Graphite:
- HTTP request metrics
- JVM metrics
- Custom application metrics
- Database connection metrics

### Sample Metrics Queries

- `stats.gauges.pipeline-app.http.server.requests`
- `stats.gauges.pipeline-app.jvm.memory.used`
- `stats.counters.pipeline-app.database.connections.active`

---

## 🧪 Testing Strategy

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn integration-test
```

### Code Coverage
```bash
mvn test jacoco:report
# View report at target/site/jacoco/index.html
```

### Performance Testing
```bash
# Simple load test
for i in {1..100}; do
  curl -s http://localhost:8080/api/health > /dev/null &
done
wait
```

---

## 🔄 CI/CD Pipeline Flow

### 1. Source Code Management
- Developer commits code to Git repository
- Webhook triggers Jenkins pipeline

### 2. Build Stage
- Maven compiles the application
- Dependencies are resolved
- Artifacts are created

### 3. Test Stage
- Unit tests executed with JUnit
- Code coverage reports generated
- Integration tests run

### 4. Quality Gates
- Code quality analysis with Checkstyle and PMD
- Security scanning with OWASP dependency check
- Test coverage thresholds enforced

### 5. Package Stage
- Docker image built
- Image tagged with build number
- Image pushed to registry

### 6. Deploy Stage
- Infrastructure provisioned with Terraform
- Configuration applied with Ansible
- Application deployed to target environment

### 7. Monitor Stage
- Health checks performed
- Metrics collected by Graphite
- Dashboards updated in Grafana

---

## 🛡️ Security Considerations

### Application Security
- Input validation and sanitization
- SQL injection prevention with JPA
- CORS configuration for API access
- Actuator endpoints secured

### Infrastructure Security
- Non-root Docker containers
- Network isolation with Docker networks
- Secrets management with environment variables
- Regular security updates

### CI/CD Security
- Dependency vulnerability scanning
- Container image scanning
- Secure credential management in Jenkins
- Branch protection rules

---

## 📁 Project Structure

```
22BLC1051/
├── src/
│   ├── main/
│   │   ├── java/com/devops/
│   │   │   ├── DevOpsApplication.java
│   │   │   ├── ResourceController.java
│   │   │   ├── ResourceService.java
│   │   │   ├── ResourceRepository.java
│   │   │   └── Resource.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/devops/
│           ├── ResourceServiceTest.java
│           └── ResourceControllerTest.java
├── docker/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── grafana/
│   │   ├── datasources/
│   │   └── dashboards/
│   └── nginx/
│       └── nginx.conf
├── infrastructure/
│   ├── terraform/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   └── ansible/
│       ├── deploy.yml
│       └── inventory
├── jenkins/
│   └── Jenkinsfile
├── pom.xml
├── README.md
├── documentation.md
└── run-pipeline.sh
```

---

## 🐛 Troubleshooting

### Common Issues

#### Application Won't Start
```bash
# Check logs
docker compose logs pipeline-app

# Verify Java version
java -version

# Check port availability
netstat -tlnp | grep 8080
```

#### Docker Issues
```bash
# Restart Docker daemon
sudo systemctl restart docker

# Clean up containers
docker system prune -f

# Check disk space
df -h
```

#### Service Connectivity Issues
```bash
# Check network
docker network ls
docker network inspect docker_devops-network

# Test connectivity
docker exec docker-pipeline-app-1 ping graphite
```

### Performance Issues
```bash
# Monitor resource usage
docker stats

# Check system resources
htop
free -h
```

---

## 📝 Maintenance

### Regular Tasks

#### Update Dependencies
```bash
mvn versions:display-dependency-updates
```

#### Security Updates
```bash
mvn org.owasp:dependency-check-maven:check
```

#### Backup Data
```bash
# Run backup script
sudo /usr/local/bin/backup-pipeline-app
```

#### Log Rotation
```bash
# Check log sizes
du -sh /var/lib/docker/containers/*/*-json.log

# Manual cleanup if needed
docker system prune --volumes
```

---

## 🎯 Next Steps

### Production Readiness

1. **Database Migration**
   - Replace H2 with PostgreSQL/MySQL
   - Implement database migrations
   - Add connection pooling

2. **Security Enhancements**
   - Implement OAuth2/JWT authentication
   - Add rate limiting
   - Enable HTTPS/TLS

3. **Scalability**
   - Implement horizontal scaling
   - Add caching layer (Redis)
   - Database read replicas

4. **Advanced Monitoring**
   - Distributed tracing with Jaeger
   - Log aggregation with ELK stack
   - Advanced alerting rules

### Cloud Deployment

1. **AWS Deployment**
   - Use provided Terraform configs
   - Deploy to ECS/EKS
   - Implement Auto Scaling Groups

2. **CI/CD Enhancements**
   - Blue-green deployments
   - Canary releases
   - Automated rollbacks

---

## 📞 Support

For questions or issues:
- Check the troubleshooting section
- Review application logs
- Verify all prerequisites are met
- Ensure all services are running

---

## 📄 License

This project is for educational purposes as part of the DevOps curriculum.

**Student ID**: 22BLC1051
**Project**: Complete DevOps CI/CD Pipeline Implementation
**Technology Stack**: Java, Spring Boot, Docker, Jenkins, Terraform, Ansible, Graphite, Grafana
