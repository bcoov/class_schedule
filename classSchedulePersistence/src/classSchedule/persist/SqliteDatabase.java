package classSchedule.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import classSchedule.model.Course;
import classSchedule.model.Description;
import classSchedule.model.IdRelation;
import classSchedule.model.Major;
import classSchedule.model.Professor;
import classSchedule.model.User;



public class SqliteDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load sqlite driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;

	//TODO: Add an sql database entry for finding the user from the login information
	@Override
	public User findUser(String username, String password) {
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select users.* " +			//the entire user tuple
							"  from users " +
							" where users.username = ? and users.password = ?"
					);
					stmt.setString(1, username);
					stmt.setString(2, password);
					
					User result = null;
					
					resultSet = stmt.executeQuery();
					if (resultSet.next()) { // query will produce at most 1 user or null if it does not exist
						result = new User();
						loadUser(result, resultSet, 1);
					}
					
					return result;			//returns an actual user or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	@Override
	public Major findMajor(String major) {
		return executeTransaction(new Transaction<Major>() {
			@Override
			public Major execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select majors.* " +			//the entire major tuple
							"  from majors " +
							" where majors.major = ?"
					);
					stmt.setString(1, major);
					
					Major result = null;
					
					resultSet = stmt.executeQuery();
					if (resultSet.next()) { // query will produce at most 1 major or null if it does not exist
						result = new Major();
						loadMajor(result, resultSet, 1);
					}
					return result;			//returns an actual major or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	@Override
	public Course findCoursebyTitle(String courseName) {
		return executeTransaction(new Transaction<Course>() {
			@Override
			public Course execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select courses.* " +			//the entire courses tuple
							"  from courses " +
							" where courses.name = ?"
					);
					stmt.setString(1, courseName);
					
					Course result = null;
					
					resultSet = stmt.executeQuery();
					if (resultSet.next()) { // query will produce at most 1 course or null if it does not exist
						result = new Course();
						loadCourse(result, resultSet, 1);
					}
					
					return result;			//returns an actual course or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	@Override
	public Professor findProfessor(String firstname, String lastname) {
		return executeTransaction(new Transaction<Professor>() {
			@Override
			public Professor execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select professors.* " +			//the entire prof tuple
							"  from professors " +
							" where professors.firstname = ? and professors.lastname = ?"
					);
					stmt.setString(1, firstname);
					stmt.setString(2, lastname);
					
					Professor result = null;
					
					resultSet = stmt.executeQuery();
					if (resultSet.next()) { 
						result = new Professor();
						loadProfessor(result, resultSet, 1);
					}
					
					return result;			//returns an actual professor or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	@Override
	public IdRelation storeMajorForUser(User user, Major major) {
		return executeTransaction(new Transaction<IdRelation>() {
			@Override
			public IdRelation execute(Connection conn) throws SQLException {
				IdRelation relate = new IdRelation();
				
				PreparedStatement stmt = null;
				
				//TODO:if new major then delete the tuple then add a completely new one
				
				try {
					stmt = conn.prepareStatement("insert into userMajors values (?, ?)");
					stmt.setInt(1, user.getId());
					stmt.setInt(2, major.getId());
					
					stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
				
				return relate;
			}
		});
	}
	
	//TODO: use this method to actually insert a user into the database
	@Override
	public User newUser(String username, String password) {
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				User user = new User();
				
				user.setUsername(username);
				user.setPassword(password);
				
				
				PreparedStatement stmt = null;
				ResultSet genKeys = null;
				
				try {
					stmt = conn.prepareStatement(
							"insert into users (username, password) values (?, ?)",
							PreparedStatement.RETURN_GENERATED_KEYS
					);
					stmt.setString(1, user.getUsername());
					stmt.setString(2, user.getPassword());
					
					
					//do update if inserting or deleting anything
					//do executeQuery otherwise
					stmt.executeUpdate();
					
					genKeys = stmt.getGeneratedKeys();
					genKeys.next();
					user.setId(genKeys.getInt(1));
					
					//should usually do a print statement for debugging
					System.out.println("Successfully inserted user with id=" + user.getId());
					
					return user;
				} finally {
					DBUtil.closeQuietly(genKeys);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	@Override
	public Major findMajorByUser(User user) {
		return executeTransaction(new Transaction<Major>() {
			@Override
			public Major execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select majors.* " +			//the entire major tuple
							"  from users, userMajors, majors " +
							" where users.id = userMajors.userId " +
							" and userMajors.majorId = majors.id " +
							" and users.id = ?"
					);
					stmt.setInt(1, user.getId());

					
					Major result = null;
					
					resultSet = stmt.executeQuery();
					if (resultSet.next()) { 
						result = new Major();
						loadMajor(result, resultSet, 1);
					}
					
					return result;			//returns an actual major or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
		
	}
	
	@Override
	public List<Course> findCourseByMajor(Major major) {
		return executeTransaction(new Transaction <List<Course>>() {
			@Override
			public List<Course> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				List<Course> courses = new ArrayList<Course>();
				
				
				try {
					stmt = conn.prepareStatement(
							"select courses.* " +			//the entire course tuple
							"  from majors, majorCourses, courses " +
							" where majors.id = majorCourses.majorId " +
							" and majorCourses.courseId = courses.id " +
							" and majors.id = ?"
					);
					stmt.setInt(1, major.getId());

					
					Course result = null;
					
					resultSet = stmt.executeQuery();
					
					while (resultSet.next()) { 
						result = new Course();
						loadCourse(result, resultSet, 1);
						courses.add(result);
					}
					
					return courses;			//returns an actual courses or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
		
	}
	
	@Override
	public Description findDescriptionByCourse(Course cour) {
		return executeTransaction(new Transaction <Description>() {
			@Override
			public Description execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select descriptions.* " +			//the entire desc tuple
							"  from descriptions " +
							" where descriptions.id = ?"
					);
					//we set the desc id to the course id because they are the same in the csv files
					//if not we had a create unique id implemented for that case
					stmt.setInt(1, cour.getId());

					
					Description result = null;
					
					resultSet = stmt.executeQuery();
					
					if(resultSet.next()) { 
						result = new Description();
						loadDescription(result, resultSet, 1);
					}
					
					return result;			//returns an actual desc or null if there is not one
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
		
	}
	
	
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}
	
	private Connection connect() throws SQLException {
		String home = System.getProperty("user.home");		//creates the database file in the home directory of whoever uses it
		//connects to the database from the home directory for the WebApp folder
		Connection conn = DriverManager.getConnection("jdbc:sqlite:" + home + "/classSchedule.db");	
		
		// Set autocommit to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	

	private void loadUser(User user, ResultSet resultSet, int i) throws SQLException{
		user.setId(resultSet.getInt(i++));
		user.setUsername(resultSet.getString(i++));
		user.setPassword(resultSet.getString(i++));	
	}
	
	private void loadMajor(Major result, ResultSet resultSet, int i) throws SQLException{
		result.setId(resultSet.getInt(i++));
		result.setName(resultSet.getString(i++));
		
	}

	private void loadProfessor(Professor result, ResultSet resultSet, int i) throws SQLException{
		result.setFirstName(resultSet.getString(i++));
		result.setLastName(resultSet.getString(i++));
	}
	
	private void loadCourse(Course result, ResultSet resultSet, int i) throws SQLException{
		result.setId(resultSet.getInt(i++));
		result.setName(resultSet.getString(i++));
	}
	
	private void loadDescription(Description result, ResultSet resultSet, int i) throws SQLException{
		result.setId(resultSet.getInt(i++));
		result.setDescript(resultSet.getString(i++));
		
	}
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt5a = null;
				PreparedStatement stmt6 = null;
				PreparedStatement stmt6a = null;
				PreparedStatement stmt7 = null;
				PreparedStatement stmt8 = null;
				PreparedStatement stmt8a = null;
				try {
					stmt1 = conn.prepareStatement(
							"create table users (" +
							"    id integer primary key," +
							"    username varchar(25)," +
							"    password varchar(50)" +
							")");
					stmt1.executeUpdate();
					
					stmt2 = conn.prepareStatement(
							"create table majors (" +
							"	id integer primary key," +
							"	major varchar(40)" +
							")");
					stmt2.executeUpdate();
					
					stmt3 = conn.prepareStatement(
							"create table courses (" +
							"	id integer primary key," +
							"	name varchar(80)" +
							")");
					stmt3.executeUpdate();
					
					stmt4 = conn.prepareStatement(
							"create table professors (" +
							"   id integer primary key, " +
							"   firstname varchar(20)," +
							"   lastname varchar(20)" +
							")");
					stmt4.executeUpdate();
					
					stmt5 = conn.prepareStatement(
							"create table majorCourses(" +
							"   majorId integer, " +
							"   courseId integer" +
							")");
					stmt5.executeUpdate();
					
					//create a separate prepareStatement for a unique index 
					stmt5a = conn.prepareStatement("create unique index major_course_idx on majorCourses(majorId, courseId)");
					stmt5a.executeUpdate();
					
					stmt6 = conn.prepareStatement(
							"create table userMajors(" +
							"   userId integer, " +
							"   majorId integer" +
							")");
					stmt6.executeUpdate();
					
					//create a separate prepareStatement for a unique index 
					stmt6a = conn.prepareStatement("create unique index user_major_idx on userMajors(userId, majorId)");
					stmt6a.executeUpdate();
					
					stmt7 = conn.prepareStatement(
							"create table descriptions(" +
							"	id integer primary key, " +
							"	descript varchar(400)" +
							")");
					stmt7.executeUpdate();
					
					stmt8 = conn.prepareStatement(
							"create table courDescript(" +
							"	courseId integer, " +
							"	descriptId integer" +
							")");
					stmt8.executeUpdate();
					
					stmt8a = conn.prepareStatement("create unique index course_description_idx on courDescript(courseId, descriptId)");
					stmt8a.executeUpdate();
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt5a);
					DBUtil.closeQuietly(stmt6);
					DBUtil.closeQuietly(stmt6a);
					DBUtil.closeQuietly(stmt7);
					DBUtil.closeQuietly(stmt8);
					DBUtil.closeQuietly(stmt8a);
				}
			}
		});
	}
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<User> userList;
				List<Major> majorList;
				List<Course> courseList;
				List<Professor> professorList;
				List<IdRelation> majorCourseList;
				List<IdRelation> userMajorList;
				List<Description> descList;
				List<IdRelation> courDescList;
				
				try {
					//this gets the csvs for the initial data to the SQL
					userList = InitialData.getUsers();
					majorList = InitialData.getMajors();
					courseList = InitialData.getCourses();
					professorList = InitialData.getProfessors();
					majorCourseList = InitialData.getMajorCourses();
					userMajorList = InitialData.getUserMajors();
					descList = InitialData.getDescriptions();
					courDescList = InitialData.getCourDesc();
					
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertUser = null;
				PreparedStatement insertMajor = null;
				PreparedStatement insertCourse = null;
				PreparedStatement insertProfessor = null;
				PreparedStatement insertMajorCourse = null;
				PreparedStatement insertUserMajor = null;
				PreparedStatement insertDesc = null;
				PreparedStatement insertCourDesc = null;
				
				try {
					insertUser = conn.prepareStatement("insert into users values (?, ?, ?)");
					for (User use : userList) {
						insertUser.setInt(1, use.getId());
						insertUser.setString(2, use.getUsername());
						insertUser.setString(3, use.getPassword());
						insertUser.addBatch();
					}
					insertUser.executeBatch();
					
					insertMajor = conn.prepareStatement("insert into majors values (?, ?)");
					for(Major maj : majorList)
					{
						insertMajor.setInt(1, maj.getId());
						insertMajor.setString(2, maj.getName());
						insertMajor.addBatch();
					}
					insertMajor.executeBatch();
					
					insertCourse = conn.prepareStatement("insert into courses values (?, ?)");
					for(Course cour: courseList)
					{
						insertCourse.setInt(1, cour.getId());
						insertCourse.setString(2, cour.getName());
						insertCourse.addBatch();
					}
					insertCourse.executeBatch();
					
					insertProfessor = conn.prepareStatement("insert into professors values (?, ?, ?)");
					for (Professor p : professorList)
					{
						insertProfessor.setInt(1, p.getID());
						insertProfessor.setString(2, p.getFirstName());
						insertProfessor.setString(3, p.getLastName());
						insertProfessor.addBatch();
					}
					insertProfessor.executeBatch();
					
					insertMajorCourse = conn.prepareStatement("insert into majorCourses values (?, ?)");
					for(IdRelation majCourse: majorCourseList)
					{
						insertMajorCourse.setInt(1, majCourse.getId1());
						insertMajorCourse.setInt(2, majCourse.getId2());
						insertMajorCourse.addBatch();
						//used for debugging
						//System.out.println("Inserting into majorCourses " + majCourse.getId1() + "," + majCourse.getId2());
					}
					insertMajorCourse.executeBatch();
					
					insertUserMajor = conn.prepareStatement("insert into userMajors values (?, ?)");
					for(IdRelation userMaj: userMajorList)
					{
						insertUserMajor.setInt(1, userMaj.getId1());
						insertUserMajor.setInt(2, userMaj.getId2());
						insertUserMajor.addBatch();
					}
					insertUserMajor.executeBatch();
					
					insertDesc = conn.prepareStatement("insert into descriptions values (?,?)");
					for(Description descs : descList)
					{
						insertDesc.setInt(1, descs.getId());
						insertDesc.setString(2, descs.getDescript());
						insertDesc.addBatch();
					}
					insertDesc.executeBatch();
					
					insertCourDesc = conn.prepareStatement("insert into courDescript values (?,?)");
					for(IdRelation cd : courDescList)
					{
						insertCourDesc.setInt(1, cd.getId1());
						insertCourDesc.setInt(2, cd.getId2());
						insertCourDesc.addBatch();
					}
					insertCourDesc.executeBatch();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertUser);
					DBUtil.closeQuietly(insertMajor);
					DBUtil.closeQuietly(insertCourse);
					DBUtil.closeQuietly(insertProfessor);
					DBUtil.closeQuietly(insertMajorCourse);
					DBUtil.closeQuietly(insertUserMajor);
					DBUtil.closeQuietly(insertDesc);
					DBUtil.closeQuietly(insertCourDesc);
				}
			}
		});
	}
	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		SqliteDatabase db = new SqliteDatabase();
		db.createTables();
		
		System.out.println("Loading initial data...");
		db.loadInitialData();
		
		System.out.println("Success!");
	}
	




	






}
