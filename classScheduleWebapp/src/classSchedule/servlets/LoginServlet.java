package classSchedule.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classSchedule.LoginController;
import classSchedule.MajorController;
import classSchedule.model.Course;
import classSchedule.model.Major;
import classSchedule.model.User;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		
		req.getSession().invalidate();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException ,IOException {
		String username = req.getParameter("username");			//get the username from the user who enters it
		String password = req.getParameter("password");			//get the password from the user who enters it

		String error = "";


		if (username == null || password == null) {
			error = "Please enter a username and password.";
		} else {
			
			//User before = (User) req.getSession().getAttribute("user");
			LoginController controller = new LoginController();		//make a new controller for each servlet
			User user = controller.findUser(username, password);	//find the user from the input
			
			List<Course> todo = (List<Course>) req.getSession().getAttribute("todoList");
			if(todo!=null)
			{
				if(todo.size()>0)
				{
					for(int i=0; i<todo.size(); i++)
					{
						todo.remove(i);
					}
					req.getSession().setAttribute("todoList", todo);
				}
			}
			List<Course> done = (List<Course>) req.getSession().getAttribute("doneList");
			if(done!=null)
			{
				if(done.size()>0)
				{
					for(int i=0; i<done.size(); i++)
					{
						done.remove(i);
					}
					req.getSession().setAttribute("doneList", done);
				}
			}
			
			
			if (user != null) {										//checks to make sure it is a valid user
				// Successful login!

				// Add user to session
				req.getSession().setAttribute("user", user);		//makes sure the user is logged in and the attribute 
				//can be accessed by any servlet now

				MajorController majorController = new MajorController();
				Major major = majorController.findMajorForUser(user);
				List<Course> alreadyReg = majorController.findCourseByUser(user);
				List<Course> doneReg = majorController.findCourseByUserDone(user);

				if (major != null) {
					// Redirect to class path
					req.getSession().setAttribute("maj", major);
					req.getSession().setAttribute("todoList", alreadyReg);
					req.getSession().setAttribute("doneList", doneReg);
					resp.sendRedirect(req.getContextPath() + "/class");
				} else {
					// Redirect to major page
					resp.sendRedirect(req.getContextPath() + "/major");		//after a successful session on a servlet you should
				}
				return;
				
			} else {
				user = controller.newUser(username, password);
				
				req.getSession().setAttribute("user", user);
				resp.sendRedirect(req.getContextPath() + "/major");	
			}
		}
		//this can be all used in the html code
		req.setAttribute("username", username);			//sets the attribute for the username for the input
		req.setAttribute("password", password);			//sets the attribute for the password for the input
		req.setAttribute("error", error);				//sets the error msg for the html code

		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
}
