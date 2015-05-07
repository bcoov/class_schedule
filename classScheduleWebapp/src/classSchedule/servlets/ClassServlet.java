package classSchedule.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import classSchedule.ClassController;
import classSchedule.model.Course;
import classSchedule.model.Major;


public class ClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//gets the current user of the session and finds their major from unique indexes
		ClassController controller = new ClassController();
		Major major = (Major) req.getSession().getAttribute("maj");
		
		List<Course> classes = controller.findCourseByMajor(major);
		req.getSession().setAttribute("allCourses", classes);
		
		List<Course> testlist = (List<Course>)req.getSession().getAttribute("fall15list");
		
		if(testlist != null)
		{
			if(testlist.size() > 0)
				System.out.println("fall 2015");
			else
				System.out.println("nothing");
		}
				
		
		
		req.getRequestDispatcher("/_view/class.jsp").forward(req, resp);
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {

		String coursetitle = req.getParameter("courseItem");
		List<Course> classes = (List<Course>) req.getSession().getAttribute("allCourses");
		
		//goes through the entire course list for the provided major and sets the selected value to an attribute 
		//this is so we have a value to use for the CourseDescriptionServlet
		for(Course item: classes){
			if (item.getName().equalsIgnoreCase(coursetitle))
			{
				req.getSession().setAttribute("selectedCourse", item);
				resp.sendRedirect(req.getContextPath() + "/courseDescription");
				
				return;
			}
		}
		return;

	}
}
