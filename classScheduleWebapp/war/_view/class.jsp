<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>

	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<title>Major Course List</title>
		<link rel="stylesheet" type = "text/css" href = "${pageContext.servletContext.contextPath}/CSS/Layout.css">
	
	</head>
	
	<body>
	
	<div id = "container">
	<div id = "header">
		<h1>YCP Schedule Planner Prototype2</h1>
		<h2>${maj.name} Course List</h2>	
	
	</div>
	
	<div>
		<div id = "nav">
		<p>Username: <br> ${user.username}</p>
		<p>Major: <br> ${maj.name}</p>
		
		</div>
		
		<%-- If a major exists, displays the courses
			Currently displays all courses, instead of major specific --%>
		<div id = "section">
			<form action="${pageContext.servletContext.contextPath}/class" method="post">
			<ul>
			
			<c:choose>
				<c:when test="${maj.name != null }">
					
					<c:forEach var="courselist" items="${allCourses}">

						<%-- <input type="radio" name="coursetitle" value="${courselist.name}" checked>${courselist.name}
						<br> --%>
						<li>
							<input type ="radio" name ="courseItem" value ="${courselist.name}">${courselist.name}
							<br>
						</li>
						
					</c:forEach>
					
				</c:when>
			</c:choose>
			
			</ul>
			<input type="submit" name="submit" value="Select Course"></input>
			
			</form>
			
			<h3>Completed courses</h3>
			
			<ul>
			<c:forEach var="compcourse" items = "${doneList}">
				<li>
					<p>${compcourse.name}</p>
				</li>				
			
			</c:forEach>
			</ul>
			
			<h3>Todo list</h3>
			
			<ul>
			<c:forEach var="todocourse" items = "${todoList}">
				<li>
					<p>${todocourse.name}</p>
				</li>				
			
			</c:forEach>
			</ul>
			
			<c:if test = "${not empty fall14list}">
				<h3>Fall 2014</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall15list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty spring15list}">
				<h3>Spring 2015</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${spring15list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty fall15list}">
				<h3>Fall 2015</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall15list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty spring16list}">
				<h3>Spring 2016</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${spring16list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty fall16list}">
				<h3>Fall 2016</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall16list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty spring17list}">
				<h3>Spring 2017</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${spring17list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty fall17list}">
				<h3>Fall 2017</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall17list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty spring18list}">
				<h3>Spring 2018</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${spring18list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty fall18list}">
				<h3>Fall 2018</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall18list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty spring19list}">
				<h3>Spring 2019</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${spring19list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty fall2019list}">
				<h3>Fall 2019</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall19list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty spring20list}">
				<h3>Spring 2020</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${spring20list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
			
			<c:if test = "${not empty fall20list}">
				<h3>Fall 2020</h3>
				<ul>
					<c:forEach var="coursedisplay" items = "${fall20list}">
						<li>
							<p>${coursedisplay.name}</p>
						</li>
					</c:forEach>				
				
				</ul>
			</c:if>
		</div>
	
	</div>
	
	
	
	<div id = "section">
		<a href="./major">
			<button>Click to add a new major</button>
		</a>
	
	</div>
	
	<div id = "footer">
		<h6> I'm writing code</h6>
	
	</div>
	
	</div>
	
	
	
	</body>

	<script>
		var navhieght = $("#nav").height();
		var sechieght = $("#section").height();
		
		if(sechieght > navhieght)
		{
			$("#nav").height($("#section").height());
		}
	
	</script>




</html>
