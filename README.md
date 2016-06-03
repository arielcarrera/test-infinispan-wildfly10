# test-infinispan-wildfly10
A quick start test project to test infinispan subsystem configuration in wildfly 10 and wildfly 8

## Install

### Wildfly container
Add to standalone-full.xml into infinispan section: 

run wildfly

	cd $WILDFLY_HOME/bin
	
	linux:  ./standalone.sh -c standalone-full.xml --debug
	window: ./standalone.bat -c standalone-full.xml --debug

## Config Wildfly infinispan
run
	
	jboss-cli.sh -c --file=config.cli

### build
Build project for wildfly 10:

	cd $PROJECT_DIR/

	mvn clean package
	
Build project for wildfly 8:	
	wildfly 8:  mvn clean package -Pwildfly8

### Deploy
Deploy (localhost)

	mvn wildfly:deploy

### Test application
Browse to:
test:

	http://localhost:8080/test-infinispan-wildfly10/test
	
