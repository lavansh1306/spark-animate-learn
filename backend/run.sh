#!/bin/sh

echo "🚀 Starting Backend Build and Test..."
echo ""

# Check if Java is installed
if ! command -v java >/dev/null 2>&1; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

echo "✅ Java version:"
java -version
echo ""

# Check if Maven is installed
if ! command -v mvn >/dev/null 2>&1; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

echo "✅ Maven version:"
mvn -version
echo ""

# Navigate to backend directory
cd "$(dirname "$0")"

echo "📦 Installing dependencies..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""
echo "🏃 Starting Spring Boot application..."
echo ""
echo "Backend will be available at: http://localhost:8080"
echo "H2 Console: http://localhost:8080/h2-console"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

mvn spring-boot:run
