#!/bin/bash
# BILD Mobile Framework - Docker CI Helper Script
# Run this script to test the CI pipeline in a local Docker container

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Constants
DOCKER_IMAGE="bild-mobile-ci"
CONTAINER_NAME="bild-ci-test"

print_header() {
    echo -e "${BLUE}🐳 BILD Mobile Framework - Docker CI Test${NC}"
    echo -e "${BLUE}=========================================${NC}"
    echo
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

check_docker() {
    print_info "Checking Docker installation..."
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        print_error "Docker daemon is not running. Please start Docker."
        exit 1
    fi
    
    print_success "Docker is ready"
}

cleanup_existing() {
    print_info "Cleaning up existing containers..."
    docker stop $CONTAINER_NAME 2>/dev/null || true
    docker rm $CONTAINER_NAME 2>/dev/null || true
    print_success "Cleanup completed"
}

build_image() {
    print_info "Building Docker CI image..."
    if docker build -t $DOCKER_IMAGE -f Dockerfile.ci .; then
        print_success "Docker image built successfully"
    else
        print_error "Failed to build Docker image"
        exit 1
    fi
}

run_tests() {
    print_info "Starting CI container..."
    
    # Run in interactive mode if terminal is available
    if [ -t 0 ]; then
        print_info "Running in interactive mode (press Ctrl+C to stop)"
        docker run -it --name $CONTAINER_NAME \
            --privileged \
            -p 4723:4723 \
            -v "$(pwd)/reports:/app/reports" \
            -v "$(pwd)/screenshots:/app/screenshots" \
            -v "$(pwd)/logs:/app/logs" \
            $DOCKER_IMAGE
    else
        print_info "Running in batch mode"
        docker run --name $CONTAINER_NAME \
            --privileged \
            -v "$(pwd)/reports:/app/reports" \
            -v "$(pwd)/screenshots:/app/screenshots" \
            -v "$(pwd)/logs:/app/logs" \
            $DOCKER_IMAGE
    fi
}

copy_results() {
    print_info "Copying test results..."
    
    # Create directories if they don't exist
    mkdir -p reports screenshots logs
    
    # Copy results from container
    docker cp $CONTAINER_NAME:/app/target/surefire-reports/. ./reports/ 2>/dev/null || true
    docker cp $CONTAINER_NAME:/app/reports/. ./reports/ 2>/dev/null || true
    docker cp $CONTAINER_NAME:/app/screenshots/. ./screenshots/ 2>/dev/null || true
    docker cp $CONTAINER_NAME:/app/logs/. ./logs/ 2>/dev/null || true
    
    print_success "Test results copied to local directories"
}

show_usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build    - Build the Docker CI image only"
    echo "  test     - Run the complete CI test pipeline"
    echo "  clean    - Clean up Docker resources"
    echo "  shell    - Open interactive shell in CI container"
    echo "  logs     - Show container logs"
    echo "  help     - Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 test        # Run full CI test"
    echo "  $0 build       # Build image only"
    echo "  $0 shell       # Interactive debugging"
}

# Main execution
case "${1:-test}" in
    "build")
        print_header
        check_docker
        cleanup_existing
        build_image
        print_success "Docker CI image ready"
        ;;
        
    "test")
        print_header
        check_docker
        cleanup_existing
        build_image
        run_tests
        copy_results
        
        # Show test results
        if [ -f "reports/ExtentReport.html" ]; then
            print_success "Test execution completed!"
            print_info "Test report available at: reports/ExtentReport.html"
        else
            print_warning "Test execution completed, check logs for details"
        fi
        
        # Cleanup
        cleanup_existing
        ;;
        
    "clean")
        print_header
        check_docker
        cleanup_existing
        docker rmi $DOCKER_IMAGE 2>/dev/null || true
        print_success "Docker resources cleaned up"
        ;;
        
    "shell")
        print_header
        check_docker
        
        # Check if image exists
        if ! docker image inspect $DOCKER_IMAGE &> /dev/null; then
            print_warning "Docker image not found, building..."
            build_image
        fi
        
        print_info "Opening interactive shell..."
        docker run -it --name "${CONTAINER_NAME}-shell" \
            --privileged \
            -p 4723:4723 \
            -v "$(pwd):/app" \
            --entrypoint /bin/bash \
            $DOCKER_IMAGE
        
        docker rm "${CONTAINER_NAME}-shell" 2>/dev/null || true
        ;;
        
    "logs")
        print_header
        if docker ps -a | grep -q $CONTAINER_NAME; then
            docker logs $CONTAINER_NAME
        else
            print_error "No container found with name: $CONTAINER_NAME"
        fi
        ;;
        
    "help"|"-h"|"--help")
        show_usage
        ;;
        
    *)
        print_error "Unknown command: $1"
        echo
        show_usage
        exit 1
        ;;
esac