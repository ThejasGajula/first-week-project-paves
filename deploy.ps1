# Set your Docker Hub username here
$dockerUser = "rohitlingarker"

Write-Host "Rebuilding and pushing frontend image..."
cd front-end
docker build -t "$dockerUser/frontend:latest" .
docker push "$dockerUser/frontend:latest"
cd ..

Write-Host "Rebuilding and pushing gesture API image..."
cd gesture_python
docker build -t "$dockerUser/gesture-api:latest" .
docker push "$dockerUser/gesture-api:latest"
cd ..

Write-Host "Rebuilding and pushing backend image..."
cd middleware
if (Test-Path .\mvnw.cmd) {
    .\mvnw.cmd clean package -DskipTests
} else {
    mvn clean package -DskipTests
}
docker build -t "$dockerUser/backend:latest" .
docker push "$dockerUser/backend:latest"
cd ..

Write-Host "All images built and pushed successfully!"
