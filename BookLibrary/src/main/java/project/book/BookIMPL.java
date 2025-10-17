package project.book;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookIMPL implements BookService {
	@Autowired
	private DataSource dataSource;
	
	private static String removeWhiteSpace(String context) {
		String[] fields = context.split("\\s");
		StringBuilder str = new StringBuilder();
		for(int i = 0 ;i < fields.length ;i++)str.append(fields[i]);
		return str.toString();
	}
	
	public void registerBook(int user,Book book) {
		try {
			Connection con = dataSource.getConnection();//db sundesh
			Statement stmt = con.createStatement();//eisagwgh enhmerwsh sql
			if (book.getISBN() != null && book.getTitle() != null) {
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM book WHERE ISBN = \""+book.getISBN()+"\" AND UserIDprovider = "+user+" ;");
				rs.next();
				if(rs.getInt(1) == 0)stmt.executeUpdate("INSERT INTO book(ISBN,UserIDprovider,Title,genre,description,authors,TitleSearch,authorsSearch) VALUES"+
	            				"(\""+book.getISBN()+"\""+","+
	            				user+","+
	            				"\""+book.getTitle()+"\""+","+
	            				"\""+book.getGenre()+"\""+","+
	            				"\""+book.getDescription()+"\""+","+
	            				"\""+book.getAuthors()+"\""+","+
	            				"\""+removeWhiteSpace(book.getTitle().toLowerCase())+"\""+","+
	            				"\""+removeWhiteSpace(book.getAuthors().toLowerCase())+"\" "+
	            				");"
								);
				else stmt.executeUpdate("UPDATE book SET "+
        				"Title = \""+book.getTitle()+"\""+","+
        				"genre = \""+book.getGenre()+"\""+","+
        				"description = \""+book.getDescription()+"\""+","+
        				"authors = \""+book.getAuthors()+"\""+","+
        				"TitleSearch = \""+removeWhiteSpace(book.getTitle().toLowerCase())+"\""+","+
        				"authorsSearch = \""+removeWhiteSpace(book.getAuthors().toLowerCase())+"\" "+
        				"WHERE ISBN = \""+book.getISBN()+"\" AND UserIDprovider = "+user+" ;"
						);
	            stmt.close();
	            con.close();
			}
			else {
				stmt.close();
	            con.close();
			}
		} 
		catch (SQLException e) { e.printStackTrace();}
	}
	
	@Override
	public Book[] showUserBook(int user) {
		ArrayList<Book> booklist = new ArrayList<Book>();
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ISBN,Title,genre,description,authors FROM book WHERE UserIDprovider = "+user+" ;");
			while(rs.next())booklist.add(new Book(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5)));
			rs.close();
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
		Book[] result = new Book[booklist.size()];
		for (int i = 0 ; i < result.length ; i++)result[i] = booklist.get(i);
		return result;
	}
	
	public String showProviderBookName(int user , String isbn) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Title FROM book WHERE UserIDprovider = "+user+" AND ISBN = "+isbn+" ;");
			rs.next();
			String result = rs.getString(1);
			rs.close();
            stmt.close();
            con.close();
            return result;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void removeBook(int user,Book book) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
            stmt.executeUpdate(
            "DELETE FROM book WHERE UserIDprovider = "+user+
            " AND ISBN = \""+book.getISBN()+"\" ;"
            );
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
	}
	@Override
	public BookHolder[] showBookFromFavor(String[] favorGenre ,String[] favorAuthors ) {
		ArrayList<BookHolder> booklist = new ArrayList<BookHolder>();
		try {
			Connection con = dataSource.getConnection();
			StringBuilder str = new StringBuilder();
			str.append("SELECT id1.ISBN , id1.Title , id1.genre , id1.description , id1.authors"+ 
			" , id2.FullName , id2.id" + " FROM book AS id1 , profile AS id2 WHERE (");
			if (favorGenre.length > 0) {
				int i;
				int count = favorGenre.length - 1;
				for (i = 0; i < count ;i++)str.append("FIND_IN_SET(\""+favorGenre[i]+"\",id1.genre) > 0 OR ");
				str.append("FIND_IN_SET(\""+favorGenre[i]+"\",id1.genre) > 0 ");
			}
			else str.append("FALSE ");
			str.append("OR ");
			if (favorAuthors.length > 0) {
				int i;
				int count = favorAuthors.length - 1;
				for (i = 0; i < count ;i++)str.append("FIND_IN_SET(\""+removeWhiteSpace(favorAuthors[i].toLowerCase())+"\",id1.authorsSearch) > 0 OR ");
				str.append("FIND_IN_SET(\""+removeWhiteSpace(favorAuthors[i].toLowerCase())+"\",id1.authorsSearch) > 0 ) ");
			}
			else str.append("FALSE ) ");
			str.append("AND id1.UserIDprovider = id2.id ;");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(str.toString() );
			while(rs.next())booklist.add(new BookHolder(
					new Book(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5))
					, rs.getString(6) , rs.getInt(7))
			);
			rs.close();
			stmt.close();
			con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
		BookHolder[] result = new BookHolder[booklist.size()];
		for (int i = 0 ; i < result.length ; i++)result[i] = booklist.get(i);
		return result;
		
	}
	@Override
	public BookHolder[] searchExact(String title,String authors) {
		ArrayList<BookHolder> booklist = new ArrayList<BookHolder>();
		StringBuilder find = new StringBuilder();
		if (!title.isBlank())find.append("id1.TitleSearch = \""+removeWhiteSpace(title.toLowerCase())+"\" ");
		else find.append("FALSE ");
		find.append("OR ");
		if (!authors.isBlank())find.append("FIND_IN_SET(\""+removeWhiteSpace(authors.toLowerCase())+"\",id1.authorsSearch) > 0");
		else find.append("FALSE");
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id1.ISBN , id1.Title , id1.genre , id1.description , id1.authors"+
			" , id2.FullName , id2.id" + " FROM book AS id1 , profile AS id2"+
			" WHERE ("+find.toString()+") AND id1.UserIDprovider = id2.id ;");
			while(rs.next())booklist.add(new BookHolder(
					new Book(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5))
					, rs.getString(6) , rs.getInt(7))
			);
			rs.close();
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
		BookHolder[] result = new BookHolder[booklist.size()];
		for (int i = 0 ; i < result.length ; i++)result[i] = booklist.get(i);
		return result;
		
	}
	@Override
	public BookHolder[] searchApprox(String title,String authors) {
		ArrayList<BookHolder> booklist = new ArrayList<BookHolder>();
		StringBuilder find = new StringBuilder();
		if (!title.isBlank())find.append("id1.TitleSearch LIKE \"%"+removeWhiteSpace(title.toLowerCase())+"%\" ");
		else find.append("FALSE ");
		find.append("OR ");
		if (!authors.isBlank())find.append("id1.authorsSearch LIKE \"%"+removeWhiteSpace(authors.toLowerCase())+"%\"");
		else find.append("FALSE");
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id1.ISBN , id1.Title , id1.genre , id1.description , id1.authors"+
					" , id2.FullName , id2.id" + " FROM book AS id1 , profile AS id2 "+
					" WHERE ("+find.toString()+") AND id1.UserIDprovider = id2.id ;");
			while(rs.next())booklist.add(new BookHolder(
					new Book(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5))
					, rs.getString(6) , rs.getInt(7))
			);
			rs.close();
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
		BookHolder[] result = new BookHolder[booklist.size()];
		for (int i = 0 ; i < result.length ; i++)result[i] = booklist.get(i);
		return result;
		
	}
	
	@Override
	public void makeRequest(int user,int id, String ISBN) {
		if (user == id)return;
		else {
			try {
				Connection con = dataSource.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT UserIDrequest FROM book_request WHERE BookISBN = \""+ISBN+"\" AND UserIDprovider = "+id+" ;");
				while(rs.next()) if(rs.getInt(1) == user) {rs.close();stmt.close();con.close();return;}
				stmt.executeUpdate(
				"INSERT INTO book_request(UserIDprovider,BookISBN,UserIDrequest) "+
				"VALUES("+id+","+"\""+ISBN+"\""+","+user+");"
				);
	            stmt.close();
	            con.close();
			} 
			catch (SQLException e) { e.printStackTrace();}
		}
	}
	
	@Override
	public BookRequest[] getSelfRequests(int self) {
		ArrayList<BookRequest> booklist = new ArrayList<BookRequest>();
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM book_request WHERE UserIDrequest = "+self+";");
			while(rs.next())booklist.add(new BookRequest(rs.getString(2), rs.getInt(1), rs.getInt(3), rs.getBoolean(4)));
			rs.close();
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
		BookRequest[] result = new BookRequest[booklist.size()];
		for (int i = 0 ; i < result.length ; i++)result[i] = booklist.get(i);
		return result;
		
	}
	
	@Override
	public BookRequest[] getOthersRequests(int self) {
		ArrayList<BookRequest> booklist = new ArrayList<BookRequest>();
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM book_request WHERE UserIDprovider = "+self+" ;");
			while(rs.next())booklist.add(new BookRequest(rs.getString(2), rs.getInt(1), rs.getInt(3), rs.getBoolean(4)));
			rs.close();
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
		BookRequest[] result = new BookRequest[booklist.size()];
		for (int i = 0 ; i < result.length ; i++)result[i] = booklist.get(i);
		return result;
		
	}
	
	@Override
	public void removeRequest(int user,int id, String ISBN) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate(
					"DELETE FROM book_request"+
					" WHERE UserIDProvider = "+id+
					" AND BookISBN = \""+ISBN+"\""+
					" AND UserIDrequest= "+user+" ;"
					);
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
	}
	@Override
	public void approveRequest(int user,int id, String ISBN) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate(
			"UPDATE book_request SET isApproved = 1"+
			" WHERE UserIDProvider = "+user+
			" AND BookISBN = \""+ISBN+"\""+
			" AND UserIDrequest = "+id+" ;"
			);
			stmt.executeUpdate(
			"DELETE FROM book_request"+
			" WHERE UserIDProvider = "+user+
			" AND BookISBN = \""+ISBN+"\""+
			" AND isApproved = 0 ;"
			);
            stmt.close();
            con.close();
		} 
		catch (SQLException e) { e.printStackTrace();}
	}
	
}
