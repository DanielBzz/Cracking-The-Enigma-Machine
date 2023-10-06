## Cracking The Enigma
Cracking The Enigma is a Windows application (also got a console app) using java, tomcat server container, javafx, OkHTTP.
 
## About
The Enigma machine is a cipher device developed and used in the early- to mid-20th century to protect commercial, diplomatic, and military communication.
My application run a decoding contest by few entities: 
-Uboat, German submarine responsible for determining an initial code for the machine, 
distributing an encrypted message and deciding which of the various teams was able to decipher the message accurately (winner of the competition).

-Allies, Decoding teams of the allies competing among themselves to be the first to succeed in deciphering the encrypted message transmitted by the UBoat.

-Agent, Agents who are members of the various decoding teams and are responsible for performing the decoding tasks themselves. Each agent is a member of exactly one team.

-Battlefield, The battlefield where the competition takes place.
 Battleground settings include the name, how many decryption teams are participating in each competition, 
 and the difficulty level of the decryption. Each battle involves exactly one German submarine, and one or more decoding team.

## How to use
clone the repository. 

Server Activation: Run the file "runServer.bat".

UBoat Loading: Run the file "uBoatClient.bat".

Allies Loading: Run the file "alliesClient.bat".

Agent Loading: Run the file "agentClient.bat".

In the first screen of each application (except for the server), enter the username.
uBoat: 
- Click "Load File" to load the XML file.
- Select the desired reflector from the radio buttons.
- Choose the desired rotors (from right to left) - the upper button will position the rotor on the top letter, and the lower button will do the opposite. In addition, pressing the key in the center of the rotor will display the wiring inside the rotor itself.
- Then, go to the competition screen by clicking on the "contest" tab.
- Enter the desired string (only words from the dictionary) and then press the "process" key.
- Afterward, press the "Ready" key and wait.

Allies:
- Click on the desired competition and then press the "join" key.
- Choose the task size using the spinner and then press the "ready" key and wait for the competition to start.

Agent:
- Click on the desired "allies".
- Select the desired number of disturbances
- Enter the number of tasks you want to pull from the server in a single request.

## Technology  and concepts used
- Java 8.
- Tomacat - java servlet conntainer for the server part.
- JavaFX - for design the windows forms ui.
- OKHTTP - for efficient HTTP requests from client to server.
- multithreading - Dealt with multithreading aspects on the server side, including thread pool, internal blocking queue management, and thread synchronization.
- Json - json library, used to parse data between server and client.
- XML files - used to define the settings of a contest.
- OOP methodology
- Event oriented methodology.
