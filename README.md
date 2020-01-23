# Spring-boot-Kafka

Step1: start Kafka 
foleder: Spring-boot-Kafka/Docker-kafka-server-examples/cp-all-in-one
command: docker-compose up -d --build
Step2: start Producer
Step3: start Consumer

Run command console
Producer: mvn spring-boot:run 
Consumer first: mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085,--spring.profiles.active=C1
Consumer second: mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8086,--spring.profiles.active=C2
