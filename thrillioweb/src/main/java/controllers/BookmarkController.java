package controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constants.KidFriendlyStatus;
import entities.Bookmark;
import entities.User;
import managers.BookmarkManager;
import managers.UserManager;

@WebServlet(urlPatterns={"/auth/login/bookmark","/auth/login/bookmark/mybooks","/auth/login/bookmark/save","/auth/login/bookmark/unsave"})
public class BookmarkController extends HttpServlet{
    
	public BookmarkController() {
	
	}
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher=null;
		if(request.getSession().getAttribute("userId")!=null) {
			long userId=(long)request.getSession().getAttribute("userId");
			if(request.getServletPath().contains("unsave")) {
				dispatcher=request.getRequestDispatcher("/mybooks.jsp");
				String bid=request.getParameter("bid");
				User user=UserManager.getInstance().getUser(userId);
				Bookmark book=BookmarkManager.getinstance().getBook(Long.parseLong(bid));
				BookmarkManager.getinstance().deleteUserBookmark(user, book);
				Collection<Bookmark>list=BookmarkManager.getinstance().getBooks(true,userId);
				request.setAttribute("books",list);
			}
			
			else if(request.getServletPath().contains("save")){
				dispatcher=request.getRequestDispatcher("/mybooks.jsp");
				//dispatcher.forward(request, response);
				String bid=request.getParameter("bid");
				User user=UserManager.getInstance().getUser(userId);
				Bookmark book=BookmarkManager.getinstance().getBook(Long.parseLong(bid));
				BookmarkManager.getinstance().saveUserBookmark(user, book);
				Collection<Bookmark>list=BookmarkManager.getinstance().getBooks(true,userId);
				request.setAttribute("books",list);
			
			}
			
			else if(request.getServletPath().contains("mybooks")) {
				dispatcher=request.getRequestDispatcher("/mybooks.jsp");
				Collection<Bookmark>list=BookmarkManager.getinstance().getBooks(true,userId);
				request.setAttribute("books",list);
	
			}
			else {
				dispatcher=request.getRequestDispatcher("/browse.jsp");	
			Collection<Bookmark>list=BookmarkManager.getinstance().getBooks(false,userId);
			request.setAttribute("books",list);
			}
		}
		else {
			dispatcher=request.getRequestDispatcher("/login.jsp");	
		}
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public void saveUserBookmark(User user, Bookmark bookmark) {
		BookmarkManager.getinstance().saveUserBookmark(user, bookmark);

	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
		BookmarkManager.getinstance().setKidFriendlyStatus(user,kidFriendlyStatus, bookmark);
	}

	public void share(User user, Bookmark bookmark) {
		BookmarkManager.getinstance().share(user,bookmark);
		
	}
}
