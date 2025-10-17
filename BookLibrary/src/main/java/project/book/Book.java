package project.book;

import project.common.Genres;

public class Book{
	private String ISBN;
	private String title;
	private Genres genre;
	private String description;
	private String authors;
	
	public Book() {
		this.ISBN = null;
		this.title = null;
		this.genre = new Genres();
		this.description = null;
		this.authors = null;
	}
	
	public void setISBN(String ISBN) {if(ISBN.matches("^[0-9]{10}$"))this.ISBN = ISBN;}
	public void setTitle(String title){if(title.length() <= 60)this.title = title;}
	public void setGenre(String genre) {this.genre = new Genres(genre);}
	public void setDescription(String description) {this.description = description;}
	public void setAuthors(String authors) {this.authors = authors;}
	
	
	public Book(String ISBN,String title,String genre,String description,String authors) {
		this.ISBN = ISBN;
		this.title = title;
		this.genre = new Genres(genre);
		this.description = description;
		this.authors = authors;
	}
	
	public String getAuthors() {return authors;}
	public String getISBN() {return ISBN;}
	public String getGenre() {return genre.toString();}
	public String[] getGenreArray() {return genre.toArray();}
	public String getTitle() {return title;}
	public String getDescription() {return description;}
}