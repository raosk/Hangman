package com.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class Game {
    Player player;
    String unconcealedWord, concealedWord, revealedLetters = "";
    int guessesLeft;
    boolean isRunning = false;

    public Game(Player player) {
        this.player = player;
    }

    public void start() {
        this.isRunning = true;
        this.guessesLeft = 7;
        ArrayList<String> wordList = new ArrayList<>();
        Random rnd = new Random();
        Scanner reader = null;

        // Load words from file, select one at random, conceal the word
        try {
            File myFile = new File("wordsToBeGuessed.txt");
            reader = new Scanner(myFile);
            while (reader.hasNextLine()) {
                String wordRead = reader.nextLine().trim().toUpperCase();
                wordList.add(wordRead);
            }
            int wordListSize = wordList.size();
            //System.out.println("There's a total of " + wordListSize + " words"); // Just for testing
            unconcealedWord = wordList.get(rnd.nextInt(0, wordListSize));
            concealedWord = unconcealedWord.replaceAll(".", "*");
            reader.close();
        } catch (Exception e) {
            System.out.println("Failed to read the file");
            e.printStackTrace();
        }
    }

    String guessALetter(String playerGuess) {
        char letter = playerGuess.charAt(0);
        boolean canContinue = true;
        String result = "";

        // Tarkista että arvaus on kirjain
        if ((playerGuess.length() != 1) & !(Character.isLetter(letter))) {
            canContinue = false;
            return (letter + " is not a letter!");
        }            

        // Jos kirjain kuuluu piilotettuun sanaan, lisää kirjain näkyvään sanaan.
        if (canContinue & unconcealedWord.indexOf(letter) != -1) {
            // Lisää kirjain oikein arvattujen kirjainten listaan.
            if (revealedLetters.length() == 0) {                // ONKO TÄÄ OSIO TARPEELLINEN?
                revealedLetters = Character.toString(letter);   // ONKO TÄÄ OSIO TARPEELLINEN?
            } else {
                revealedLetters += letter;                      // EIKÖ TÄÄ RIITÄ?
            }
            // Paljasta sanasta oikein arvattu kirjain.
            concealedWord = unconcealedWord;
            for (int i = 0; i < concealedWord.length(); i++){
                char c = concealedWord.charAt(i);       
                if (revealedLetters.indexOf(c) == -1)
                    concealedWord = concealedWord.replace(c, '*');
            }

            result = "The word has the letter \""+ Character.toUpperCase(letter) + "\"!\n";

            // Tarkista voitto
            if (concealedWord == unconcealedWord) {
                return result + victory();
            } else {
                return (result 
                        + "You have " + guessesLeft + " guesses remaing"
                        + "\n\nThe word is: " + concealedWord
                        );
            }
        }
        return "There's no \""+ letter + "\" in the word...\n" + endCheck();
    }

    String guessTheWord(String guess) {
        String feedback = "You guessed the word \""+ guess + "\"";
        if (guess.equals(unconcealedWord)) {
            return feedback + "\n" + victory();
        } else {
            return feedback + "\nIt's wrong!\n" + endCheck();
        }
    }

    String endCheck() {
        guessesLeft -= 1;
        if (guessesLeft == 0) {
            quit();
            return "Zero guesses left\nYou lost the game...\n\nThe word was: " + unconcealedWord;
        } else {
            return "You have " + guessesLeft + " guesses remaing\n\nThe word is: " + concealedWord;
        }
    }

    String victory() {
        player.incrementWins();
        this.isRunning = false;
        return "The word is " + unconcealedWord + "\nYou won the game!";
    }

    void quit() {
        player.incrementLosses();
        this.isRunning = false;
    }

    // You can use this method to play the game just in the console.
    // This is what I originally created before the implementing the Spring Boot.
    public void startInConsole() {
        String string;
        Scanner scanner;
        start();

        // The game loop
        System.out.println("\n\nThe game begins!");
        System.out.println("The word is: " + concealedWord);
        
        do {
            System.out.println("\nGuess a letter or word or type 0 to quit the game:");
            scanner = new Scanner(System.in);
            string = scanner.nextLine();
            String response;
            string = string.trim().toUpperCase();
            System.out.println();
            
                if (string.equals("0")) {
                    this.quit();
                    response = "The game has been closed";
                } else if (string.length() == 1) {
                    response = this.guessALetter(string);
                } else if (string.length() > 1) {
                    response = this.guessTheWord(string);
                } else {
                    response = "You need to input a letter, word or 0";
                }
    
                if (this.isRunning == false) {
                    response = response + "\n" + this.player.toString();
                }
            
            System.out.println(response);
             
        } while (isRunning);
        scanner.close();
    }
}
