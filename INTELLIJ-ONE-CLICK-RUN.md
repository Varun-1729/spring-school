# 🚀 IntelliJ IDEA One-Click Run Setup

## ✅ **AUTOMATIC BROWSER OPENING CONFIGURED!**

Your School Management System is now configured to automatically open the browser when you click the **Run** button in IntelliJ IDEA.

## 🎯 **How to Use (Super Simple):**

### **Method 1: Use the Green Run Button**
1. **Open** `SchoolManagementSystemApplication.java`
2. **Click** the green ▶️ **Run** button next to the main method
3. **Wait** 3 seconds after "Started SchoolManagementSystemApplication"
4. **Browser opens automatically** at login page!

### **Method 2: Use Run Configuration**
1. **Look** for "School Management System - Auto Browser" in run configurations dropdown
2. **Click** the green ▶️ **Run** button in toolbar
3. **Browser opens automatically**!

### **Method 3: Keyboard Shortcut**
1. **Press** `Shift + F10` (Windows/Linux) or `Ctrl + R` (Mac)
2. **Browser opens automatically**!

## 🔧 **What Happens Automatically:**

1. ✅ **Application starts** (Spring Boot)
2. ✅ **Database connects** (MySQL)
3. ✅ **Server ready** on port 8080
4. ✅ **Browser opens** automatically after 3 seconds
5. ✅ **Login page loads** with clean interface
6. ✅ **Credentials displayed** in console

## 🔐 **Login Credentials** (shown in console):
```
👨‍💼 Admin: admin@school.com / admin123
👩‍🏫 Teacher: teacher@school.com / teacher123  
🎓 Student: student@school.com / student123
```

## 🌐 **URL** (opens automatically):
`http://localhost:8080/school-login.html`

## 📊 **Console Output:**
```
🌐 Opening browser automatically...
📱 URL: http://localhost:8080/school-login.html
🔐 Demo Credentials:
   👨‍💼 Admin: admin@school.com / admin123
   👩‍🏫 Teacher: teacher@school.com / teacher123
   🎓 Student: student@school.com / student123
✅ Browser opened successfully!
```

## 🎨 **Features:**

### ✅ **Automatic Browser Opening:**
- **Waits** 3 seconds for server to be ready
- **Opens** default browser automatically
- **Navigates** directly to login page
- **Works** on Windows, Mac, and Linux

### ✅ **Development Profile:**
- **Enhanced logging** for debugging
- **SQL queries** visible in console
- **Auto-reload** on code changes
- **Development banner** with credentials

### ✅ **IntelliJ Integration:**
- **Custom run configuration** included
- **One-click launch** from toolbar
- **Keyboard shortcuts** supported
- **Debug mode** compatible

## 🔧 **Troubleshooting:**

### **Browser doesn't open:**
- **Wait** 5-10 seconds (server startup time)
- **Check console** for "Browser opened successfully!" message
- **Manually go to**: `http://localhost:8080/school-login.html`

### **Database connection error:**
- **Start MySQL** service
- **Check** database `school_management` exists
- **Verify** credentials in `application-dev.properties`

### **Port already in use:**
- **Stop** any running instances
- **Or change** port in `application-dev.properties`

## 📁 **Files Added for IntelliJ:**

| File | Purpose |
|------|---------|
| `BrowserAutoOpenConfig.java` | Auto-opens browser when app starts |
| `application-dev.properties` | Development profile configuration |
| `banner-dev.txt` | Custom development banner |
| `School_Management_System___Auto_Browser.xml` | IntelliJ run configuration |

## 🎯 **Perfect Workflow:**

1. **Open** IntelliJ IDEA
2. **Click** green ▶️ **Run** button
3. **Wait** 3 seconds
4. **Browser opens** automatically
5. **Login** with demo credentials
6. **Test** all three dashboards!

## 🎉 **Benefits:**

- ✅ **No terminal commands** needed
- ✅ **No manual browser opening**
- ✅ **No URL typing** required
- ✅ **Credentials shown** automatically
- ✅ **One-click solution**
- ✅ **Perfect for development**
- ✅ **Great for demonstrations**

---

## 🚀 **READY TO USE!**

**Just click the Run button in IntelliJ IDEA and everything happens automatically!**

**Perfect for:**
- ✅ **Daily development**
- ✅ **Quick testing**
- ✅ **Client demonstrations**
- ✅ **Code reviews**
- ✅ **Feature showcases**

**Your School Management System is now IntelliJ IDEA optimized!** 🎓
