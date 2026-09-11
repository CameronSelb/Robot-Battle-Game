# Robot Battle Game

Robot Battle Game is a Java-based client-server game where players control robots inside a virtual world. Players can create robots, move around the world, navigate obstacles, and battle other robots.

The server manages the game world, robots, obstacles, commands, and communication with clients. The project also includes a client application that connects to the server and handles requests and responses.

The game world can be configured using a JSON configuration file.

## Requirements

- Java JDK 17 or later
- Apache Maven
- IntelliJ IDEA or another Java-compatible IDE

## Technologies

- Java
- Maven
- Jackson
- JUnit
- TCP/IP networking
- JSON

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   ├── client/
│   │   │   ├── Client
│   │   │   ├── DataHandler
│   │   │   ├── RequestHandler
│   │   │   └── ResponseHandler
│   │   │
│   │   └── server/
│   │       ├── clientcommands/
│   │       ├── configuration/
│   │       ├── obstacles/
│   │       ├── protocol/
│   │       ├── servercommands/
│   │       ├── world/
│   │       ├── ClientHandler
│   │       ├── RobotWorldClient
│   │       ├── RobotWorldJsonClient
│   │       └── Server
│   │    
│   │
│   └── resources/
│       └── config/
│           └── world_config.json
