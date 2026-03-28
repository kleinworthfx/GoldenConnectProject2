# 🗄️ Database Setup Guide for GoldenConnect

## **📋 Prerequisites**
- MySQL 8.0 or higher installed
- MySQL Workbench (optional but recommended)
- Java 17 and Maven installed

---

## **🔧 Database Configuration Options**

### **Option 1: Environment Variables (Recommended)**

#### **Windows (PowerShell)**
```powershell
# Set environment variables for your session
$env:GOLDENCONNECT_DB_URL = "jdbc:mysql://localhost:3306/goldenconnect"
$env:GOLDENCONNECT_DB_USERNAME = "root"
$env:GOLDENCONNECT_DB_PASSWORD = "your_mysql_password"

# Run the application
mvn javafx:run
```

#### **Windows (Command Prompt)**
```cmd
# Set environment variables for your session
set GOLDENCONNECT_DB_URL=jdbc:mysql://localhost:3306/goldenconnect
set GOLDENCONNECT_DB_USERNAME=root
set GOLDENCONNECT_DB_PASSWORD=your_mysql_password

# Run the application
mvn javafx:run
```

#### **macOS/Linux**
```bash
# Set environment variables for your session
export GOLDENCONNECT_DB_URL="jdbc:mysql://localhost:3306/goldenconnect"
export GOLDENCONNECT_DB_USERNAME="root"
export GOLDENCONNECT_DB_PASSWORD="your_mysql_password"

# Run the application
mvn javafx:run
```

### **Option 2: Permanent Environment Variables**

#### **Windows**
1. Open System Properties → Advanced → Environment Variables
2. Add new User Variables:
   - `GOLDENCONNECT_DB_URL` = `jdbc:mysql://localhost:3306/goldenconnect`
   - `GOLDENCONNECT_DB_USERNAME` = `root`
   - `GOLDENCONNECT_DB_PASSWORD` = `your_mysql_password`

#### **macOS/Linux**
Add to your `~/.bashrc` or `~/.zshrc`:
```bash
export GOLDENCONNECT_DB_URL="jdbc:mysql://localhost:3306/goldenconnect"
export GOLDENCONNECT_DB_USERNAME="root"
export GOLDENCONNECT_DB_PASSWORD="your_mysql_password"
```

---

## **🗄️ Database Setup Steps**

### **Step 1: Create Database**
```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create the database
CREATE DATABASE IF NOT EXISTS goldenconnect;
USE goldenconnect;
```

### **Step 2: Run Schema Script**
```bash
# Navigate to project directory
cd GoldenConnectProject

# Run the schema script
mysql -u root -p goldenconnect < src/main/resources/db/schema.sql
```

### **Step 3: Load Sample Data**
```bash
# Load sample data
mysql -u root -p goldenconnect < src/main/resources/db/data.sql
```

### **Step 4: Verify Database**
```sql
-- Connect to MySQL
mysql -u root -p goldenconnect

-- Check tables
SHOW TABLES;

-- Check sample data
SELECT * FROM users LIMIT 5;
SELECT * FROM elderly_profiles LIMIT 5;
SELECT * FROM volunteers LIMIT 5;
```

---

## **🔍 Troubleshooting**

### **Common Issues:**

#### **1. "Access denied for user 'root'@'localhost'"**
- **Solution**: Check your MySQL root password
- **Alternative**: Create a new MySQL user:
```sql
CREATE USER 'goldenconnect'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON goldenconnect.* TO 'goldenconnect'@'localhost';
FLUSH PRIVILEGES;
```

#### **2. "Table doesn't exist" errors**
- **Solution**: Make sure you ran the schema.sql script
- **Check**: Verify tables exist with `SHOW TABLES;`

#### **3. Connection timeout**
- **Solution**: Check if MySQL service is running
- **Windows**: Services → MySQL80 → Start
- **macOS**: `brew services start mysql`
- **Linux**: `sudo systemctl start mysql`

#### **4. Port conflicts**
- **Solution**: If MySQL is not on default port 3306, update the URL:
```bash
export GOLDENCONNECT_DB_URL="jdbc:mysql://localhost:YOUR_PORT/goldenconnect"
```

---

## **📝 Team Member Checklist**

- [ ] MySQL installed and running
- [ ] Database `goldenconnect` created
- [ ] Schema script executed successfully
- [ ] Sample data loaded
- [ ] Environment variables set
- [ ] Application runs without database errors
- [ ] Can login with sample user (admin@test.com / password123)

---

## **🔐 Security Notes**

- **Never commit passwords to version control**
- **Use environment variables for sensitive data**
- **Consider using a dedicated database user instead of root**
- **Regularly backup your database**

---

## **📞 Support**

If you encounter issues:
1. Check the troubleshooting section above
2. Verify your MySQL installation
3. Ensure all environment variables are set correctly
4. Check the application console for detailed error messages
