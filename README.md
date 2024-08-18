# Sudoku Game

This is a Sudoku game application built using Java Swing. The application allows users to play Sudoku with different difficulty levels.

<img src="https://github.com/WilmerCP/Sudoku/blob/f77e1d1b6d1857d2efaffcd0ca1b2c312c759f06/mainWindow.png" width="480">

<img src="https://github.com/WilmerCP/Sudoku/blob/f77e1d1b6d1857d2efaffcd0ca1b2c312c759f06/menu.png" width="480">

<img src="https://github.com/WilmerCP/Sudoku/blob/f77e1d1b6d1857d2efaffcd0ca1b2c312c759f06/screenshot.png" width="480">

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 22 or higher
- Maven

### Building the Project

To build the project, run the following command in the root directory:

```sh
mvn clean package
```

This will generate a **sudoku.jar** file in the **target** directory.

### Running the Application

To run the application, use the following command:

```sh
java -jar target/sudoku.jar
```

### Classes Overview

#### App.java

The main entry point of the application. It initializes and starts the application.

#### GameWindow.java

The main game window where the Sudoku game is played. It extends `JFrame` and implements `ActionListener` and `Serializable`.

#### MainWindow.java

The main window of the application. It extends `JFrame` and implements `ActionListener` and `Serializable`.

#### MenuWindow.java

The menu window where users can select the difficulty level and start the game.

#### SudokuFetcher.java

Fetches Sudoku puzzles from local data files.

### Resources

#### Local Data

- Easy.txt
- Medium.txt
- Hard.txt

These files contain Sudoku puzzles for different difficulty levels.

### License

This project is licensed under the MIT License.
