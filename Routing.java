/*
 * routing.java
 *
 * Created on June 21, 2005, 4:11 PM
 */

package LearningCenter;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.*;

/**
 *
 * @author  Jinbo Lu
 * @version
 */
public class Routing extends HttpServlet {
    
    /** Initializes the servlet.
     */
boolean authorized, isDir; /*voted*/;
   
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    //the following 3 lines prevent using back and forward buttons for security reasons
        //back-forward are client-side attributes, basically using cache
        response.setHeader("Cache-Control","no-cache");//http 1.1, using "no-store" over "no-cache" if the latter doesn't work
        response.setHeader("Pragma","no-cache");//http 1.0: compatibility with old versions
        response.setHeader("Expires","-1");//prevents caching at the proxy server
        
    PrintWriter out = response.getWriter();
    String docType =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
      "Transitional//EN\"\n";
    String title = "role check";
    out.print(docType +
              "<HTML>\n" +
              "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
              "<BODY BGCOLOR=\"#FDF5E6\"><CENTER>\n" +
              "\n");
    HttpSession session=request.getSession();
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
    String url = "jdbc:odbc:learningcenter";//should be modified for each different semester
    String DatabaseUser="";//here can be any string but empty
    String DatabasePassword="";
    String tutors_ta="tutors";
    String records_ta="tut_records";
    String tutees_ta="tutees";
    //String director="mbroeke";
    String dirStatus="director";
    String tutorStatus="tutor";
    String terminatedStatus="terminated";
    session.setAttribute("dirStatus",dirStatus);
    session.setAttribute("tutorStatus",tutorStatus);
    session.setAttribute("terminatedStatus",terminatedStatus);
    //session.setAttribute("tutorStatus",tutorStatus);
    /*String CourseName="math117";//table name in the above database for each course; should be modified
    String CourseAdminUser="admin117";//administrator username in the above table
    String CourseManager="m117";
    int QZ_Num_Drop=2;//number of lowest quizzes to be discounted toward final grades
    int HW_Num_Drop=0;//number of lowest hw grades to be discounted
    int[] Num_Drop={QZ_Num_Drop,HW_Num_Drop};
    int[] missing_times={5,3};//attendance missing times for bonus purpose---explained next line
    double[] attendance_bonus={0.01,0.02};//0.01 for less than 5 times, 0.02 for less than 3 times
    double[] G_Percent_Distr={0.13,0.16,0.18,0.18,0.35};//in the order: hw, quiz, exam1,exam2,...,final
    double[] G_Perfect={5,10,50,50,100};//in the order: hw, quiz,exam1,exam2,...,final
    double[] L_Grades={0.9,0.87,0.84,0.80,0.77,0.75,0.7,0.65,0.5};//A,A-,B+,B,B-,C+,C,C-,D
    String SurveyDirectory="../webapps/edu/surveys/_what_do_you_think.jsp";//edu replaced by coursename_sp05
        //should be modified for dir changes, pay attention to saveSurf.jsp where dir is set
    String CourseHome="http://www.marshfield.uwc.edu/faculty/jlu/2005/"+CourseName+".htm";
     **/
    session.setAttribute("driver",driver);
    session.setAttribute("url",url);
    session.setAttribute("DatabasePassword",DatabasePassword);
    session.setAttribute("DatabaseUser",DatabaseUser);
    session.setAttribute("tutors_ta",tutors_ta);
    session.setAttribute("records_ta",records_ta);
    session.setAttribute("tutees_ta",tutees_ta);
    /*session.setAttribute("CourseName",CourseName);
    session.setAttribute("CourseAdminUser",CourseAdminUser);
    session.setAttribute("QZ_Num_Drop",new Integer(QZ_Num_Drop));
    session.setAttribute("HW_Num_Drop",new Integer(HW_Num_Drop));
    session.setAttribute("Num_Drop",Num_Drop);
    session.setAttribute("missing_times",missing_times);
    session.setAttribute("attendance_bonus",attendance_bonus);
    session.setAttribute("G_Percent_Distr",G_Percent_Distr);
    session.setAttribute("G_Perfect",G_Perfect);
    session.setAttribute("L_Grades",L_Grades);
    session.setAttribute("SurveyDirectory",SurveyDirectory);
    session.setAttribute("CourseHome",CourseHome);
    */
    String courses_ta="courses";
    String[] courses=new String[100];//make sure 100 is bigger than # of courses in courses_ta (not # of records in courses_ta)
   Integer _actualNumOfCourses=new Integer(1);
    session.setAttribute("actualNumOfCourses",_actualNumOfCourses);
    session.setAttribute("courses",courses);
    session.setAttribute("TimeOut","TimeOut");//to test whether the session has timed out
    
    String PleaseCheck="<br>Check your username and password and try again";//username,password verification response; change font, color  
    //String query = "SELECT username, password, status FROM "+tutors_ta;
    //showReport(driver, url,DatabaseUser,DatabasePassword, username, password,query, out, dirStatus,terminatedStatus, PleaseCheck);
    //info about the courses table
    try{
   Class.forName(driver);
   Connection connection = DriverManager.getConnection(url,DatabaseUser,DatabasePassword);
   Statement statement_courses=connection.createStatement();
   Statement statement_profs=connection.createStatement();
   ResultSet resultSet_courses=statement_courses.executeQuery("SELECT DISTINCT course_number from "+courses_ta);
   String cn; ResultSet resultSet_profs;
   int Num=0;
   while(resultSet_courses.next()){
   cn=resultSet_courses.getString("course_number");courses[Num]=cn;
   resultSet_profs=statement_profs.executeQuery("SELECT professor from "+courses_ta+" WHERE course_number='"+cn+"'");
   while(resultSet_profs.next()){courses[Num]+="@"+resultSet_profs.getString("professor");}
  // out.print(courses[Num]);
   Num++;
    }
   _actualNumOfCourses=new Integer(Num);session.setAttribute("actualNumOfCourses",_actualNumOfCourses);
   //_actualNumOfCourses=k;
   
   boolean isTuteeYet=false;//usernameUsed_ee=false,usernameUsed_or=false;
   Statement stmt=connection.createStatement();
   String qry="SELECT ID,password FROM "+tutees_ta;
   ResultSet rs=stmt.executeQuery(qry);
   while(rs.next()){if(rs.getString("ID").equals(username)){
           //usernameUsed_ee=true; 
         if(rs.getString("password").equals(password)){isTuteeYet=true;}}}
   if(isTuteeYet){session.setAttribute("username",username);
               session.setAttribute("password",password);
               RequestDispatcher view= request.getRequestDispatcher("Tutee.jsp");
               view.forward(request,response);};
    //The following lines may be needed (partially) to allow new tutees to self-register
   /*if(!isTuteeYet){
       if(username!=null &&password!=null){
           username.trim();password.trim();
           if(username.length()>0&&password.length()>0){
       Statement stmt_ =connection.createStatement();
       String qry_i="INSERT INTO "+tutees_ta+" (ID,password) "+"VALUES ('"+username+"','"+password+"')";
       stmt_.executeUpdate(qry_i);
   }}}*/
   connection.close();
    }
    catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }

    
    //authorization
    String query = "SELECT username, password, status FROM "+tutors_ta;//survey removed
 
    
    showReport(driver, url,DatabaseUser,DatabasePassword, username, password,query, out, dirStatus,terminatedStatus, PleaseCheck);
    out.println("<br>");
    
    
    if(isDir){session.setAttribute("status",dirStatus);session.setAttribute("username",username);
               session.setAttribute("password",password);
               RequestDispatcher view= request.getRequestDispatcher("Tutoring.jsp");
                view.forward(request,response); }
    
    else if(authorized){
        session.setAttribute("status",tutorStatus);session.setAttribute("username",username);
        session.setAttribute("password",password);
        RequestDispatcher view= request.getRequestDispatcher("Tutoring.jsp");
                view.forward(request,response);}
    
    out.println("</CENTER></BODY></HTML>");
 out.close(); }

  public void showReport(String driver, String url, String DatabaseUser,String DatabasePassword,
                        String uname, String pword,String query,
                        PrintWriter out, String status, String t_status, String Verification) {
    try {
      // Load database driver if it's not already loaded.
      Class.forName(driver);
      // Establish network connection to database.
      Connection connection =DriverManager.getConnection(url, DatabaseUser, DatabasePassword);
      
      
      Statement statement = connection.createStatement();
      // Send query to database and store results.
      ResultSet resultSet = statement.executeQuery(query);
      // Print results.
      authorized = false; isDir=false; //voted=false;
      
      // Step through each row in the result set.
      while(resultSet.next()) {
          String un=resultSet.getString("username"),pw=resultSet.getString("password");
          String st=resultSet.getString("status");
         // boolean vc=resultSet.getBoolean("survey");
        if (uname.equals(un)&& pword.equals(pw) && !st.equals(t_status)){
                                      authorized=true; 
                                      if(st.equals(status)){isDir=true;}
                                      //else if (vc){voted=true;}
        }
        }
        
      
      if (!authorized) {out.println(Verification);} 
     
      connection.close();
    } catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }
  }
    
}
