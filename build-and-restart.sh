docker network create e-commerce-network
docker-compose up -d
docker network connect e-commerce-network postgres
docker build . -t e-commerce
docker stop e-commerce
docker rm e-commerce
docker run -d -p 8080:8080 --name=e-commerce -e SPRING_PROFILES_ACTIVE=prod --network e-commerce-network --restart unless-stopped e-commerce