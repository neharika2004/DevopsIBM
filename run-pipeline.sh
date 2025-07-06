#!/bin/bash

# DevOps CI/CD Pipeline Execution Script
# Task Management API

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

function print_step() {
    echo -e "${BLUE}STEP $1: $2${NC}"
}

function print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

function print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

function print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Step 1: Build and Test
print_step "1" "Building and Testing the Application"

cd /home/nitesh/22BLC1051

echo "Compiling the application..."
mvn clean compile
if [ $? -eq 0 ]; then
    print_success "Application compiled successfully"
else
    print_error "Compilation failed"
    exit 1
fi

echo "Running unit tests..."
mvn test
if [ $? -eq 0 ]; then
    print_success "All tests passed"
else
    print_error "Tests failed"
    exit 1
fi

echo "Packaging the application..."
mvn package -DskipTests
if [ $? -eq 0 ]; then
    print_success "Application packaged successfully"
else
    print_error "Packaging failed"
    exit 1
fi

# Step 2: Docker Build
print_step "2" "Building Docker Image"

echo "Building Docker image..."
cd docker
docker build -t task-manager-api:latest -f Dockerfile ..
if [ $? -eq 0 ]; then
    print_success "Docker image built successfully"
else
    print_error "Docker build failed"
    exit 1
fi

# Step 3: Infrastructure Deployment
print_step "3" "Deploying Infrastructure with Docker Compose"

echo "Starting all services..."
docker compose up -d
if [ $? -eq 0 ]; then
    print_success "All services started successfully"
else
    print_error "Service deployment failed"
    exit 1
fi

# Step 4: Health Checks
print_step "4" "Performing Health Checks"

echo "Waiting for services to be ready..."
sleep 30

echo "Checking application health..."
HEALTH_CHECK=$(curl -s http://localhost:8080/api/tasks/health)
if [[ "$HEALTH_CHECK" == *"Task Management API is running!"* ]]; then
    print_success "Application is healthy"
else
    print_error "Application health check failed"
    exit 1
fi

# Checking Grafana
GRAFANA_CHECK=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3000)
if [ "$GRAFANA_CHECK" = "200" ]; then
    print_success "Grafana is accessible"
else
    print_warning "Grafana may still be starting up"
fi

# Checking Jenkins
JENKINS_CHECK=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081)
if [ "$JENKINS_CHECK" = "200" ] || [ "$JENKINS_CHECK" = "403" ]; then
    print_success "Jenkins is accessible"
else
    print_warning "Jenkins may still be starting up"
fi

# Step 5: Display Service URLs
print_step "5" "Service URLs and Access Information"

echo ""
echo "============================================="
echo "🎉 PIPELINE DEPLOYMENT SUCCESSFUL!"
echo "============================================="
echo ""
echo "📊 SERVICE URLS:"
echo "• Task Management API: http://localhost:8080/api/tasks"
echo "• Health Check: http://localhost:8080/api/tasks/health"
echo "• H2 Database Console: http://localhost:8080/h2-console"
echo "• Grafana Dashboard: http://localhost:3000 (admin/admin123)"
echo "• Jenkins: http://localhost:8081"
echo ""
echo "🔧 DATABASE INFO (H2 Console):"
echo "• JDBC URL: jdbc:h2:mem:taskdb"
echo "• Username: sa"
echo "• Password: (leave empty)"
echo ""

print_success "Pipeline execution completed successfully!"
