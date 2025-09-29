#!/bin/bash

# BigWater Affiliate System - AlmaLinux 9.6 Deployment Script
# This script automates the deployment process

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration variables
DOMAIN_NAME=""
DB_PASSWORD=""
APP_DIR="/opt/bigwater"
DB_NAME="bigwater_affiliate"
DB_USER="bigwater"

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}===================================================${NC}"
    echo -e "${BLUE} $1${NC}"
    echo -e "${BLUE}===================================================${NC}"
}

# Function to check if running as root
check_root() {
    if [[ $EUID -eq 0 ]]; then
        print_error "This script should not be run as root. Please run as a regular user with sudo privileges."
        exit 1
    fi
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to get user input
get_user_input() {
    print_header "Configuration Setup"
    
    read -p "Enter your domain name (e.g., yourdomain.com): " DOMAIN_NAME
    while [[ -z "$DOMAIN_NAME" ]]; do
        print_warning "Domain name cannot be empty."
        read -p "Enter your domain name (e.g., yourdomain.com): " DOMAIN_NAME
    done
    
    read -s -p "Enter MySQL password for bigwater user: " DB_PASSWORD
    echo
    while [[ -z "$DB_PASSWORD" ]]; do
        print_warning "Password cannot be empty."
        read -s -p "Enter MySQL password for bigwater user: " DB_PASSWORD
        echo
    done
    
    print_status "Configuration completed."
}

# Function to update system
update_system() {
    print_header "Updating System"
    sudo dnf update -y
    sudo dnf install -y epel-release
    sudo dnf groupinstall -y "Development Tools"
    sudo dnf install -y wget curl git unzip firewalld nginx certbot python3-certbot-nginx
    print_status "System updated successfully."
}

# Function to install Java
install_java() {
    print_header "Installing Java 21"
    sudo dnf install -y java-21-openjdk java-21-openjdk-devel
    
    # Set JAVA_HOME
    if ! grep -q "JAVA_HOME" ~/.bashrc; then
        echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk' >> ~/.bashrc
        echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
    fi
    
    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
    export PATH=$PATH:$JAVA_HOME/bin
    
    print_status "Java installed successfully. Version: $(java -version 2>&1 | head -n 1)"
}

# Function to install Maven
install_maven() {
    print_header "Installing Maven"
    
    if [[ ! -d "/opt/maven" ]]; then
        cd /tmp
        wget -q https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
        sudo tar xzf apache-maven-3.9.6-bin.tar.gz -C /opt/
        sudo ln -sf /opt/apache-maven-3.9.6 /opt/maven
        
        # Set Maven environment
        if ! grep -q "MAVEN_HOME" ~/.bashrc; then
            echo 'export MAVEN_HOME=/opt/maven' >> ~/.bashrc
            echo 'export PATH=$PATH:$MAVEN_HOME/bin' >> ~/.bashrc
        fi
    fi
    
    export MAVEN_HOME=/opt/maven
    export PATH=$PATH:$MAVEN_HOME/bin
    
    print_status "Maven installed successfully. Version: $(/opt/maven/bin/mvn -version | head -n 1)"
}

# Function to install MySQL
install_mysql() {
    print_header "Installing MySQL 8.0"
    
    sudo dnf install -y mysql-server mysql
    sudo systemctl start mysqld
    sudo systemctl enable mysqld
    
    print_status "MySQL installed and started successfully."
}

# Function to setup database
setup_database() {
    print_header "Setting up Database"
    
    # Create database and user
    mysql -u root -p << EOF
CREATE DATABASE IF NOT EXISTS ${DB_NAME};
CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASSWORD}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}'@'localhost';
FLUSH PRIVILEGES;
EOF

    print_status "Database and user created successfully."
}

# Function to install Node.js
install_nodejs() {
    print_header "Installing Node.js"
    
    if ! command_exists node; then
        curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
        sudo dnf install -y nodejs
    fi
    
    if ! command_exists pnpm; then
        sudo npm install -g pnpm
    fi
    
    print_status "Node.js installed successfully. Version: $(node --version)"
}

# Function to create application directory
create_app_directory() {
    print_header "Creating Application Directory"
    
    sudo mkdir -p $APP_DIR
    sudo chown $USER:$USER $APP_DIR
    
    print_status "Application directory created at $APP_DIR"
}

# Function to create systemd service
create_systemd_service() {
    print_header "Creating Systemd Service"
    
    sudo tee /etc/systemd/system/bigwater-api.service > /dev/null << EOF
[Unit]
Description=BigWater Affiliate API
After=mysql.service
Requires=mysql.service

[Service]
Type=simple
User=bigwater
Group=bigwater
WorkingDirectory=${APP_DIR}/quarkus-api
ExecStart=/usr/bin/java -Xms512m -Xmx2g -XX:+UseG1GC -jar ${APP_DIR}/quarkus-api/target/quarkus-app/quarkus-run.jar
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=bigwater-api
Environment=JAVA_HOME=/usr/lib/jvm/java-21-openjdk

[Install]
WantedBy=multi-user.target
EOF

    print_status "Systemd service created successfully."
}

# Function to configure Nginx
configure_nginx() {
    print_header "Configuring Nginx"
    
    sudo tee /etc/nginx/conf.d/bigwater.conf > /dev/null << EOF
server {
    listen 80;
    server_name ${DOMAIN_NAME} www.${DOMAIN_NAME};
    
    # Frontend static files
    location / {
        root /var/www/html;
        try_files \$uri \$uri/ /index.html;
        
        # Add security headers
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header X-Content-Type-Options "nosniff" always;
    }
    
    # API proxy
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        
        # CORS headers
        add_header Access-Control-Allow-Origin * always;
        add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS" always;
        add_header Access-Control-Allow-Headers "DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization" always;
        
        if (\$request_method = 'OPTIONS') {
            return 204;
        }
    }
    
    # Static assets caching
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)\$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
        root /var/www/html;
    }
}
EOF

    sudo nginx -t
    print_status "Nginx configuration created successfully."
}

# Function to setup firewall
setup_firewall() {
    print_header "Configuring Firewall"
    
    sudo systemctl enable firewalld
    sudo systemctl start firewalld
    
    sudo firewall-cmd --permanent --add-service=http
    sudo firewall-cmd --permanent --add-service=https
    sudo firewall-cmd --permanent --add-port=8080/tcp
    sudo firewall-cmd --reload
    
    print_status "Firewall configured successfully."
}

# Function to create bigwater user
create_user() {
    print_header "Creating BigWater User"
    
    if ! id "bigwater" &>/dev/null; then
        sudo useradd -r -s /bin/false bigwater
    fi
    
    sudo chown -R bigwater:bigwater $APP_DIR
    sudo chmod -R 755 $APP_DIR
    
    print_status "BigWater user created and permissions set."
}

# Function to create backup script
create_backup_script() {
    print_header "Creating Backup Script"
    
    sudo tee ${APP_DIR}/backup-db.sh > /dev/null << EOF
#!/bin/bash
DATE=\$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="${APP_DIR}/backups"
mkdir -p \$BACKUP_DIR

mysqldump -u ${DB_USER} -p'${DB_PASSWORD}' ${DB_NAME} > "\$BACKUP_DIR/${DB_NAME}_\$DATE.sql"

# Keep only last 7 days of backups
find \$BACKUP_DIR -name "*.sql" -type f -mtime +7 -delete
EOF

    chmod +x ${APP_DIR}/backup-db.sh
    
    # Add to crontab
    (crontab -l 2>/dev/null; echo "0 2 * * * ${APP_DIR}/backup-db.sh") | crontab -
    
    print_status "Backup script created and scheduled."
}

# Function to install SSL certificate
install_ssl() {
    print_header "Installing SSL Certificate"
    
    read -p "Do you want to install SSL certificate with Let's Encrypt? (y/n): " install_ssl_choice
    if [[ $install_ssl_choice == "y" || $install_ssl_choice == "Y" ]]; then
        sudo certbot --nginx -d $DOMAIN_NAME -d www.$DOMAIN_NAME --non-interactive --agree-tos --email admin@$DOMAIN_NAME
        sudo systemctl enable certbot-renew.timer
        print_status "SSL certificate installed successfully."
    else
        print_warning "SSL certificate installation skipped."
    fi
}

# Function to start services
start_services() {
    print_header "Starting Services"
    
    sudo systemctl daemon-reload
    sudo systemctl enable nginx mysqld
    sudo systemctl restart nginx mysqld
    
    print_status "Services started successfully."
}

# Function to display completion message
display_completion() {
    print_header "Deployment Completed Successfully!"
    
    echo -e "${GREEN}Your BigWater Affiliate System has been deployed successfully!${NC}"
    echo
    echo -e "${BLUE}Next Steps:${NC}"
    echo "1. Copy your application files to: $APP_DIR"
    echo "2. Apply database schema: mysql -u $DB_USER -p $DB_NAME < AFFILIATE_DATABASE_SCHEMA.sql"
    echo "3. Update application.properties with database credentials"
    echo "4. Build and deploy backend: cd $APP_DIR/quarkus-api && ./mvnw clean package"
    echo "5. Build and deploy frontend: cd $APP_DIR/vue3 && pnpm install && pnpm build"
    echo "6. Copy frontend build to nginx: sudo cp -r dist/* /var/www/html/"
    echo "7. Start the application: sudo systemctl start bigwater-api"
    echo
    echo -e "${BLUE}Access URLs:${NC}"
    echo "Frontend: https://$DOMAIN_NAME"
    echo "API Health: https://$DOMAIN_NAME/api/q/health"
    echo
    echo -e "${BLUE}Useful Commands:${NC}"
    echo "Check API status: sudo systemctl status bigwater-api"
    echo "View API logs: sudo journalctl -u bigwater-api -f"
    echo "View nginx logs: sudo tail -f /var/log/nginx/access.log"
    echo "Backup database: $APP_DIR/backup-db.sh"
}

# Main deployment function
main() {
    print_header "BigWater Affiliate System - AlmaLinux 9.6 Deployment"
    
    check_root
    get_user_input
    
    update_system
    install_java
    install_maven
    install_mysql
    setup_database
    install_nodejs
    create_app_directory
    create_user
    create_systemd_service
    configure_nginx
    setup_firewall
    create_backup_script
    start_services
    install_ssl
    
    display_completion
}

# Run main function if script is executed directly
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi