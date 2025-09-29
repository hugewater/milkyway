# Deployment Checklist for AlmaLinux 9.6

## Pre-Deployment Preparation

### 1. Development Environment
- [ ] All Java files compiled successfully
- [ ] Database schema file ready (`AFFILIATE_DATABASE_SCHEMA.sql`)
- [ ] Frontend built and tested (`pnpm build`)
- [ ] All environment variables documented
- [ ] SSL certificate requirements assessed

### 2. Server Requirements
- [ ] AlmaLinux 9.6 server provisioned
- [ ] Minimum 4GB RAM, 20GB storage
- [ ] Root/sudo access available
- [ ] Domain name configured (A record pointing to server IP)
- [ ] SSH access configured

### 3. Security Considerations
- [ ] Strong database passwords prepared
- [ ] Firewall rules planned
- [ ] SSL certificate strategy decided
- [ ] Backup strategy planned

## Deployment Steps

### Phase 1: System Setup
- [ ] Update AlmaLinux system
- [ ] Install Java 21 OpenJDK
- [ ] Install Maven 3.9.x
- [ ] Install MySQL 8.0
- [ ] Install Node.js 18.x and pnpm
- [ ] Configure firewall

### Phase 2: Database Configuration
- [ ] Create `bigwater_affiliate` database
- [ ] Create `bigwater` database user
- [ ] Apply database schema from `AFFILIATE_DATABASE_SCHEMA.sql`
- [ ] Test database connectivity
- [ ] Configure database backup script

### Phase 3: Application Deployment
- [ ] Create application directories (`/opt/bigwater`)
- [ ] Copy backend code to server
- [ ] Update `application.properties` with production settings
- [ ] Build Quarkus application
- [ ] Create systemd service for backend
- [ ] Copy frontend code to server
- [ ] Build Vue.js frontend
- [ ] Configure Nginx for frontend and API proxy

### Phase 4: Service Configuration
- [ ] Configure Nginx virtual host
- [ ] Test Nginx configuration
- [ ] Start MySQL service
- [ ] Start backend API service
- [ ] Start Nginx service
- [ ] Configure SSL certificate (Let's Encrypt)

### Phase 5: Testing & Validation
- [ ] Test database connectivity
- [ ] Test backend API endpoints
- [ ] Test frontend loading
- [ ] Test affiliate system functionality
- [ ] Verify SSL certificate
- [ ] Test backup script

## Post-Deployment Tasks

### Monitoring Setup
- [ ] Configure log rotation
- [ ] Set up system monitoring
- [ ] Configure alerting (optional)
- [ ] Document maintenance procedures

### Security Hardening
- [ ] Install fail2ban
- [ ] Configure SELinux policies
- [ ] Disable unnecessary services
- [ ] Update SSH configuration

### Maintenance Planning
- [ ] Schedule regular backups
- [ ] Plan update procedures
- [ ] Document troubleshooting steps
- [ ] Create monitoring dashboards

## File Locations Reference

### Backend Files
- Application: `/opt/bigwater/quarkus-api/`
- Configuration: `/opt/bigwater/quarkus-api/src/main/resources/application.properties`
- Service: `/etc/systemd/system/bigwater-api.service`
- Logs: `journalctl -u bigwater-api`

### Frontend Files
- Source: `/opt/bigwater/vue3/`
- Built files: `/var/www/html/`
- Nginx config: `/etc/nginx/conf.d/bigwater.conf`
- Logs: `/var/log/nginx/`

### Database Files
- Schema: `AFFILIATE_DATABASE_SCHEMA.sql`
- Backup script: `/opt/bigwater/backup-db.sh`
- Backups: `/opt/bigwater/backups/`
- MySQL config: `/etc/my.cnf`

## Quick Commands Reference

### Service Management
```bash
# Start/stop/restart services
sudo systemctl start|stop|restart bigwater-api
sudo systemctl start|stop|restart nginx
sudo systemctl start|stop|restart mysqld

# Check service status
sudo systemctl status bigwater-api nginx mysqld

# Enable services at boot
sudo systemctl enable bigwater-api nginx mysqld
```

### Log Monitoring
```bash
# Follow API logs
sudo journalctl -u bigwater-api -f

# Follow Nginx logs
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log

# Check MySQL logs
sudo tail -f /var/log/mysqld.log
```

### Database Operations
```bash
# Connect to database
mysql -u bigwater -p bigwater_affiliate

# Run backup
/opt/bigwater/backup-db.sh

# Apply schema updates
mysql -u bigwater -p bigwater_affiliate < new_schema.sql
```

### Application Management
```bash
# Rebuild backend
cd /opt/bigwater/quarkus-api
./mvnw clean package -DskipTests
sudo systemctl restart bigwater-api

# Rebuild frontend
cd /opt/bigwater/vue3
pnpm build
sudo cp -r dist/* /var/www/html/
```

## Troubleshooting Quick Fixes

### Backend Not Starting
1. Check Java version: `java -version`
2. Check application logs: `journalctl -u bigwater-api`
3. Verify database connection
4. Check port availability: `ss -tlnp | grep 8080`

### Frontend Not Loading
1. Check Nginx status: `systemctl status nginx`
2. Verify files in `/var/www/html/`
3. Check Nginx configuration: `nginx -t`
4. Review Nginx error logs

### Database Issues
1. Check MySQL status: `systemctl status mysqld`
2. Test connection: `mysql -u bigwater -p`
3. Verify user permissions
4. Check MySQL error log

### SSL Certificate Issues
1. Check certificate status: `certbot certificates`
2. Test renewal: `certbot renew --dry-run`
3. Verify domain DNS settings
4. Check Nginx SSL configuration

## Emergency Contacts & Documentation
- [ ] Document server access credentials
- [ ] Document database passwords
- [ ] Document domain registrar details
- [ ] Document backup locations
- [ ] Create emergency contact list