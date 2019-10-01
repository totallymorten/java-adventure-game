# java-adventure-game

A small experimental multiplayer engine written in java.

## Running the game (release)

You can either use the shell scripts included in the release or run manually as below.

1. First start the server (if you are running locally - not connecting to remote server)
2. Then start the client
3. If your are running locally, you can just leave "localhost" in the server field. Else specify server address.
4. You can navigate the login screen with keys up/down
5. Press enter to login to server and join world

### Starting the server
* java -classpath adventure.jar adventure.server.AdventureServer

### Starting the client
* java -classpath adventure.jar adventure.engine.AdventureGame


