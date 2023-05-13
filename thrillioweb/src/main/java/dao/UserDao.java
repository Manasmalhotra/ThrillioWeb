package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;

import constants.Gender;
import constants.UserType;
import entities.User;
import managers.UserManager;
import util.StringUtil;
public class UserDao {
	public List<User> getUsers() throws SQLException {
		List<User> users = new ArrayList<> ();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
				Statement stmt = conn.createStatement();) {	

		String query="Select b.id,b.email,b.password,b.first_name,b.last_name,b.gender_id,b.user_type_id,b.created_date from User b";
		ResultSet rs=stmt.executeQuery(query);
		
    	while(rs.next()) {
    		long id = rs.getLong("id");
    		String email=rs.getString("email");
    		String password=rs.getString("password");
    		String firstName=rs.getString("first_name");
    		String lastName=rs.getString("last_name");
    		int gender_id=rs.getInt("gender_id");
    		Gender gender = Gender.values()[gender_id];
    		int user_id=rs.getInt("user_type_id");
    		UserType userType=UserType.values()[user_id];
    		users.add(UserManager.getInstance().createUser(id,email,password,firstName,lastName, gender, userType));
    	}
	   }
		return users;
	}

	
	
	public User getUser(long userId) {
		User user = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
				Statement stmt = conn.createStatement();) {	
			String query = "Select * from User where id = " + userId;
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				long id = rs.getLong("id");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				int gender_id = rs.getInt("gender_id");
				Gender gender = Gender.values()[gender_id];
				int user_type_id = rs.getInt("user_type_id");
				UserType userType = UserType.values()[user_type_id];
				
				user = UserManager.getInstance().createUser(id, email, password, firstName, lastName, gender, userType);
	    	}			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return user;
		
	}
	public String cover(String property) {
		return "'"+property+"'";
	}
	public void addUser(User u) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
				Statement stmt = conn.createStatement();) {	
			String query="INSERT INTO User (email, password, first_name, \r\n"
					+ "last_name, gender_id, user_type_id, created_date) \r\n"
					+ "VALUES ("+cover(u.getEmail())+","+cover(StringUtil.encodePassword(u.getPassword()))+","+cover(u.getFirstName())+","+cover(u.getLastName())+","+u.getGender().getValue()+",0"+","+"NOW())";
			stmt.executeUpdate(query);
		}
	}
	public long authenticate(String email, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
				Statement stmt = conn.createStatement();) {	
			String query = "Select id from User where email = '" + email + "' and password = '" + password + "'";
			System.out.println("query: " + query);
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				return rs.getLong("id");				
	    	}			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return -1;
	}



	public boolean userPresent(String email) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root", "Manas@65");
				Statement stmt = conn.createStatement();) {	
		      String query="Select id from User where email="+"'"+email+"'";
		      ResultSet rs=stmt.executeQuery(query);
		      if(rs.next()) {
		    	  return true;
		      }
		      return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	
	}

}
