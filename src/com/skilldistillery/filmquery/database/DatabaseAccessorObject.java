package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	String user = "student";
	String pass = "student";
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";

	@Override
	public Film findFilmById(int filmNum) throws ClassNotFoundException, SQLException {
		Connection conn = DriverManager.getConnection(URL, user, pass);
		Class.forName("com.mysql.cj.jdbc.Driver");
		Film film = new Film();
		String sql = "SELECT * FROM film WHERE id = ?";
		String sqlLang = "SELECT film.*, language.name FROM film WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		PreparedStatement stmt2 = conn.prepareStatement(sqlLang);
		stmt.setInt(1, filmNum);
		stmt2.setInt(1, filmNum);
		ResultSet fResult = stmt.executeQuery();
		if (fResult.next()) {
			film.setId(fResult.getInt("id"));
			film.setTitle(fResult.getString("title"));
			film.setDescription(fResult.getString("description"));
			film.setReleaseYear(fResult.getShort("release_year"));
			film.setLanguageId(fResult.getInt("language_id"));
		film.setLangName(findLangNamebyId(fResult.getInt("language_id")));
			film.setRentalDuration(fResult.getInt("rental_duration"));
			film.setRentalRate(fResult.getDouble("rental_rate"));
			film.setLength(fResult.getInt("length"));
		film.setReplacementCost(fResult.getDouble("replacement_cost"));
			film.setRating(fResult.getString("rating"));
			film.setSpecialFeatures(fResult.getString("special_features"));
			film.setCast(findActorsByFilmId(fResult.getInt("id")));
		}
		fResult.close();
		conn.close();
		return film;
	}
	private String findLangNamebyId(int languageId) throws ClassNotFoundException, SQLException {
		DatabaseAccessor langRetriever = new DatabaseAccessorObject();	
		String user = "student";
		String pass = "student";
		String URL = "jdbc:mysql://localhost:3306/sdvid";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String sql = "SELECT * FROM language WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, languageId);
		ResultSet fResult2 = stmt.executeQuery();
		String langName = null;
		if (fResult2.next()) {
			langName = fResult2.getString("name");
		}
		fResult2.close();
		conn.close();
		return langName;
	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		List<Film> filmsKeyword = new ArrayList<Film>();
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "SELECT * FROM film WHERE film.title LIKE ? OR film.description LIKE ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + keyword + "%");
		stmt.setString(2, "%" + keyword + "%");
		ResultSet kwResult = stmt.executeQuery();
		while (kwResult.next()) {
			Film kwFilm = new Film();
			kwFilm.setId(kwResult.getInt("id"));
			kwFilm.setTitle(kwResult.getString("title"));
			kwFilm.setDescription(kwResult.getString("description"));
			kwFilm.setReleaseYear(kwResult.getShort("release_year"));
			kwFilm.setLanguageId(kwResult.getInt("language_id"));
		kwFilm.setLangName(findLangNamebyId(kwResult.getInt("language_id")));
			kwFilm.setRentalDuration(kwResult.getInt("rental_duration"));
			kwFilm.setRentalRate(kwResult.getDouble("rental_rate"));
			kwFilm.setLength(kwResult.getInt("length"));
			kwFilm.setReplacementCost(kwResult.getDouble("replacement_cost"));
			kwFilm.setRating(kwResult.getString("rating"));
			kwFilm.setSpecialFeatures(kwResult.getString("special_features"));
			kwFilm.setCast(findActorsByFilmId(kwResult.getInt("id")));
			filmsKeyword.add(kwFilm);
		}
		kwResult.close();
		conn.close();
		return filmsKeyword;
	}

	public Actor findActorById(int actorId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Actor actor = null;
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		if (actorResult.next()) {
			actor = new Actor();
			actor.setId(actorResult.getInt("id"));
			actor.setFirstName(actorResult.getString("first_name"));
			actor.setLastName(actorResult.getString("last_name"));
			actor.setFilms(findFilmById(actorId));
		}
		actorResult.close();
		conn.close();
		return actor;
	}

	public List<Film> findFilmsByActorId(int actorId) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM film JOIN film_actor ON film.id = film_actor.film_id WHERE film_actor.actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				actorId = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				short releaseYear = rs.getShort("release_year");
				int langId = rs.getInt("language_id");
				int rentDur = rs.getInt("rental_duration");
				double rate = rs.getDouble("rental_rate");
				int length = rs.getInt("length");
				double repCost = rs.getDouble("replacement_cost");
				String rating = rs.getString("rating");
				String features = rs.getString("special_features");
				Film film = new Film(actorId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);
				films.add(film);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		List<Actor> cast = new ArrayList<>();
		String user = "student";
		String pass = "student";
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT actor.first_name, actor.last_name FROM actor, film_actor WHERE actor.id = film_actor.actor_id AND film_actor.film_id = ?";
			
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Actor actor = new Actor();
				actor.setFirstName(rs.getString("first_name"));
				actor.setLastName(rs.getString("last_name"));
				cast.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cast;
	}
}
// for required search string prepStatement.setString(1, "%" + parmNameUsedInYourMethod + "%");