package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;


import constants.BookGenre;
import constants.MovieGenre;
import entities.Book;
import entities.Bookmark;
import entities.Movie;
import entities.UserBookmark;
import entities.WebLink;
import managers.BookmarkManager;
public class BookmarkDao {

public void saveUserBookmark(UserBookmark userBookmark) {
	//DataStore.add(userBookmark);
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {
		    if(userBookmark.getBookmark() instanceof Book) {
		    	saveUserBook(userBookmark,stmt);
		    }
		    else if(userBookmark.getBookmark() instanceof Movie) {
		        saveUserMovie(userBookmark,stmt);
		    }	
		    else {
		    	saveUserWebLink(userBookmark,stmt);}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}

private void saveUserBook(UserBookmark userBookmark, Statement stmt) throws SQLException {
	String query="insert into User_Book(user_id,book_id) values("+
                  userBookmark.getUser().getId()+ ", "+userBookmark.getBookmark().getId()+")";
	
	stmt.executeUpdate(query);
}

private void saveUserMovie(UserBookmark userBookmark, Statement stmt) throws SQLException {
	String query="insert into User_Movie(user_id,movie_id) values("+
            userBookmark.getUser().getId()+ ", "+userBookmark.getBookmark().getId()+")";

stmt.executeUpdate(query);
	
}

private void saveUserWebLink(UserBookmark userBookmark, Statement stmt) throws SQLException {
	String query="insert into User_WebLink(user_id,weblink_id) values("+
            userBookmark.getUser().getId()+ ", "+userBookmark.getBookmark().getId()+")";

stmt.executeUpdate(query);	
}

public void deleteUserBookmark(UserBookmark userBookmark) {
	//DataStore.add(userBookmark);
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {
		    if(userBookmark.getBookmark() instanceof Book) {
		    	deleteUserBook(userBookmark,stmt);
		    }
		    else if(userBookmark.getBookmark() instanceof Movie) {
		        deleteUserMovie(userBookmark,stmt);
		    }	
		    else {
		    	deleteUserWebLink(userBookmark,stmt);}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}


private void deleteUserWebLink(UserBookmark userBookmark, Statement stmt) throws SQLException {
	String query="delete from User_WebLink where user_id="+
            userBookmark.getUser().getId()+ "and weblink_id="+userBookmark.getBookmark().getId();

stmt.executeUpdate(query);
	
}

private void deleteUserMovie(UserBookmark userBookmark, Statement stmt) throws SQLException {
	String query="delete from user_movie where user_id="+userBookmark.getUser().getId()+ " and movie_id="+userBookmark.getBookmark().getId();
stmt.executeUpdate(query);
	
}

private void deleteUserBook(UserBookmark userBookmark, Statement stmt) throws SQLException {
	String query="delete from user_book where user_id="+userBookmark.getUser().getId()+ " and book_id="+userBookmark.getBookmark().getId();

stmt.executeUpdate(query);
}



public void updateKidFriendlyStatus(Bookmark bookmark) {
	int kidFriendlyStatus=bookmark.getKidFriendlyStatus().ordinal();
	long userId=bookmark.getKidFriendlyMarkedBy().getId();
	String tableToUpdate="Book";
	if(bookmark instanceof Movie) {
		tableToUpdate="Movie";
	}
	else if(bookmark instanceof WebLink) {
		tableToUpdate="WebLink";
	}
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {	
	 String query="update "+tableToUpdate+" set kid_friendly_status = "+kidFriendlyStatus+", kid_friendly_marked_by= "+userId+" where id="+bookmark.getId()+"";
     System.out.println(query);  
	 stmt.executeUpdate(query);
	}
	catch (SQLException e) {
		e.printStackTrace();
	}
}

public void sharedByInfo(Bookmark bookmark) {
	long userId=bookmark.getSharedBy().getId();
	String tableToUpdate="Book";
	if(bookmark instanceof WebLink) {
		tableToUpdate="WebLink";
	}
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {	
	 String query="update "+tableToUpdate+" set shared_by= "+userId+" where id="+bookmark.getId()+"";
     System.out.println(query);  
	 stmt.executeUpdate(query);
	}
	catch (SQLException e) {
		e.printStackTrace();
	}
	
}

public Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
	Collection<Bookmark> result = new ArrayList<>();
	
	try {
		Class.forName("com.mysql.jdbc.Driver");
		//new com.mysql.jdbc.Driver(); 
		            // OR
		//System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
	
	                // OR java.sql.DriverManager
	    //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {			
		
		String query = "";
		if (!isBookmarked) {
			query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, " +
								"amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and " + 
								"b.id NOT IN (select ub.book_id from User u, User_Book ub where u.id = " + userId +
								" and u.id = ub.user_id) group by b.id";				
		} else {
			query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, " +
					"amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and " + 
					"b.id IN (select ub.book_id from User u, User_Book ub where u.id = " + userId +
					" and u.id = ub.user_id) group by b.id";
		}
		
		ResultSet rs = stmt.executeQuery(query);				
		
    	while (rs.next()) {
    		long id = rs.getLong("id");
			String title = rs.getString("title");
			String imageUrl = rs.getString("image_url");
			int publicationYear = rs.getInt("publication_year");
			//String publisher = rs.getString("name");		
			String[] authors = rs.getString("authors").split(",");			
			int genre_id = rs.getInt("book_genre_id");
			BookGenre genre = BookGenre.values()[genre_id];
			double amazonRating = rs.getDouble("amazon_rating");
			
			System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
    		
    		Bookmark bookmark = BookmarkManager.getinstance().createBook(id, title, imageUrl, publicationYear, null, authors, genre, amazonRating/*, values[7]*/);
    		result.add(bookmark); 
    	}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return result;
}

public Bookmark getBook(long bookId) {
	Book book = null;
	
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {
		String query = "Select b.id, title, image_url, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date"
				+ " from Book b, Publisher p, Author a, Book_Author ba "
				+ "where b.id = " + bookId + " and b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";
    	ResultSet rs = stmt.executeQuery(query);
		
    	while (rs.next()) {
    		long id = rs.getLong("id");
			String title = rs.getString("title");
			String imageUrl = rs.getString("image_url");
			int publicationYear = rs.getInt("publication_year");
			String publisher = rs.getString("name");		
			String[] authors = rs.getString("authors").split(",");			
			int genre_id = rs.getInt("book_genre_id");
			BookGenre genre = BookGenre.values()[genre_id];
			double amazonRating = rs.getDouble("amazon_rating");
			
			System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
    		
    		book = BookmarkManager.getinstance().createBook(id, title, imageUrl, publicationYear, publisher, authors, genre, amazonRating/*, values[7]*/);
    	}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return book;
}

public Bookmark getMovie(long mid){
	Movie movie=null;
	try {
		Class.forName("com.mysql.jdbc.Driver");

	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {	
		String query = "Select m.id, title,image_url,release_year, GROUP_CONCAT(DISTINCT a.name SEPARATOR ',') AS actors, GROUP_CONCAT(DISTINCT d.name SEPARATOR ',') AS directors, movie_genre_id, imdb_rating"
				+ " from Movie m, Actor a, Movie_Actor ma, Director d, Movie_Director md "
				+ "where m.id="+mid+" and m.id = ma.movie_id and ma.actor_id = a.id and "
				      + "m.id = md.movie_id and md.director_id = d.id group by m.id";
		
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			long id=rs.getLong("id");
			String title=rs.getString("title");
			String imageUrl=rs.getString("image_url");
			String[]directors=rs.getString("directors").split(",");
			String [] actors=rs.getString("actors").split(",");
			int release_year=rs.getInt("release_year");
			MovieGenre mg=MovieGenre.values()[rs.getInt("movie_genre_id")];
			double imdb_rating=rs.getDouble("imdb_rating");
			movie=BookmarkManager.getinstance().createMovie(id,title,imageUrl,release_year,actors,directors,mg,imdb_rating);
		}
		
}
	catch (SQLException e) {
		e.printStackTrace();
	}
	return movie;
}

public Collection<Bookmark> getMovies(boolean isBookmarked, long userId) {
	Collection<Bookmark> result = new ArrayList<>();
	
	try {
		Class.forName("com.mysql.jdbc.Driver");
		//new com.mysql.jdbc.Driver(); 
		            // OR
		//System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
	
	                // OR java.sql.DriverManager
	    //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
			Statement stmt = conn.createStatement();) {			
		
		String query = "";
		if (!isBookmarked) {
			query="select m.id,title,image_url,GROUP_CONCAT(d.name SEPARATOR ',') as directors,GROUP_CONCAT(a.name SEPARATOR ',') as actors,"+
			          "release_year,movie_genre_id,imdb_rating from movie m,movie_actor ma,movie_director md,actor a,director d "+
				      "where m.id=ma.movie_id and m.id=md.movie_id and ma.actor_id=a.id and md.director_id=d.id and m.id not in (Select movie_id from user u,user_movie um where u.id=5 and u.id=um.user_id) group by m.id";
		}
		else {
			query="select m.id,title,image_url,GROUP_CONCAT(d.name SEPARATOR ',') as directors,GROUP_CONCAT(a.name SEPARATOR ',') as actors,"+
			          "release_year,movie_genre_id,imdb_rating from movie m,movie_actor ma,movie_director md,actor a,director d "+
				      "where m.id=ma.movie_id and m.id=md.movie_id and ma.actor_id=a.id and md.director_id=d.id and m.id in (Select movie_id from user u,user_movie um where u.id=5 and u.id=um.user_id) group by m.id";
		}
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			long id=rs.getLong("id");
			String title=rs.getString("title");
			String imageUrl=rs.getString("image_url");
			String[]directors=rs.getString("directors").split(",");
			String [] actors=rs.getString("actors").split(",");
			int release_year=rs.getInt("release_year");
			MovieGenre mg=MovieGenre.values()[rs.getInt("movie_genre_id")];
			double imdb_rating=rs.getDouble("imdb_rating");
			Movie movie=BookmarkManager.getinstance().createMovie(id,title,imageUrl,release_year,actors,directors,mg,imdb_rating);
			result.add(movie);
		}

}
	catch (SQLException e) {
		e.printStackTrace();
	}
	
	return result;
	
}
}