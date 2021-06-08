all: build 
	sudo docker-compose -f docker-compose up -d 
	sudo docker stats
	
.PHONY:
build:
	mvn -f ./cab-commons clean install -DskipTests
	mvn -f ./cab_test clean package -DskipTests
	mvn -f ./cab_user clean package -DskipTests
	mvn -f ./gateway clean package -DskipTests

.PHONY:
clean:
	sudo docker-compose -f docker-compose down; 
	sudo docker image rm myawesomecab_cab_gateway myawesomecab_cab_user myawesomecab_cab;
