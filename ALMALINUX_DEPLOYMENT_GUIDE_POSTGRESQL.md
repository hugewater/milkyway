# AlmaLinux 9.6 Deployment Guide - BigWater Affiliate System (PostgreSQL)

This guide provides complete instructions for deploying the BigWater affiliate system to AlmaLinux 9.6 using PostgreSQL.

## System Requirements

- AlmaLinux 9.6 server
- Minimum 4GB RAM
- 20GB available disk space
- Root or sudo access
- Internet connection

## 1. System Preparation

### Update System
```bash
sudo dnf update -y
sudo dnf install -y epel-release
sudo dnf groupinstall -y "Development Tools"
```

### Install Required Packages
```bash
sudo dnf install -y \
    wget \
    curl \
    git \
    unzip \
    firewalld \
    nginx \
    certbot \
    python3-certbot-nginx
```

## 2. Java Installation (OpenJDK 21)

```bash
# Install OpenJDK 21
sudo dnf install -y java-21-openjdk java-21-openjdk-devel

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc

# Verify installation
java -version
```

## 3. Maven Installation

```bash
# Download and install Maven 3.9.x
cd /opt
sudo wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
sudo tar xzf apache-maven-3.9.6-bin.tar.gz
sudo ln -s apache-maven-3.9.6 maven

# Set Maven environment
echo 'export MAVEN_HOME=/opt/maven' >> ~/.bashrc
echo 'export PATH=$PATH:$MAVEN_HOME/bin' >> ~/.bashrc
source ~/.bashrc

# Verify installation
mvn -version
```

## 4. PostgreSQL 15 Installation

```bash
# Install PostgreSQL 15
sudo dnf install -y postgresql-server postgresql postgresql-contrib

# Initialize database
sudo postgresql-setup --initdb

# Start and enable PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Configure PostgreSQL authentication
sudo sed -i 's/ident/md5/g' /var/lib/pgsql/data/pg_hba.conf
sudo sed -i 's/peer/md5/g' /var/lib/pgsql/data/pg_hba.conf

# Restart PostgreSQL to apply changes
sudo systemctl restart postgresql

# Create database and user
sudo -u postgres psql
```

```sql
-- Execute these commands in PostgreSQL prompt
CREATE DATABASE bigwater_affiliate;
CREATE USER bigwater WITH ENCRYPTED PASSWORD 'YourStrongPassword123!';
GRANT ALL PRIVILEGES ON DATABASE bigwater_affiliate TO bigwater;
ALTER DATABASE bigwater_affiliate OWNER TO bigwater;
\q
```

## 5. Node.js Installation (for Vue.js Frontend)

```bash
# Install Node.js 18.x LTS
curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo dnf install -y nodejs

# Install pnpm globally
sudo npm install -g pnpm

# Verify installation
node --version
npm --version
pnpm --version
```

## 6. Application Deployment

### Create Application Directory
```bash
sudo mkdir -p /opt/bigwater
sudo chown $USER:$USER /opt/bigwater
cd /opt/bigwater
```

### Deploy Backend (Quarkus API)

```bash
# Copy your project files (replace with actual source)
# Method 1: Direct copy from development machine
scp -r /path/to/bigwater-backend/quarkus-api user@server:/opt/bigwater/

# Method 2: Git clone (if using version control)
# git clone https://your-repo-url/bigwater-backend.git
# cd bigwater-backend/quarkus-api
```

### Configure Database Schema
```bash
cd /opt/bigwater/quarkus-api

# Apply PostgreSQL database schema
psql -U bigwater -d bigwater_affiliate -h localhost < AFFILIATE_DATABASE_SCHEMA_POSTGRESQL.sql
```

### Update Application Configuration
```bash
# Edit application.properties
nano src/main/resources/application.properties
```

Add/update these properties:
```properties
# Database configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=bigwater
quarkus.datasource.password=YourStrongPassword123!
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/bigwater_affiliate

# JPA/Hibernate configuration
quarkus.hibernate-orm.database.generation=validate
quarkus.hibernate-orm.log.sql=false
quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect

# HTTP configuration
quarkus.http.host=0.0.0.0
quarkus.http.port=8080

# CORS configuration (adjust domains as needed)
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000,https://yourdomain.com
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with

# Logging
quarkus.log.level=INFO
quarkus.log.category."com.bigwater".level=DEBUG

# PostgreSQL-specific optimizations
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.max-size=20
quarkus.datasource.jdbc.acquisition-timeout=PT5S

# Connection pool for PostgreSQL
quarkus.datasource.jdbc.pooling-enabled=true
```

### Add PostgreSQL Driver to Maven Dependencies
```bash
# Edit pom.xml and add PostgreSQL driver
nano pom.xml
```

Add this dependency:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>
```

### Build and Start Backend
```bash
# Build the application
./mvnw clean package -DskipTests

# Create systemd service
sudo tee /etc/systemd/system/bigwater-api.service > /dev/null << 'EOF'
[Unit]
Description=BigWater Affiliate API
After=postgresql.service
Requires=postgresql.service

[Service]
Type=simple
User=bigwater
Group=bigwater
WorkingDirectory=/opt/bigwater/quarkus-api
ExecStart=/usr/bin/java -jar /opt/bigwater/quarkus-api/target/quarkus-app/quarkus-run.jar
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=bigwater-api
Environment=JAVA_HOME=/usr/lib/jvm/java-21-openjdk

[Install]
WantedBy=multi-user.target
EOF
```

### Deploy Frontend (Vue.js)

```bash
cd /opt/bigwater

# Copy frontend files
scp -r /path/to/bigwater-ui/vue3 user@server:/opt/bigwater/

cd vue3

# Install dependencies
pnpm install

# Update API base URL in config
nano src/config/api.js
```

Update API configuration:
```javascript
export const API_CONFIG = {
  BASE_URL: 'http://localhost:8080/api', // Change to your server's API URL
  TIMEOUT: 30000
}
```

```bash
# Build for production
pnpm build

# Copy built files to nginx directory
sudo cp -r dist/* /var/www/html/
```

## 7. Nginx Configuration

```bash
# Create nginx configuration
sudo tee /etc/nginx/conf.d/bigwater.conf > /dev/null << 'EOF'
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    
    # Frontend static files
    location / {
        root /var/www/html;
        try_files $uri $uri/ /index.html;
        
        # Add security headers
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header X-Content-Type-Options "nosniff" always;
    }
    
    # API proxy
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # CORS headers
        add_header Access-Control-Allow-Origin * always;
        add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS" always;
        add_header Access-Control-Allow-Headers "DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization" always;
        
        if ($request_method = 'OPTIONS') {
            return 204;
        }
    }
    
    # Static assets caching
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
        root /var/www/html;
    }
}
EOF

# Test nginx configuration
sudo nginx -t

# Enable and start nginx
sudo systemctl enable nginx
sudo systemctl restart nginx
```

## 8. User Management & Permissions

```bash
# Create bigwater user
sudo useradd -r -s /bin/false bigwater
sudo chown -R bigwater:bigwater /opt/bigwater
sudo chmod -R 755 /opt/bigwater
```

## 9. Firewall Configuration

```bash
# Enable firewall
sudo systemctl enable firewalld
sudo systemctl start firewalld

# Allow HTTP, HTTPS, and PostgreSQL
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=5432/tcp

# Reload firewall
sudo firewall-cmd --reload
```

## 10. SSL Certificate (Production)

```bash
# Install SSL certificate (replace yourdomain.com)
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com

# Auto-renewal
sudo systemctl enable certbot-renew.timer
```

## 11. Start Services

```bash
# Enable and start all services
sudo systemctl daemon-reload
sudo systemctl enable bigwater-api
sudo systemctl start bigwater-api

# Check service status
sudo systemctl status bigwater-api
sudo systemctl status nginx
sudo systemctl status postgresql
```

## 12. Monitoring & Logs

```bash
# View application logs
sudo journalctl -u bigwater-api -f

# View nginx logs
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log

# View PostgreSQL logs
sudo tail -f /var/lib/pgsql/data/log/postgresql-*.log
```

## 13. Testing Deployment

### Test Backend API
```bash
# Test health endpoint
curl http://localhost:8080/q/health

# Test affiliates endpoint
curl -H "Content-Type: application/json" http://localhost:8080/api/affiliates
```

### Test Frontend
```bash
# Access frontend
curl http://localhost/

# Check if files are served correctly
curl http://localhost/assets/
```

## 14. Database Management

### Backup Script
```bash
# Create backup script
sudo tee /opt/bigwater/backup-db.sh > /dev/null << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/bigwater/backups"
mkdir -p $BACKUP_DIR

# PostgreSQL backup
pg_dump -U bigwater -h localhost bigwater_affiliate > "$BACKUP_DIR/bigwater_affiliate_$DATE.sql"

# Keep only last 7 days of backups
find $BACKUP_DIR -name "*.sql" -type f -mtime +7 -delete
EOF

chmod +x /opt/bigwater/backup-db.sh

# Add to crontab for daily backups
echo "0 2 * * * /opt/bigwater/backup-db.sh" | sudo crontab -
```

## 15. Performance Optimization

### JVM Tuning
```bash
# Update systemd service with JVM options
sudo sed -i 's|ExecStart=.*|ExecStart=/usr/bin/java -Xms512m -Xmx2g -XX:+UseG1GC -jar /opt/bigwater/quarkus-api/target/quarkus-app/quarkus-run.jar|' /etc/systemd/system/bigwater-api.service

sudo systemctl daemon-reload
sudo systemctl restart bigwater-api
```

### PostgreSQL Tuning
```bash
# Edit PostgreSQL configuration
sudo nano /var/lib/pgsql/data/postgresql.conf
```

Add/update these settings:
```
# Memory settings
shared_buffers = 256MB
effective_cache_size = 1GB
work_mem = 4MB
maintenance_work_mem = 64MB

# Connection settings
max_connections = 200

# Logging
log_statement = 'none'
log_min_duration_statement = 1000

# Performance
random_page_cost = 1.1
effective_io_concurrency = 200

# Write-ahead logging
checkpoint_completion_target = 0.7
wal_buffers = 16MB
```

```bash
# Restart PostgreSQL to apply changes
sudo systemctl restart postgresql
```

## 16. Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   sudo lsof -i :8080
   # Kill the process or change port in application.properties
   ```

2. **Database connection issues**
   ```bash
   # Check PostgreSQL status
   sudo systemctl status postgresql
   
   # Test connection
   psql -U bigwater -d bigwater_affiliate -h localhost
   ```

3. **Permission denied errors**
   ```bash
   sudo chown -R bigwater:bigwater /opt/bigwater
   sudo chmod -R 755 /opt/bigwater
   ```

4. **Frontend not loading**
   ```bash
   # Check nginx configuration
   sudo nginx -t
   
   # Check if files exist
   ls -la /var/www/html/
   ```

## 17. Security Hardening

### Additional Security Measures
```bash
# Install fail2ban
sudo dnf install -y fail2ban
sudo systemctl enable fail2ban
sudo systemctl start fail2ban

# Configure PostgreSQL security
sudo nano /var/lib/pgsql/data/pg_hba.conf
```

Update pg_hba.conf for better security:
```
# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   all             all                                     peer
host    all             all             127.0.0.1/32            md5
host    all             all             ::1/128                 md5
host    bigwater_affiliate bigwater     localhost               md5
```

```bash
# Disable root SSH login (optional)
sudo sed -i 's/#PermitRootLogin yes/PermitRootLogin no/' /etc/ssh/sshd_config
sudo systemctl restart sshd

# Update SELinux contexts
sudo setsebool -P httpd_can_network_connect 1
sudo semanage fcontext -a -t httpd_exec_t "/var/www/html(/.*)?"
sudo restorecon -Rv /var/www/html/
```

## 18. Maintenance

### Regular Tasks
```bash
# Weekly system updates
sudo dnf update -y

# PostgreSQL maintenance
sudo -u postgres vacuumdb --all --analyze --verbose

# Check disk space
df -h

# Monitor memory usage
free -h

# Check service status
sudo systemctl status bigwater-api nginx postgresql
```

## Summary

After completing these steps, your BigWater affiliate system will be fully deployed on AlmaLinux 9.6 with PostgreSQL:

- ✅ Quarkus backend API running on port 8080
- ✅ Vue.js frontend served by Nginx
- ✅ PostgreSQL database with affiliate schema
- ✅ SSL certificate (if configured)
- ✅ Automated backups
- ✅ Monitoring and logging
- ✅ Security hardening

Access your application at: `https://yourdomain.com` (or `http://your-server-ip` for testing)

The affiliate system includes:
- 7-tier commission structure (Fan → President)
- Automatic promotion system
- Commission calculation with generation and leadership bonuses
- Complete transaction tracking
- REST API for frontend integration
- PostgreSQL-optimized queries and functions