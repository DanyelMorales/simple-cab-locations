localpkg:
	mvn -f ./cab_test package -DskipTests
	mvn -f ./cab_user package -DskipTests
	