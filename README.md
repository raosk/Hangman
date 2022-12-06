# Hangman
a Java Spring Boot game
Playable both with HTTP requests and with the command line

## Basics
A version of the classic Hangman game. A player profile can be created or loaded, then a game instance can be created. A random mystery word is selected by the game, and its letters are concealed. The player can guess either a letter or a word. Guessing a correct letter reveals the word. Guessing all the correct letters or the correct word is required to win the game. The player has 7 guesses, but a guess is not spent if a correct letter or word is guessed, so the game ends if the player makes 7 wrong guesses. Player data is saved to a file after each game. Stored player data and current game data can be requested.

## Extra features
* Functionality to create/load multiple player profiles.
    For this feature I implemented the Serializable interface. Each player profile is saved to a separate file for easy management. The serialized profiles are created into the /savedGameData folder.
* Functionality to read the mystery words from a text file.
    The words are read with Scanner and then stored to an array list, from where a random word is then selected, as the game requires. New words can now easily be added for the game by writing them to the text file. Look at /wordsToBeGuessed.txt.
* I also combined several of the game's functionalities under singular methods.
    This makes it easier and faster to test and run the game. As main examples, the methods to Save/Load player profile are combined under a single request/method. As are the player's in-game options of guessing a letter, guessing a word, and quitting the game. Instead of having separate getter method for every variable of the player class, I overrided the toString() method, as most of the player data is always requested all at once by the game. Combining things and getting rid of redundant code felt very satisfying and made the files cleaner.

## Guide
To test the game, use the Thunder Client and the following HTTP requests:
* POST	localhost:8080/player?name=playerName
	* To create/load a player
* POST	localhost:8080/game
	* To create a game for the selected player
* PUT	localhost:8080/game/move/?
	* ? = type a letter to guess a letter
	* ? = type a word to guess a word
	* ? = type 0 to quit the game
* GET	localhost:8080/player
	* To get player information
* GET	localhost:8080/game
	* To get game & player information
* Some extra requests can also be found in GameController.java file.

* Alternatively, you can play the game in the console (see GameApplication.java). Both play styles use the same save files and core functionalities.
