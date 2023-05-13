package managers;

import java.sql.SQLException;
import java.util.List;

import constants.Gender;
import constants.UserType;
import dao.UserDao;
import entities.User;
import util.StringUtil;

public class UserManager {
	private static UserManager instance = new UserManager();
    private static UserDao dao=new UserDao();
	private UserManager() {
	}

	public static UserManager getInstance() {
		return instance;
	}

	public User createUser(long id, String email, String password, String firstName, String lastName, Gender gender,
			UserType userType) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setGender(gender);
		user.setUserType(userType);
		
		return user;
	}
	public void addUser(User u) throws SQLException {
		dao.addUser(u);
	}
	public List<User> getUsers() throws SQLException {
			return dao.getUsers();
	}

	public User getUser(long userId) {
		return dao.getUser(userId);
		
	}

	public long authenticate(String email, String password) {
		return dao.authenticate(email,StringUtil.encodePassword(password));
		
	}

	public boolean userPresent(String email) {
		// TODO Auto-generated method stub
		return dao.userPresent(email);
	}
}
