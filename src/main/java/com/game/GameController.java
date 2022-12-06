package com.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private Player currentPlayer = null;
    private Game game = null;

    // Activate a player profile (create/load).
    // The methods for creating a new profile and loading an old one are combined here -
    // a practice I've seen with older text based games, and it sure made the game more fluent.
    @PostMapping("/player")
    public String activatePlayer(@RequestParam String name) {
        if (currentPlayer != null) {
            return ("You need to unactivate the current player before activating a new one\n\n"
                    + "The current player is: " + currentPlayer.getName());
        } else {
            name = name.trim().toLowerCase().replaceAll(" ", "_");
            if (name.length() > 1) {
                currentPlayer = new Player(name);
                return "Player profile activated for " + name;
            } else {
                return "That's not a valid player name!";
            }
        }
    }

    // Create and activate a new game instance.
    // You need to have an activated player before doing this.
    @PostMapping("/game")
    public String createGame() {
        if (game != null) {
            return "You already have a game running!";
        } else if (currentPlayer == null) {
            return "You need to first activate a player profile!";
        } else {
            game = new Game(currentPlayer);
            game.start();
            return "The game is now running!\nThe word is: " + game.concealedWord;
        }
    }

    // MAKE A MOVE
    // I originally went with separate requests, but in the end it
    // made more sense to group everything together for ease of use and speed of play.
    // I used the PathVariable instead of RequestParam just because I wanted to test it too.
    @PutMapping("game/move/{string}")
    public String playerMove(@PathVariable String string) {

        String response;
        string = string.trim().toUpperCase();
        
        if (game == null) {
            response = "You have no initialized game instance!";
        } else if (game.isRunning == false) {
            response = "There's no game currently running!";
        } else {
            if (string.equals("0")) {
                game.quit();
                response = "The game has been closed";
            } else if (string.length() == 1) {
                response = game.guessALetter(string);
            } else if (string.length() > 1) {
                response = game.guessTheWord(string);
            } else {
                response = "You need to input a letter, word or 0";
            }

            if (game.isRunning == false) {
                game = null;
                response = response + "\n" + currentPlayer.toString();
            }
        }
        return response;
    }

    // Close the game instance.
    @PutMapping("game/exit")
    public String endGame() {
        if (game != null) {
            game = null;
            return "The game was closed";
        } else {
            return "There's no game currently running!";
        }
    }

    // Unactivate the current player and close the game instance, if it exists.
    @PutMapping("player/exit")
    public String exitPlayer() {
        String feedback;
        if (game != null) {
            feedback = "The game was closed & the player " + currentPlayer.getName() + " was unactivated";
            game = null;
            currentPlayer = null;
        } else if (currentPlayer != null) {
            feedback = "The player " + currentPlayer.getName() + " was unactivated";
            currentPlayer = null;
        } else {
            feedback = "There is no active player!";
        }
        return feedback;
    }
  
    // Get info of the current active player.
    @GetMapping("/player")
    public String getPlayer() {
        if (currentPlayer != null) {
            return currentPlayer.toString();
        } else {
            return "You have not activated a player profile!";
        }
    }

    // Get info of the current active game & its player.
    @GetMapping("/game")
    public String getGame() {
        if (game != null) {
            return (game.player
            + "\nThe word: " + game.concealedWord
            + "\n" + game.guessesLeft + " guesses left");
        } else {
            return "There's no game currently running!";
        }
    }

}
