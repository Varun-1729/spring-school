# 🚀 Deployment Guide - School Management System

## 📋 Pre-Deployment Checklist

✅ **Application Status**: Running successfully on localhost:8080  
✅ **Database**: MySQL connection working  
✅ **Demo Credentials**: Removed from login page  
✅ **Environment Variables**: Configured in application.properties  
✅ **GitHub Repository**: https://github.com/varun-1729/school-management-system  

## 🌐 Railway Deployment (Recommended)

### Step 1: Push to GitHub
```bash
# Initialize git repository (if not already done)
git init
git add .
git commit -m "Initial commit - School Management System"

# Add your GitHub repository
git remote add origin https://github.com/varun-1729/school-management-system.git
git branch -M main
git push -u origin main
```

### Step 2: Deploy on Railway
1. **Go to Railway**: https://railway.app
2. **Sign up/Login** with GitHub
3. **Create New Project** → **Deploy from GitHub repo**
4. **Select Repository**: varun-1729/school-management-system
5. **Configure Environment Variables**:
   ```
   DATABASE_URL=mysql://username:password@host:port/database_name
   DATABASE_USERNAME=your_mysql_username
   DATABASE_PASSWORD=your_mysql_password
   PORT=8080
   ```
6. **Deploy**: Railway will automatically build and deploy

### Step 3: Database Setup on Railway
1. **Add MySQL Database**: In Railway dashboard → Add → Database → MySQL
2. **Get Connection Details**: Copy the connection string
3. **Update Environment Variables**: Use the Railway MySQL credentials

## 🎨 Render Deployment (Alternative)

### Step 1: Create Render Account
1. Go to https://render.com
2. Sign up with GitHub

### Step 2: Create Web Service
1. **New** → **Web Service**
2. **Connect Repository**: varun-1729/school-management-system
3. **Configure Settings**:
   - **Name**: school-management-system
   - **Environment**: Java
   - **Build Command**: `mvn clean package`
   - **Start Command**: `java -jar target/*.jar`

### Step 3: Environment Variables
```
DATABASE_URL=your_mysql_connection_string
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
```

## ☁️ Heroku Deployment

### Step 1: Install Heroku CLI
```bash
# Download from https://devcenter.heroku.com/articles/heroku-cli
# Or use npm
npm install -g heroku
```

### Step 2: Deploy to Heroku
```bash
# Login to Heroku
heroku login

# Create Heroku app
heroku create school-management-varun

# Add MySQL addon
heroku addons:create jawsdb:kitefin

# Set environment variables
heroku config:set DATABASE_URL=your_mysql_url
heroku config:set DATABASE_USERNAME=your_username
heroku config:set DATABASE_PASSWORD=your_password

# Deploy
git push heroku main
```

## 🔧 Environment Variables Explained

| Variable | Description | Example |
|----------|-------------|---------|
| `DATABASE_URL` | MySQL connection string | `jdbc:mysql://host:3306/dbname` |
| `DATABASE_USERNAME` | MySQL username | `root` or `admin` |
| `DATABASE_PASSWORD` | MySQL password | `your_secure_password` |
| `PORT` | Application port | `8080` (default) |

## 📊 Post-Deployment Testing

### 1. Verify Application
- ✅ Login page loads without demo credentials
- ✅ All three dashboards accessible
- ✅ Database connection working
- ✅ File upload functionality working

### 2. Test User Roles
- **Admin**: admin@school.com / admin123
- **Teacher**: teacher@school.com / teacher123
- **Student**: student@school.com / student123

### 3. Test Features
- ✅ User authentication
- ✅ Dashboard navigation
- ✅ Data refresh buttons
- ✅ Assignment submission (Student)
- ✅ Course management (Admin)
- ✅ Grade management (Teacher)

## 🚨 Troubleshooting

### Common Issues:

1. **Database Connection Error**
   - Check environment variables
   - Verify MySQL service is running
   - Confirm connection string format

2. **Build Failure**
   - Ensure Java 17 is specified in deployment settings
   - Check Maven dependencies in pom.xml
   - Verify all files are committed to Git

3. **Port Issues**
   - Use environment variable `PORT` instead of hardcoded 8080
   - Railway/Render automatically assign ports

### Quick Fixes:
```bash
# Check application logs
heroku logs --tail  # For Heroku
# Or check Railway/Render dashboard logs

# Restart application
heroku restart  # For Heroku
# Or redeploy on Railway/Render
```

## 🎯 Success Indicators

✅ **Application URL**: Your deployed app URL  
✅ **Login Page**: Clean, no demo credentials shown  
✅ **Three Dashboards**: Different color themes working  
✅ **Database**: All data loading correctly  
✅ **File Upload**: Assignment submission working  
✅ **Responsive**: Works on mobile devices  

## 📞 Support

If you encounter issues:
1. Check deployment platform logs
2. Verify environment variables
3. Test database connection
4. Review application.properties configuration

---

**🎓 Your School Management System is ready for production deployment!**

Choose your preferred platform and follow the steps above. Railway is recommended for its simplicity and automatic MySQL integration.
