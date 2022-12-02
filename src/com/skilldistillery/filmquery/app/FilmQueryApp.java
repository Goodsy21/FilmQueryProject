package com.skilldistillery.filmquery.app;

import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
    Scanner sc = new Scanner(System.in);
    int filmId = 0;
    do {
    	System.out.println("Enter the actor ID who you want to see films from");
    	filmId = sc.nextInt();
    	sc.nextLine();
    	app.test(filmId);
    } while (filmId != 0);
    System.out.println("Adieu");
    
    sc.close();
//    app.launch(); this is the weekend project
  }

  private void test(int filmId) {
	  DatabaseAccessor db = new DatabaseAccessorObject();
    Film film = db.findFilmById(1); //do this method first
    System.out.println(film);
  }

//  private void launch() {
//    Scanner input = new Scanner(System.in);
//    
//    startUserInterface(input);
//    
//    input.close();
//  }

  private void startUserInterface(Scanner input) {
    
  }

}
