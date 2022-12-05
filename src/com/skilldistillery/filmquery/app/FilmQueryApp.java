package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		FilmQueryApp app = new FilmQueryApp();
		Scanner sc = new Scanner(System.in);
		app.startUserInterface(sc);
	}

	private void printFilmList() throws ClassNotFoundException, SQLException {
		DatabaseAccessor db = new DatabaseAccessorObject();
		for (Film film : db.findFilmByKeyword(null)) {
			System.out.println(film.getTitle() + " " + film.getReleaseYear() + " " + film.getRating() + " \n"
					+ film.getDescription() + "\n");
		}
	}

	private void startUserInterface(Scanner sc) throws ClassNotFoundException, SQLException {
		int input = 0;
		DatabaseAccessor db = new DatabaseAccessorObject();
		System.out.println("Select would you like to do");
		System.out.println("1) Look up a film by its ID");
		System.out.println("2) Look up a film by a keyword");
		System.out.println("3) Exit the application");
		input = sc.nextInt();
		if (input == 1) {
			System.out.println("Enter the numerical ID of the film you'd like to see");
			input = sc.nextInt();
			Film searchedFilm = db.findFilmById(input);
			if (searchedFilm.getTitle() == null && searchedFilm.getDescription() == null) {
				System.out.println("There is no film matching your request");
			} else {
				System.out.println(
						searchedFilm.getTitle() + " " + searchedFilm.getReleaseYear() + " " + searchedFilm.getRating()
								+ " " + searchedFilm.getLangName() + " \n" + searchedFilm.getDescription());
			}
			startUserInterface(sc);
		}

		if (input == 2) {
			System.out.println("What keyword would you like to search for?");
			String keyword = sc.next();
			List<Film> searchedFilm = db.findFilmByKeyword(keyword);
			for (Film film : searchedFilm) {
				System.out.println(film.getTitle() + " " + film.getReleaseYear() + " " + film.getRating() + " " + film.getLangName() + " \n"
						+ film.getDescription() + "\n");
			}
			if (db.findFilmByKeyword(keyword).isEmpty()) {
				System.out.println("There is no film matching your request");
			}
			startUserInterface(sc);
		}
		if (input == 3) {
			System.out.println("Adieu");
		}
		sc.close();
	}
}
