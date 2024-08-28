sudo service postgresql stop
sudo docker rm rabbitmq
sudo docker-compose up -d
./mvnw spring-boot:run
sudo docker-compose down