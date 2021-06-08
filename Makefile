all: build 
	sudo docker-compose -f docker-compose up -d 
	sudo docker stats
	
.PHONY:
build:
	mvn -f ./cab-commons install -DskipTests
	mvn -f ./cab_test package -DskipTests
	mvn -f ./cab_user package -DskipTests
	mvn -f ./gateway package -DskipTests
