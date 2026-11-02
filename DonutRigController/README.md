# Donut Rig Controller

A client-side Fabric mod project for Minecraft Java 1.21.11.

## What it does

- Opens a controller screen with **Right = you** and **Left = viewer**.
- Lets you choose values from 1 to 9.
- `RIGHT >= LEFT` mode automatically keeps the right value at least as high as the left.
- `EQUAL` mode keeps both values equal.
- `RANDOM VALID` selects a random pair satisfying the current mode.
- Saves settings to `config/donut-rig-controller.properties`.
- Default keybind: **Right Shift**.

## Important limitation

This is a visual planner only. It does not interact with blocks, press levers, send packets, or alter the actual output of server-side droppers. A client-only mod cannot decide what a remote server's dropper produces.

## Requirements

- Minecraft Java Edition 1.21.11
- Java 21 JDK
- Fabric Loader
- Fabric API for 1.21.11
- Gradle 8.14+ (or use the Gradle version supported by your installed Fabric Loom)

## Build

From this project folder, run:

```bash
gradle build
```

The mod JAR will be created in:

```text
build/libs/donut-rig-controller-1.0.0.jar
```

Copy that JAR into your Minecraft instance's `mods` folder. Fabric API must also be installed.

## Controls

- **Right Shift**: open/close controller.
- Click a number in the RIGHT row to set your displayed number.
- Click a number in the LEFT row to set the viewer's displayed number.
- **MODE** switches between `RIGHT >= LEFT` and `EQUAL`.
- **RANDOM VALID** picks a valid pair.
- **CONTROLLER: ON/OFF** toggles the planner state.
