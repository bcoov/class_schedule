<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>

	<head>
		<title>Course Description</title>
		<link rel="stylesheet" type = "text/css" href = "${pageContext.servletContext.contextPath}/CSS/Layout.css">
	
	</head>
	
	<body>
	
	<div id = "container">
	<div id = "header">
		<h1>YCP Schedule Planner Prototype2</h1>	
	
	</div>
	
	<div>
		<div id = "nav">
		<p>Username: <br> ${user.username}</p>
		<p>Major: <br> ${maj.name}</p>
		
		</div>
		
		<div id = "section">
			<h2>${selectedCourse.name}</h2>
			
			<h3>${selectedCourse.semester}</h3>
			
			<p>${descr.descript}</p>
			
			<form action="${pageContext.servletContext.contextPath}/courseDescription" method="post">
				<input type = "submit" name = "completed" value="Completed"></input>
				<br>
				<input type = "submit" name = "todo" value = "TODO"></input>
			</form>
		
		
		</div>
	
	</div>
	
	
	
	
	
	
	
	<div id = "footer">
		<h6> Please click the Completed button if you have finished the course or TODO to schedule one</h6>
	
	</div>
	
	</div>
	
	
	
	</body>






</html>