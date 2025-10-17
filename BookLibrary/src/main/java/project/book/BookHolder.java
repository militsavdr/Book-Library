package project.book;


public class BookHolder {
	private String ISBN;
	private String title;
	private String genre;
	private String description;
	private String authors;
	private String name;
	private int id;
	
	public BookHolder(Book myBook,String myName , int id) {
		this.ISBN = myBook.getISBN();
		this.title = myBook.getTitle();
		this.genre = myBook.getGenre();
		this.description = myBook.getDescription();
		this.authors = myBook.getAuthors();
		this.name = myName;
		this.id = id;
	}
	
	public String getISBN() {return ISBN;}
	public String getTitle() {return title;}
	public String getGenre() {return genre;}
	public String getDescription() {return description;}
	public String getAuthors() {return authors;}

	public String getName() {return name;}
	public int getId() {return id;}
}
