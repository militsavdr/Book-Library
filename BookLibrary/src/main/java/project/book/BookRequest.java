package project.book;

public class BookRequest {
	private String ISBN;
	private int Provider;
	private int Requester;
	private boolean isApproved;
	
	public BookRequest(String isbn , int Provider , int Requester ,boolean isApproved) {
		this.ISBN = isbn;
		this.Provider = Provider;
		this.Requester = Requester;
		this.isApproved = isApproved;
	}
	public String getISBN() {return ISBN;}
	public int getProvider() {return Provider;}
	public int getRequester() {return Requester;}
	public boolean isApproved() {return isApproved;}
}
