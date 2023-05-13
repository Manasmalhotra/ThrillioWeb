package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Bookmark;
import entities.User;
import managers.BookmarkManager;
import managers.UserManager;

/**
 * Servlet implementation class MovieController
 */
@WebServlet(urlPatterns={"/auth/login/bookmark/movies","/auth/login/bookmark/mymovies","/auth/login/bookmark/movies/save","/auth/login/bookmark/movies/unsave"})
public class MovieController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MovieController() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher=null;
		if(request.getSession().getAttribute("userId")!=null) {
			long userId=(long)request.getSession().getAttribute("userId");
			if(request.getServletPath().contains("unsave")) {
				dispatcher=request.getRequestDispatcher("/mymovies.jsp");
				String mid=request.getParameter("mid");
				User user=UserManager.getInstance().getUser(userId);
				Bookmark movie=null;
			    movie = BookmarkManager.getinstance().getMovie(Long.parseLong(mid));
				
				BookmarkManager.getinstance().deleteUserBookmark(user, movie);
				Collection<Bookmark>list=BookmarkManager.getinstance().getMovies(true,userId);
				request.setAttribute("movies",list);
			}
			
			else if(request.getServletPath().contains("save")){
				dispatcher=request.getRequestDispatcher("/mymovies.jsp");
				//dispatcher.forward(request, response);
				String mid=request.getParameter("mid");
				User user=UserManager.getInstance().getUser(userId);
				Bookmark movie=BookmarkManager.getinstance().getMovie(Long.parseLong(mid));
				BookmarkManager.getinstance().saveUserBookmark(user, movie);
				Collection<Bookmark>list=BookmarkManager.getinstance().getMovies(true,userId);
				request.setAttribute("movies",list);
			
			}
			
			else if(request.getServletPath().contains("mymovies")) {
				dispatcher=request.getRequestDispatcher("/mymovies.jsp");
				Collection<Bookmark>list=BookmarkManager.getinstance().getMovies(true,userId);
				request.setAttribute("movies",list);
	
			}
			else {
				dispatcher=request.getRequestDispatcher("/movies.jsp");	
			Collection<Bookmark>list=BookmarkManager.getinstance().getMovies(false,userId);
			request.setAttribute("movies",list);
			}
		}
		else {
			dispatcher=request.getRequestDispatcher("/login.jsp");	
		}
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
