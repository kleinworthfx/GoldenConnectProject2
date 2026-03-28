# GoldenConnect Login/Registration Flow Test Guide

## 🎯 **Test Objectives**
Verify that the login and registration functionality works correctly with the database integration.

## 🚀 **How to Run the Application**

### Option 1: Using Maven (Recommended)
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="za.ac.cput.goldenconnect.ui.MainApplication"
```

### Option 2: Using the Batch File
```bash
test_login.bat
```

### Option 3: Using IDE
Run `MainApplication.java` directly from your IDE.

## 📋 **Test Cases**

### **Test Case 1: Database Connection**
**Objective**: Verify database connectivity
- [ ] Application starts without database errors
- [ ] No connection timeout messages
- [ ] Login screen loads properly

### **Test Case 2: Registration Flow**
**Objective**: Test new user registration

#### **Step 1: Navigate to Registration**
- [ ] Click "Don't have an account? Sign Up" on login screen
- [ ] Registration form loads correctly
- [ ] All form fields are present and functional

#### **Step 2: Fill Registration Form**
- [ ] Enter full name: "Test User"
- [ ] Enter email: "test.user@example.com"
- [ ] Select role: "VOLUNTEER"
- [ ] Enter password: "testpassword123"
- [ ] Confirm password: "testpassword123"

#### **Step 3: Submit Registration**
- [ ] Click "Create Account" button
- [ ] Success message appears
- [ ] User is automatically logged in
- [ ] Dashboard loads with user information

### **Test Case 3: Login Flow**
**Objective**: Test existing user login

#### **Step 1: Use Sample User**
- [ ] Email: "john.smith@email.com"
- [ ] Password: "password123"
- [ ] Click "Sign In" button
- [ ] Success message appears
- [ ] Dashboard loads with user information

#### **Step 2: Use Newly Created User**
- [ ] Email: "test.user@example.com"
- [ ] Password: "testpassword123"
- [ ] Click "Sign In" button
- [ ] Success message appears
- [ ] Dashboard loads with user information

### **Test Case 4: Error Handling**
**Objective**: Test error scenarios

#### **Step 1: Invalid Login**
- [ ] Enter invalid email: "invalid@email.com"
- [ ] Enter invalid password: "wrongpassword"
- [ ] Click "Sign In" button
- [ ] Error message appears: "Invalid credentials"

#### **Step 2: Empty Fields**
- [ ] Leave email field empty
- [ ] Leave password field empty
- [ ] Click "Sign In" button
- [ ] Error message appears: "Please fill in all fields"

#### **Step 3: Registration Validation**
- [ ] Try to register with existing email
- [ ] Try to register with mismatched passwords
- [ ] Try to register with empty fields
- [ ] Verify appropriate error messages appear

### **Test Case 5: Navigation Flow**
**Objective**: Test navigation between screens

#### **Step 1: Login to Registration**
- [ ] From login screen, click "Don't have an account? Sign Up"
- [ ] Registration screen loads
- [ ] Form fields are empty and ready for input

#### **Step 2: Registration to Login**
- [ ] From registration screen, click "Already have an account? Sign In"
- [ ] Login screen loads
- [ ] Form fields are empty and ready for input

#### **Step 3: Dashboard Navigation**
- [ ] After successful login, verify dashboard loads
- [ ] Check that navigation panel appears on the left
- [ ] Verify user information is displayed correctly

### **Test Case 6: Logout Flow**
**Objective**: Test logout functionality

#### **Step 1: Logout Process**
- [ ] From dashboard, click "Logout" in navigation panel
- [ ] Success message appears: "You have been successfully logged out"
- [ ] User is redirected to login screen
- [ ] Form fields are cleared

## 🔍 **Expected Results**

### **Successful Registration**
- User account is created in database
- User is automatically logged in
- Dashboard shows user information
- Navigation panel displays user name

### **Successful Login**
- User is authenticated against database
- Dashboard loads with user-specific data
- Navigation panel shows correct user information
- All navigation options are available

### **Error Handling**
- Clear error messages for invalid inputs
- Form validation prevents invalid submissions
- Application remains stable during errors
- User can retry after errors

## 🐛 **Common Issues & Solutions**

### **Issue 1: Database Connection Failed**
**Symptoms**: Application won't start or shows connection errors
**Solution**: 
- Verify MySQL is running
- Check database credentials in `DatabaseConnectionManager.java`
- Ensure `goldenconnect` database exists

### **Issue 2: Login Always Fails**
**Symptoms**: Valid credentials don't work
**Solution**:
- Check if sample data was loaded correctly
- Verify password hashing is working
- Check database table structure

### **Issue 3: Registration Fails**
**Symptoms**: New user registration doesn't work
**Solution**:
- Check database permissions
- Verify email uniqueness constraints
- Check for validation errors

## 📊 **Test Results Template**

```
Test Date: _______________
Tester: _______________

Test Case | Status | Notes
---------|--------|------
Database Connection | ⬜ Pass ⬜ Fail | 
Registration Flow | ⬜ Pass ⬜ Fail | 
Login Flow | ⬜ Pass ⬜ Fail | 
Error Handling | ⬜ Pass ⬜ Fail | 
Navigation Flow | ⬜ Pass ⬜ Fail | 
Logout Flow | ⬜ Pass ⬜ Fail | 

Overall Result: ⬜ PASS ⬜ FAIL

Issues Found:
1. ________________________________
2. ________________________________
3. ________________________________

Recommendations:
1. ________________________________
2. ________________________________
3. ________________________________
```

## 🎉 **Success Criteria**

The login/registration flow test is **PASSED** when:
- ✅ All test cases pass
- ✅ Database integration works correctly
- ✅ Error handling is robust
- ✅ User experience is smooth
- ✅ No critical bugs are found

---

**Happy Testing! 🚀**
