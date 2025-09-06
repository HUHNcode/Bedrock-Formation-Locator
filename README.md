# Bedrock-Formation-Locator

Finds Minecraft coordinates from bedrock patterns using world seeds. This is a Java-based tool that allows you to define custom bedrock patterns and search for them in a given Minecraft world seed.

## Features

*   Search for bedrock patterns in a given world seed.
*   Define custom patterns for different Y-levels.
*   Search for connected patterns across multiple Y-levels.
*   Multi-threaded search for faster results.

## Prerequisites

*   Java Development Kit (JDK) 17 or later.

## Getting Started

### 1. Configuration

Before running the application, you need to configure your search parameters in the `config.json` file. This includes setting the world seed, search area, and defining the bedrock patterns you want to find.

For detailed information on all configuration options, please refer to the [Configuration Documentation](CONFIG_DOCUMENTATION.md).

### 2. Building the Application

You can build the application using the provided Gradle wrapper.

```bash
./gradlew build
```

This will compile the code and create a runnable JAR file in the `app/build/libs/` directory.

### 3. Running the Application

You can run the application in two ways:

**a) Using Gradle:**

```bash
./gradlew run
```

**b) Using the JAR file:**

```bash
java -jar app/build/libs/app.jar
```

The application will start searching for the configured bedrock patterns and print the coordinates of any matches to the console.

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.

## License

This project is currently not licensed.