package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constants.Gender;
import constants.UserType;
import entities.User;
import managers.UserManager;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Servlet implementation class AuthController
 */
@WebServlet(urlPatterns={"/auth/register","/auth/login","/auth/logout"})
public class AuthController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getServletPath().contains("register")) {
			long id=1000;
			String email=request.getParameter("email");
			if(UserManager.getInstance().userPresent(email)==true) {
				PrintWriter out = response.getWriter(); 
				out.println("<script type=\"text/javascript\">"); 
				out.println("alert('Email already in use! Please login to continue');");
				out.println("location='/register.jsp';"); 
				out.println("</script>"); 
				request.getSession().invalidate();
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
			else {
			String password=request.getParameter("password");
			String firstName=request.getParameter("firstname");
			String lastName=request.getParameter("lastname");
			int gender_id=Integer.parseInt(request.getParameter("gender"));
			Gender gender = Gender.values()[gender_id];
			UserType userType=UserType.values()[0];
			User u=UserManager.getInstance().createUser(id,email,password,firstName,lastName, gender, userType);
			try {
				UserManager.getInstance().addUser(u);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long userId=UserManager.getInstance().authenticate(email,password);
			HttpSession session=request.getSession();
			session.setAttribute("userId", userId);
			request.getRequestDispatcher("login/bookmark/mybooks").forward(request, response);
			}
		}
		else if(!request.getServletPath().contains("logout")) {
			String email=request.getParameter("email");
			String password=request.getParameter("password");
			
			long userId=UserManager.getInstance().authenticate(email,password);
			if(userId!=-1) {
				HttpSession session=request.getSession();
				session.setAttribute("userId", userId);
				request.getRequestDispatcher("login/bookmark/mybooks").forward(request, response);
			}
			else {
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}
		else {
			request.getSession().invalidate();
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
