#!/bin/bash

# Set the project directory (assuming the script is in the project root)
PROJECT_DIR=$(pwd)

# Navigate to the project directory
cd $PROJECT_DIR

# Clean and package the Maven project
echo "Cleaning and packaging the Maven project..."
./mvnw clean package

# Check if the Maven build was successful
if [ $? -eq 0 ]; then
    echo "Maven build successful."
else
    echo "Maven build failed. Exiting."
    exit 1
fi

# Find the generated JAR file in the target directory
JAR_FILE=$(find target -name "*.jar" | head -n 1)

# Check if the JAR file was found
if [ -z "$JAR_FILE" ]; then
    echo "No JAR file found in the target directory. Exiting."
    exit 1
fi

# Display the location of the new JAR file
echo "New JAR file created at: $JAR_FILE"

# Optionally, you can copy the JAR file to a specific directory (e.g., deploy or dist)
# cp $JAR_FILE /path/to/deploy/directory/

# Make the JAR file executable (if it’s a Spring Boot application with an embedded server)
chmod +x $JAR_FILE

# Run the JAR file (optional)
# echo "Running the JAR file..."
# java -jar $JAR_FILE

echo "Done."
