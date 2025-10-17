package project.book;

import org.springframework.stereotype.Service;

@Service
public interface BookService {
	public void registerBook(int user,Book book);
	public Book[] showUserBook(int user);
	public void removeBook(int user,Book book);
	public BookHolder[] showBookFromFavor(String[] favorGenre ,String[] favorAuthors);
	public BookHolder[] searchExact(String title , String authors);
	public BookHolder[] searchApprox(String title , String authors);
	public String showProviderBookName(int user , String isbn);
	public void makeRequest(int user,int id , String ISBN);
	public BookRequest[] getSelfRequests(int self);
	public BookRequest[] getOthersRequests(int self);
	public void removeRequest(int user,int id, String ISBN);
	public void approveRequest(int user,int id, String ISBN);
}
