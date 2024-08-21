sudo service postgresql stop
sudo docker-compose up -d
./mvnw spring-boot:run
sudo docker-compose down