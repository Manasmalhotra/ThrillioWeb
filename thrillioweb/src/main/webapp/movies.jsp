<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>thrill.io</title>
</head>
<body style="font-family: Arial; font-size: 20px;">
	<div
		style="height: 65px; align: center; background: #DB5227; font-family: Arial; color: white;"">
		<br>
		<b> <a href=""
			style="font-family: garamond; font-size: 34px; margin: 0px 0px 0px 10px; color: white; text-decoration: none;">thrill.io</a></b>
		<div
			style="height: 25px; background: #DB5227; font-family: Arial; color: white;">
			<b> <a
				href="<%=request.getContextPath()%>/auth/login/bookmark/mybooks"
				style="font-size: 16px; color: white; text-decoration: none; margin-left: 900px">My
					Books</a> <a
				href="<%=request.getContextPath()%>/auth/login/bookmark/mymovies"
				style="font-size: 16px; color: white; text-decoration: none;">My
					Movies</a> <a
				href="<%=request.getContextPath()%>/auth/login/bookmark/books"
				style="font-size: 16px; color: white; text-decoration: none;">Books</a>
				<a href="<%=request.getContextPath()%>/auth/login/bookmark/movies"
				style="font-size: 16px; color: white; text-decoration: none;">Movies</a>
				<a href="<%=request.getContextPath()%>/auth/logout"
				style="font-size: 16px; color: white; text-decoration: none;">Logout</a>
			</b>

		</div>
	</div>
	<br>
	<br>

	<table>
		<c:forEach var="movie" items="${movies}">
			<tr>
				<td><img src="${movie.imageUrl}" width="175" height="200">
				</td>

				<td style="color: gray;">Title: <span style="color: #B13100;">${movie.title}</span>
					<br>
				<br> Release Year: <span style="color: #B13100;">${movie.releaseYear}</span>
					<br>
				<br> IMDB Rating: <span style="color: #B13100;">${movie.imdbRating}</span>
					<br>
				<br> Genre: <span style="color: #B13100;">${movie.genre.getName()}</span>
					<br>
				<br> <a
					href="<%=request.getContextPath()%>/auth/login/bookmark/movies/save?mid=${movie.id}"
					style="font-size: 18px; color: #0058A6; font-weight: bold; text-decoration: none">Save</a>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>

		</c:forEach>

	</table>

</body>
</html>