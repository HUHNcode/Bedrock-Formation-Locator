# Bedrock-Finder

Finds Minecraft coordinates from bedrock patterns using world seeds. This is a Java-based tool that allows you to define custom bedrock patterns and search for them in a given Minecraft world seed.

## Features

- Search for bedrock patterns in a given world seed.
- Define custom patterns for different Y-levels.
- Search for connected patterns across multiple Y-levels.
- Multi-threaded search for faster results.

## Prerequisites

- Java Development Kit (JDK) 17 or later.

## Getting Started

### 1. Download the Latest Release

Download the latest `Bedrock-Finder-X.X.zip` file from the [releases page](https://github.com/TimonGaebele/Bedrock-Finder/releases).

### 2. Configuration

1.  Unzip the downloaded file in a location of your choice.
2.  Edit the `config.json` file to configure your search parameters. For detailed information on all configuration options, please refer to the [Configuration Documentation](CONFIG_DOCUMENTATION.md).

### 3. Running the Application

#### Windows

Open a command prompt in the folder where you unzipped the release and run the following command (replace `X.X` with the version number):

```bash
java -jar Bedrock-Finder-vX.X.jar
```

Alternatively, you can create a `.bat` file with the following content for easier execution:

```batch
java -jar Bedrock-Finder-vX.X.jar
pause
```

#### Linux / macOS

Open a terminal in the folder where you unzipped the release and run the following command (replace `X.X` with the version number):

```bash
java -jar Bedrock-Finder-vX.X.jar
```

## For Developers

### Building from Source

1.  Clone the repository: `git clone https://github.com/TimonGaebele/Bedrock-Finder.git`
2.  Run the `distribution` task: `./gradlew clean distribution`
3.  The distribution zip file will be created in the `app/build/distributions` directory.

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.

## License

This project is currently not licensed.
