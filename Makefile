RELEASE_VERSION = 0.1.2

.PHONY: build test run package clean tag release help docker_build docker_run docker_push

build:
	mvn compile

test:
	chmod +x scripts/Iteration2ReferenceServerTest.sh
	scripts/Iteration2ReferenceServerTest.sh || true

run:
	mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robots.server.Server"

package:
	mvn clean package

clean:
	mvn clean

tag:
	@git add pom.xml
	@git add Makefile
	@git add .lms/*
	@git commit -m "Release version $(RELEASE_VERSION)"
	@git tag release-$(RELEASE_VERSION)

release:
	@echo "Creating a release for version $(RELEASE_VERSION)"
	@sed -i 's/<version>.*<\/version>/<version>$(RELEASE_VERSION)<\/version>/' pom.xml
	make package
	make tag
	make docker_build

# ----------------------
# Docker targets
# ----------------------

docker_build:
	docker build -t robot-world:$(RELEASE_VERSION) .

docker_run:
	docker run -p 5050:5050 --name robot-world-$(RELEASE_VERSION) robot-world:$(RELEASE_VERSION)

docker_push:
	@if [ -z "$(DOCKER_REGISTRY)" ]; then echo "Please set DOCKER_REGISTRY (e.g. registry.gitlab.com//robot-world)" && exit 1; fi
	docker tag robot-world:$(RELEASE_VERSION) $(DOCKER_REGISTRY)/robot-world:$(RELEASE_VERSION)
	docker push $(DOCKER_REGISTRY)/robot-world:$(RELEASE_VERSION)

help:
	@echo "make build         : Compile"
	@echo "make test          : Run tests"
	@echo "make run           : Run the server"
	@echo "make package       : Build JAR"
	@echo "make clean         : Clean build files"
	@echo "make release       : Release with version $(RELEASE_VERSION)"
	@echo "make docker_build  : Build Docker image with version $(RELEASE_VERSION)"
	@echo "make docker_run    : Run Docker image locally"
	@echo "make docker_push   : Push Docker image to registry"


#RELEASE_VERSION = 0.1.2
#
#
#.PHONY: build test run package clean tag release help docker_build docker_run docker_push
#
#build:
#	mvn compile
#test:
#	chmod +x scripts/Iteration2ReferenceServerTest.sh
#	scripts/Iteration2ReferenceServerTest.sh || true
#
#run:
#	mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robots.server.Server"
#
#package:
#	mvn clean package
#
#clean:
#	mvn clean
#
#tag:
#	@
#	git add pom.xml
#	git add Makefile
#	git add .lms/*
#	git commit -m "Release version $(RELEASE_VERSION)"
#	git tag release-$(RELEASE_VERSION)
#
#
#
#release:
#	@
#	echo "creating a release for version $(RELEASE_VERSION)"
#	sed -i 's/<version>.*<\/version>/<version>$(RELEASE_VERSION)<\/version>/' pom.xml
#	make package
#	make tag
#
#
#help:
#
#	@echo "make build : To compile"
#	@echo "make test : To test"
#	@echo "make run : To run the server"
#	@echo "make package : To "
#
#
#build_image:
#	sudo docker build -t robot-world:0.0.2 .
#	sudo docker run -p 5050:5050 robot-world:0.0.2
