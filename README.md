RobotWorld Game_Group_14
Version: 0.1.2

* Developers: @gemakazcpt024 , @thtshabcpt024 , @caselbcpt024 , @thmatabcpt024
  A turn-based robot battle simulation game with REST API support, CLI server management, and Dockerized deployment. Players can control robots, place obstacles, 
    and manage worlds through both CLI and web API.

* Table of Contents:
    - Project Overview
    - Features
    - Project Structure
    - Installation
    - Configuration
    - Usage:
       * Run Server
       * REST API
       * Docker
    - Testing:
       * CI/CD Pipeline
       * Contributing
       * License

* Project Overview:
     - RobotWorld is a client-server game where users control robots in a shared world. 
       Robots can move, shoot, and interact with obstacles. The server supports:
     - Command-line interactions for server admins
     - Multiple concurrent clients
     - REST API for world management and monitoring
     - Save and restore functionality using a database

* Features:
    - Launch and control robots from client terminals.
    - Dynamic obstacles like lakes, mountains, and pits.
    - Multiple robot types: Juggernaut, Tank, Soldier, Marksman, Sniper.
    - Multi-robot simulation with turn-based mechanics
    - Configurable world size, shields, shots, and other parameters
    - REST API endpoints for world state and restoration
    - Full Docker support for easy deployment
    - Unit and integration tests for server commands and world management
    - CI/CD pipelines for automated builds and tests

* Project Structure
    RobotWorld/
    ├── src/                       # Java source code
    │   ├── client/                # Client classes
    │   ├── server/                # Server classes
    │   ├── server/world/          # Robot, World, Position, RobotTypes, Direction
    │   └── webApi/                # REST API classes
    ├── scripts/                   # Utility scripts
    │   ├── build.sh
    │   ├── start.sh
    │   ├── stop.sh
    │   └── image.sh
    ├── libs/                      # JAR dependencies
    ├── pom.xml                     # Maven project dependencies
    ├── Makefile                    # Build and Docker commands
    ├── Dockerfile                  # Docker image setup
    ├── docker-compose.yml          # Compose setup
    ├── world_config.json           # Default world configuration
    └── tests/                      # Unit tests & Acceptance tests

* Requirements:
    - Java 21 JDK
    - Socket programming
    - JSON for communication
    - Maven 3.*
    - Docker & Docker Compose

* Configuration:
   - Default configuration enables us to modify the world size, shields, shots,
     and other parameters to suit any testing need.

* Usage:
  - Run Server --------> # compile( make build)
  - Run Server --------> # run server locally( make run)
  - Package Server to JAR -------> (make package)
  - Build clean artifacts -------> (make clean)
  
* REST API:
  - Endpoint      |  Method  |  Use
    /world/:name  |   GET    |  Restores saved world by name
     /world/state |   GET    |  Gets current world state(robots,obstacles, e.t.c)

* Docker: -----> The docker build and run the server in a container
   - Docker command:
        1. Build docker image run(docker build -t Robot_world_group_14:latest)
        2. Run docker image ( docker run -d --name myapp-container -p 5050 Robot_world_group_14:latest:latest)
        3. View logs (docker logs myapp-container)
        4. Exec into Container(docker exec -it myapp-container /bin/bash)
        5. Curl the Running App(curl http://localhost:5050)
   - Push to Docker Registry:
        1. Tag the Image(docker tag RobotWorldApp:latest thtshabcpt024/RobotWorldApp:latest)
        2. Push Image(docker push dockerhub-thtshabcpt024/RobotWorldApp:latest)
        3. Stop and Remove the Container(docker stop myapp-container
                                         docker rm myapp-container)
           
* Utility Scripts:
  Script:	    |        Description:
  - build.sh    | 	      Build Docker image
  - start.sh    | 	      Start container
  - stop.sh	    |        Stop container
  - image.sh    | 	      Show image details

* Testing:
    - Unit tests:
         - Run tests with Maven -----> (mvn test)
    - Acceptance Tests:
        - Iteration2ReferenceServerTest.sh runs automated acceptance tests to verify server-client communication,
          REST API responses, and game mechanics.
        - Execute ------>(chmod +x scripts/Iteration2ReferenceServerTest.sh
          scripts/Iteration2ReferenceServerTest.sh)
      
* CI/CD Pipeline
  - .gitlab-ci.yml is configured for:
          - Build: Docker image creation
          - Test: Run container and verify server logs
          - Deploy: Push image to Docker registry
  - Variables used:
          - $CI_JOB_TOKEN, $CI_REGISTRY_USER, $CI_REGISTRY

* Makefile Commands
    - make build         # Compile
    - make test          # Run tests
    - make run           # Run server
    - make package       # Build JAR
    - make clean         # Clean build files
    - make release       # Release version 0.1.2
    - make docker_build  # Build Docker image
    - make docker_run    # Run Docker image locally
    - make docker_push   # Push Docker image to registry
  
* Getting Started
  - Server
      To start the server:
    ```bash
      Run class:
      za.co.wethinkcode.robots.server.Server
    ```
  - Connecting a client
     To start the client:
    ```bash
    Run class:
      za.co.wethinkcode.robots.client.Client
     ```

* Launch  Robot
    - When prompted, use the launch command to create your robot in the game world. 
       For example: `launch soldier alpha`

* Supported Commands (after launching)
      - `forward <steps>` – Move forward
      - `back <steps>` – Move backward
      - `turn <left|right>` – Rotate direction
      - `look` – Scan surroundings 
      - `repair` – Fix damage 
      - `fire` – Shoot another robot 
      - `reload` – Refill shots 
      - `state` – View current robot status 
      - `help` – Show command list
.