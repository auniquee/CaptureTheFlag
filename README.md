# Capture The Flag Plugin

## Overview
A Capture The Flag (CTF) plugin for Minecraft using PaperMC, featuring teams, flags, power-ups, and game automation.

## Features
- Automatic team assignment
- Flag capture mechanics
- Power-up spawning
- Team-based armor and inventory setup
- Gradle build automation

## Installation
1. Clone the repository and navigate to the project folder.
2. Build the plugin using Gradle: `./gradlew build`
3. Locate the generated `.jar` file in the `build/libs` folder.
4. Place the `.jar` file in the `plugins` folder of your PaperMC server.
5. Configure teams, maps, and settings in the config files.
6. Start the server and initiate the game with `/startctf` (or relevant command).

## Usage
- Players are assigned to teams automatically.
- Flags spawn at configured locations.
- Right click enemy flags to take it, capture it at your own base.
- 

## Dependencies
- Paperweight Gradle plugin

## Commands
- `/cts [map]` - Start the game with specified map

## License
MIT License

