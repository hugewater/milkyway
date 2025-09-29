#!/bin/bash

# File Transfer Script for BigWater Deployment
# This script helps transfer files to the AlmaLinux server

# Configuration
SERVER_IP=""
SERVER_USER=""
APP_DIR="/opt/bigwater"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}===================================================${NC}"
    echo -e "${BLUE} $1${NC}"
    echo -e "${BLUE}===================================================${NC}"
}

# Function to get user input
get_server_info() {
    read -p "Enter server IP address: " SERVER_IP
    read -p "Enter server username: " SERVER_USER
    
    if [[ -z "$SERVER_IP" || -z "$SERVER_USER" ]]; then
        print_error "Server IP and username are required."
        exit 1
    fi
}

# Function to transfer backend files
transfer_backend() {
    print_header "Transferring Backend Files"
    
    # Create remote directory
    ssh ${SERVER_USER}@${SERVER_IP} "sudo mkdir -p ${APP_DIR}/quarkus-api && sudo chown ${SERVER_USER}:${SERVER_USER} ${APP_DIR}"
    
    # Transfer backend source code
    rsync -avz --progress bigwater-backend/quarkus-api/ ${SERVER_USER}@${SERVER_IP}:${APP_DIR}/quarkus-api/
    
    # Transfer database schema
    scp AFFILIATE_DATABASE_SCHEMA.sql ${SERVER_USER}@${SERVER_IP}:${APP_DIR}/
    
    # Transfer production configuration
    scp application-production.properties ${SERVER_USER}@${SERVER_IP}:${APP_DIR}/quarkus-api/src/main/resources/application.properties
    
    print_status "Backend files transferred successfully."
}

# Function to transfer frontend files
transfer_frontend() {
    print_header "Transferring Frontend Files"
    
    # Create remote directory
    ssh ${SERVER_USER}@${SERVER_IP} "sudo mkdir -p ${APP_DIR}/vue3 && sudo chown ${SERVER_USER}:${SERVER_USER} ${APP_DIR}/vue3"
    
    # Transfer frontend source code
    rsync -avz --progress bigwater-ui/vue3/ ${SERVER_USER}@${SERVER_IP}:${APP_DIR}/vue3/
    
    # Transfer production API config
    scp api-config-production.js ${SERVER_USER}@${SERVER_IP}:${APP_DIR}/vue3/src/config/api.js
    
    print_status "Frontend files transferred successfully."
}

# Function to transfer deployment scripts
transfer_deployment_scripts() {
    print_header "Transferring Deployment Scripts"
    
    # Transfer deployment script
    scp deploy-almalinux.sh ${SERVER_USER}@${SERVER_IP}:~/
    
    # Transfer documentation
    scp ALMALINUX_DEPLOYMENT_GUIDE.md ${SERVER_USER}@${SERVER_IP}:~/
    scp DEPLOYMENT_CHECKLIST.md ${SERVER_USER}@${SERVER_IP}:~/
    
    # Make deployment script executable
    ssh ${SERVER_USER}@${SERVER_IP} "chmod +x ~/deploy-almalinux.sh"
    
    print_status "Deployment scripts transferred successfully."
}

# Function to run remote deployment
run_deployment() {
    print_header "Running Remote Deployment"
    
    read -p "Do you want to run the deployment script on the server? (y/n): " run_deploy
    if [[ $run_deploy == "y" || $run_deploy == "Y" ]]; then
        ssh -t ${SERVER_USER}@${SERVER_IP} "~/deploy-almalinux.sh"
        print_status "Deployment script executed."
    else
        print_status "Deployment script ready to run. Execute: ssh ${SERVER_USER}@${SERVER_IP} '~/deploy-almalinux.sh'"
    fi
}

# Function to setup database
setup_remote_database() {
    print_header "Setting up Database"
    
    read -p "Do you want to apply the database schema? (y/n): " apply_schema
    if [[ $apply_schema == "y" || $apply_schema == "Y" ]]; then
        read -p "Enter MySQL bigwater user password: " -s db_password
        echo
        
        ssh -t ${SERVER_USER}@${SERVER_IP} "mysql -u bigwater -p${db_password} bigwater_affiliate < ${APP_DIR}/AFFILIATE_DATABASE_SCHEMA.sql"
        print_status "Database schema applied successfully."
    fi
}

# Function to build and deploy application
build_and_deploy() {
    print_header "Building and Deploying Application"
    
    # Build backend
    ssh -t ${SERVER_USER}@${SERVER_IP} "cd ${APP_DIR}/quarkus-api && ./mvnw clean package -DskipTests"
    
    # Build frontend
    ssh -t ${SERVER_USER}@${SERVER_IP} "cd ${APP_DIR}/vue3 && pnpm install && pnpm build"
    
    # Deploy frontend to nginx
    ssh -t ${SERVER_USER}@${SERVER_IP} "sudo cp -r ${APP_DIR}/vue3/dist/* /var/www/html/"
    
    # Start backend service
    ssh -t ${SERVER_USER}@${SERVER_IP} "sudo systemctl start bigwater-api"
    
    print_status "Application built and deployed successfully."
}

# Function to check deployment status
check_deployment() {
    print_header "Checking Deployment Status"
    
    echo "Checking services..."
    ssh ${SERVER_USER}@${SERVER_IP} "sudo systemctl status bigwater-api nginx mysqld --no-pager"
    
    echo
    echo "Testing API health..."
    ssh ${SERVER_USER}@${SERVER_IP} "curl -s http://localhost:8080/q/health || echo 'API not responding'"
    
    echo
    echo "Testing frontend..."
    ssh ${SERVER_USER}@${SERVER_IP} "curl -s -o /dev/null -w '%{http_code}' http://localhost/ || echo 'Frontend not responding'"
    
    print_status "Deployment status check completed."
}

# Main menu
show_menu() {
    echo
    print_header "BigWater Deployment - File Transfer Menu"
    echo "1. Transfer Backend Files"
    echo "2. Transfer Frontend Files"
    echo "3. Transfer Deployment Scripts"
    echo "4. Transfer All Files"
    echo "5. Run Deployment Script"
    echo "6. Setup Database"
    echo "7. Build and Deploy Application"
    echo "8. Check Deployment Status"
    echo "9. Exit"
    echo
}

# Main function
main() {
    print_header "BigWater File Transfer Script"
    
    get_server_info
    
    while true; do
        show_menu
        read -p "Select an option (1-9): " choice
        
        case $choice in
            1)
                transfer_backend
                ;;
            2)
                transfer_frontend
                ;;
            3)
                transfer_deployment_scripts
                ;;
            4)
                transfer_backend
                transfer_frontend
                transfer_deployment_scripts
                ;;
            5)
                run_deployment
                ;;
            6)
                setup_remote_database
                ;;
            7)
                build_and_deploy
                ;;
            8)
                check_deployment
                ;;
            9)
                print_status "Exiting..."
                exit 0
                ;;
            *)
                print_error "Invalid option. Please select 1-9."
                ;;
        esac
    done
}

# Run main function if script is executed directly
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi