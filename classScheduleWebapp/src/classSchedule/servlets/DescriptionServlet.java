package classSchedule.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classSchedule.DescriptionController;
import classSchedule.model.Course;
import classSchedule.model.Description;
import classSchedule.model.User;



public class DescriptionServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		private List<Course> done = new ArrayList<Course>();
		private List<Course> scheduled = new ArrayList<Course>();
		private List<Course> fall14 = new ArrayList<Course>();
		private List<Course> spring15 = new ArrayList<Course>();
		private List<Course> fall15 = new ArrayList<Course>();
		private List<Course> spring16 = new ArrayList<Course>();
		private List<Course> fall16= new ArrayList<Course>();
		private List<Course> spring17 = new ArrayList<Course>();
		private List<Course> fall17 = new ArrayList<Course>();
		private List<Course> spring18 = new ArrayList<Course>();
		private List<Course> fall18= new ArrayList<Course>();
		private List<Course> spring19 = new ArrayList<Course>();
		private List<Course> fall19 = new ArrayList<Course>();
		private List<Course> spring20= new ArrayList<Course>();
		private List<Course> fall20 = new ArrayList<Course>();
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			
			//gets the current course selected from the web page and gets the description of the respective course
			//then sets the attribute of the course for the jsps
			DescriptionController controller = new DescriptionController();
			Course current = (Course) req.getSession().getAttribute("selectedCourse");
			
			Description desc = controller.findDescriptionByCourse(current);
			//System.out.println(""+desc.getDescript());
			
			req.getSession().setAttribute("descr", desc);
			
			req.getRequestDispatcher("/_view/courseDescription.jsp").forward(req, resp);
		}

		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
			
			//TODO: if button pressed to either add to done list or to do for scheduling list then make them go back to
			//class.jsp to pick out another class
			
			Course selected = (Course) req.getSession().getAttribute("selectedCourse");
			
			DescriptionController controller = new DescriptionController();
			User user = (User) req.getSession().getAttribute("user");
			Course course = (Course) req.getSession().getAttribute("selectedCourse");
			
			if(req.getParameter("todo") != null)
			{
				//System.out.println("The user pressed TODO");
				scheduled.add(selected);
				req.getSession().setAttribute("todoList", scheduled);

				
				switch(selected.getSemester())
				{
				case "Fall 2014":
					fall14.add(selected);
					break;
				case "Spring 2015":
					spring15.add(selected);
					break;
				case "Fall 2015":
					fall15.add(selected);
					break;
				case "Spring 2016":
					spring16.add(selected);
					break;
				case "Fall 2016":
					fall16.add(selected);
					break;
				case "Spring 2017":
					spring17.add(selected);
					break;
				case "Fall 2017":
					fall17.add(selected);
					break;
				case "Spring 2018":
					spring18.add(selected);
					break;
				case "Fall 2018":
					fall18.add(selected);
					break;
				case "Spring 2019":
					spring19.add(selected);
					break;
				case "Fall 2019":
					fall19.add(selected);
					break;
				case "Spring 2020":
					spring20.add(selected);
					break;
				case "Fall 2020":
					fall20.add(selected);
					break;
				}
				
				req.getSession().setAttribute("fall14list", fall14);
				req.getSession().setAttribute("spring15list", spring15);
				req.getSession().setAttribute("fall15list", fall15);
				req.getSession().setAttribute("spring16list", spring16);
				req.getSession().setAttribute("fall16list", fall16);
				req.getSession().setAttribute("spring17list", spring17);
				req.getSession().setAttribute("fall17list", fall17);
				req.getSession().setAttribute("spring18list", spring18);
				req.getSession().setAttribute("fall18list", fall18);
				req.getSession().setAttribute("spring19list", spring19);
				req.getSession().setAttribute("fall19list", fall19);
				req.getSession().setAttribute("spring20list", spring20);
				req.getSession().setAttribute("fall20list", fall20);
				resp.sendRedirect(req.getContextPath() + "/class");

				controller.storeCoursesForUsers(user, course);
				resp.sendRedirect(req.getContextPath() + "/class");
				
			}
			
			if(req.getParameter("completed") != null)
			{
				//System.out.println("The user completed this course");
				done.add(selected);
				req.getSession().setAttribute("doneList", done);
				controller.storeCoursesForUsersDone(user, course);
				resp.sendRedirect(req.getContextPath() + "/class");
				
				
			}
			return;
		}
}
