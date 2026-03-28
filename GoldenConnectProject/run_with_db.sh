#!/bin/bash

echo "========================================"
echo "GoldenConnect Database Setup"
echo "========================================"
echo

# Prompt for database credentials
read -p "Enter MySQL username (default: root): " DB_USERNAME
DB_USERNAME=${DB_USERNAME:-root}

read -s -p "Enter MySQL password: " DB_PASSWORD
echo
if [ -z "$DB_PASSWORD" ]; then
    echo "Error: Password is required!"
    exit 1
fi

read -p "Enter database URL (default: jdbc:mysql://localhost:3306/goldenconnect): " DB_URL
DB_URL=${DB_URL:-jdbc:mysql://localhost:3306/goldenconnect}

echo
echo "Setting up environment variables..."
echo "Database URL: $DB_URL"
echo "Username: $DB_USERNAME"
echo "Password: [HIDDEN]"
echo

# Set environment variables
export GOLDENCONNECT_DB_URL="$DB_URL"
export GOLDENCONNECT_DB_USERNAME="$DB_USERNAME"
export GOLDENCONNECT_DB_PASSWORD="$DB_PASSWORD"

echo "Environment variables set successfully!"
echo
echo "Starting GoldenConnect application..."
echo

# Run the application
mvn javafx:run

echo
echo "Application closed."
