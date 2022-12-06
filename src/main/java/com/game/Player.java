package com.game;

import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Player implements Serializable {
    private String name;
    private int gamesPlayed = 0;
    private int wins = 0;
    private int losses = 0;

    public Player(String nameGiven) {
        nameGiven = nameGiven.toLowerCase().replaceAll(" ", "_");
        File filepath = new File("savedGameData/" + nameGiven + ".bin");

        // Tarkista onko tämän niminen pelaaja jo tallennettuna.
        if (filepath.isFile()) { 

            // Lataa pelaajan tiedot tiedostosta.
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath));
                Player p1 = (Player) ois.readObject();
                this.name = p1.name;
                this.gamesPlayed = p1.gamesPlayed;
                this.wins = p1.wins;
                this.losses = p1.losses;
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            // Luo uusi pelaaja (tätä ei vielä tallenneta)
            this.name = nameGiven;
        }
    }

    private void saveToFile() {
        File filepath = new File("savedGameData/" + this.name + ".bin");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath));
            oos.writeObject(this);
            oos.close();
            //System.out.println(" Player data was saved to a file");
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("The saving failed");
        }
    }

    public void incrementWins() {
        this.gamesPlayed += 1;
        this.wins += 1;
        saveToFile();
    }

    public void incrementLosses() {
        this.gamesPlayed += 1;
        this.losses += 1;
        saveToFile();
    }

    // I removed my other getters and setter which were not needed by the game to run...
    public String getName() {
        return this.name;
    }

    public String toString() {
        return (
            "\nPlayer: " + this.name + 
            "\nGames played: " + this.gamesPlayed + 
            "\nWins: " + this.wins + 
            "\nLosses: " + this.losses + "\n");
    }
}
