#!/bin/bash

# Loan Request Service - Start Script
show_usage() {
    echo ""
    echo "Usage:"
    echo "  ./start.sh backend      - Start only backend on port 8081"
    echo "  ./start.sh frontend     - Start only frontend on port 4200"
    echo "  ./start.sh docker       - Start with Docker Compose"
    echo ""
}

case "$1" in
    backend)
        echo "Starting Backend"
        echo ""
        ./mvnw spring-boot:run
        ;;
    frontend)
        echo "Starting Frontend"
        echo ""
        cd frontend || {
                       echo "Failed to change directory to frontend"
                       exit 1
                   }
        echo "Checking dependencies..."
        npm install > /dev/null 2>&1
        npm start
        ;;
    docker)
        echo "Starting Docker Containers"
        echo ""
        docker compose up --build
        ;;
    help)
        show_usage
        ;;
    *)
        echo "Unknown option: $1"
        show_usage
        exit 1
        ;;
esac

