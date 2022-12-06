package com.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

	// If you want to play the game in the console instead, 
	// then disable the main method above and uncomment the one below

/* 	public static void main(String[] args) throws Exception {
        Player p1 = new Player("Mr X");
        Game game = new Game(p1);
        game.startInConsole();
    } */

}
