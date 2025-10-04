#!/bin/bash

# Bangladesh Police Management System - Run Script

echo "🚔 Bangladesh Police Management System"
echo "======================================"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $(java -version 2>&1 | head -n1)"
    exit 1
fi

echo "✅ Java $JAVA_VERSION detected"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven."
    exit 1
fi

echo "✅ Maven detected"

# Build the project
echo ""
echo "🔨 Building project..."
mvn clean compile javafx:run

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Application started successfully!"
    echo ""
    echo "📋 Login Credentials:"
    echo "   Admin: admin / admin123"
    echo "   Officer: BD001 / officer123"
    echo ""
    echo "🌐 The application should open in a new window."
    echo "   - Use the tab interface to switch between Admin and Officer login"
    echo "   - Explore the dashboard features and chat functionality"
    echo ""
else
    echo ""
    echo "❌ Build failed. Check the error messages above."
    exit 1
fi