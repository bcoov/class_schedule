package classSchedule.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import classSchedule.LoginController;
import classSchedule.MajorController;
import classSchedule.model.Major;
import classSchedule.model.User;
import classSchedule.model.Course;
import classSchedule.persist.InitialData;

public class MajorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//this is to display all the majors to choose from
		List <Major> allMajors= InitialData.getMajors();
		req.getSession().setAttribute("allMajors", allMajors);
		req.getRequestDispatcher("/_view/major.jsp").forward(req, resp);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
		String major = req.getParameter("major");
		String error = "";

		
		if(major == null)
		{
			error = "Please enter a major";
		}
		else
		{
			MajorController controller = new MajorController();		//make a new controller for each servlet
			Major maj = controller.findMajor(major);
			
			 
			if(maj != null)
			{
				//Real major
				req.getSession().setAttribute("maj", maj);
				
				// Store the student's major in the database
				User user = (User) req.getSession().getAttribute("user");
				controller.storeMajorForUser(user, maj);
				
				// Redirect to schedule page
				resp.sendRedirect(req.getContextPath() + "/class");
				
				return;
			}
			else
			{
				error = "Unknown major";
			}
		}
		
		req.setAttribute("maj", major);
		req.setAttribute("error", error);
		
		req.getRequestDispatcher("/_view/major.jsp").forward(req, resp);
	}
}
