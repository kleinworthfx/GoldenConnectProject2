# 🚀 Creating GoldenConnect Executable (.exe)

## **📋 Overview**

This guide provides multiple methods to create an executable (.exe) file for your GoldenConnect JavaFX application. Choose the method that best suits your needs.

---

## **🔧 Method 1: Simple Batch Launcher (Easiest)**

### **Step 1: Build the Application**
```bash
# Clean and package
mvn clean package -DskipTests
```

### **Step 2: Create Launcher**
Run the provided batch script:
```bash
build_executable.bat
```

### **Step 3: Result**
- Creates `GoldenConnect.exe` (batch launcher)
- Creates `target/goldenconnect-1.0.0.jar` (executable JAR)

### **Usage**
- Double-click `GoldenConnect.exe`
- Or run: `java -jar target/goldenconnect-1.0.0.jar`

---

## **🔧 Method 2: Launch4j (Professional)**

### **Step 1: Install Launch4j**
1. Download Launch4j from: https://launch4j.sourceforge.net/
2. Extract to a folder (e.g., `C:\launch4j`)

### **Step 2: Build the Application**
```bash
mvn clean package -DskipTests
```

### **Step 3: Create Executable**
```bash
# Navigate to Launch4j directory
cd C:\launch4j

# Run Launch4j with our config
launch4j.exe "C:\path\to\your\project\launch4j-config.xml"
```

### **Step 4: Result**
- Creates `GoldenConnect.exe` (native Windows executable)
- Includes proper Windows metadata and icon

---

## **🔧 Method 3: JPackage (Modern - Requires JDK 14+)**

### **Step 1: Build the Application**
```bash
mvn clean package -DskipTests
```

### **Step 2: Create Runtime Image**
```bash
# Create custom runtime
jlink --module-path "%JAVA_HOME%\jmods" --add-modules java.base,java.desktop,java.logging,java.sql,java.xml,javafx.controls,javafx.fxml,javafx.graphics,javafx.base --output runtime
```

### **Step 3: Create Executable**
```bash
# Create Windows installer/executable
jpackage --input target --name GoldenConnect --main-jar goldenconnect-1.0.0.jar --main-class za.ac.cput.goldenconnect.ui.MainApplication --runtime-image runtime --type exe --win-dir-chooser --win-menu --win-shortcut
```

---

## **🔧 Method 4: GraalVM Native Image (Advanced)**

### **Step 1: Install GraalVM**
1. Download GraalVM from: https://www.graalvm.org/
2. Set `GRAALVM_HOME` environment variable

### **Step 2: Build Native Image**
```bash
# Install native-image component
gu install native-image

# Build native executable
native-image -jar target/goldenconnect-1.0.0.jar GoldenConnect
```

### **Step 3: Result**
- Creates `GoldenConnect.exe` (native executable)
- No JVM required to run

---

## **📦 Distribution Options**

### **Option A: JAR + Launcher**
- **Pros**: Simple, works on any system with Java
- **Cons**: Requires Java installation
- **Files**: `GoldenConnect.exe` + `goldenconnect-1.0.0.jar`

### **Option B: Launch4j Executable**
- **Pros**: Native Windows executable, professional look
- **Cons**: Still requires Java runtime
- **Files**: `GoldenConnect.exe` (standalone)

### **Option C: JPackage Installer**
- **Pros**: Windows installer, includes Java runtime
- **Cons**: Larger file size
- **Files**: `GoldenConnect-1.0.0.exe` (installer)

### **Option D: Native Image**
- **Pros**: No Java required, fastest startup
- **Cons**: Complex build process, larger executable
- **Files**: `GoldenConnect.exe` (native)

---

## **🔍 Troubleshooting**

### **Common Issues:**

#### **1. "Java not found" error**
- **Solution**: Install Java 17+ and add to PATH
- **Check**: Run `java -version`

#### **2. "Main class not found" error**
- **Solution**: Verify main class in `pom.xml`
- **Check**: `za.ac.cput.goldenconnect.ui.MainApplication`

#### **3. Launch4j build fails**
- **Solution**: Check Launch4j configuration
- **Verify**: JAR file exists and is valid

#### **4. JPackage fails**
- **Solution**: Ensure JDK 14+ is installed
- **Check**: Run `jpackage --version`

#### **5. Native image build fails**
- **Solution**: Install Visual Studio Build Tools
- **Windows**: Install "C++ build tools" workload

---

## **📝 Recommended Approach**

### **For Development/Testing:**
Use **Method 1** (Simple Batch Launcher)
- Quick and easy
- Good for testing
- Easy to modify

### **For Distribution:**
Use **Method 2** (Launch4j)
- Professional appearance
- Proper Windows integration
- Good user experience

### **For Production:**
Use **Method 3** (JPackage)
- Modern approach
- Includes Java runtime
- Creates proper installer

---

## **🔐 Security Considerations**

- **Code Signing**: Consider signing your executable for Windows
- **Antivirus**: Some antivirus software may flag Java executables
- **Permissions**: Ensure proper file permissions
- **Updates**: Plan for application updates

---

## **📞 Support**

If you encounter issues:
1. Check the troubleshooting section
2. Verify Java and Maven installation
3. Ensure all dependencies are resolved
4. Check build logs for specific errors
