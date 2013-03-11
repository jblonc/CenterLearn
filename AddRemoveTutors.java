/*
 * AddRemoveTutors.java
 *
 * Created on June 23, 2005, 4:21 PM
 */

package LearningCenter;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.*;
import java.text.*;
import java.util.*;
/**
 *
 * @author  Jinbo Lu
 * @version
 */
public class AddRemoveTutors extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");
        response.setHeader("Cache-Control","no-cache");//http 1.1, using "no-store" over "no-cache" if the latter doesn't work
        response.setHeader("Pragma","no-cache");//http 1.0: compatibility with old versions
        response.setHeader("Expires","-1");//prevents caching at the proxy server
        PrintWriter out = response.getWriter();
        HttpSession session=request.getSession();
        String status=(String)session.getAttribute("status");
        String dirStatus=(String)session.getAttribute("dirStatus");
        String terminatedStatus=(String)session.getAttribute("terminatedStatus");
        String tutorStatus=(String)session.getAttribute("tutorStatus");
        String DatabaseUser=(String)session.getAttribute("DatabaseUser");
        String DatabasePassword=(String)session.getAttribute("DatabasePassword");
        String url=(String)session.getAttribute("url");
        String driver=(String)session.getAttribute("driver");
        String tutors_ta=(String)session.getAttribute("tutors_ta");
        String records_ta=(String)session.getAttribute("records_ta");
        String tutors_ta_Query="SELECT username,fname,lname,status FROM "+tutors_ta;
        String logonUSER=(String)session.getAttribute("username");
        String logonPASS=(String)session.getAttribute("password");
        boolean keywordRepetition=false;
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Add or Remove Tutors</title>");
        out.println("</head>");
        out.println("<body>");
        
        Calendar cal=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        SimpleDateFormat sdf_deletion=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat sdf_short=new SimpleDateFormat("MM/dd/yyyy");
        //sdf.setTimeZone(TimeZone.getDefault());sdf_deletion.setTimeZone(TimeZone.getDefault());//these lines are not necessary
        out.print("<br><center><strong><font color=#0066FF>"+sdf.format(cal.getTime())+"</font></strong></center><br>");
        //out.print(System.currentTimeMillis());
        out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"Tutoring.jsp \">" );
        out.println("<td><input type=\"submit\" value=\"back\"></FORM>");
        
        
        out.println("<FORM METHOD=\"POST\" ACTION=\"logout \">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div><br>");
            try {
      // Load database driver if it's not already loaded.
      Class.forName(driver);
      // Establish network connection to database.
      Connection connection =DriverManager.getConnection(url, DatabaseUser, DatabasePassword);
      Statement statement_pre=connection.createStatement();
      ResultSet resultSet_pre=statement_pre.executeQuery(tutors_ta_Query);
      /*String newTutor=request.getParameter("newTutor");newTutor.trim();
      String newPassword=request.getParameter("newPassword");newPassword.trim();
      String firstName=request.getParameter("firstName");firstName.trim();
      String lastName=request.getParameter("lastName");lastName.trim();
      String newStatus=request.getParameter("newStatus");newStatus.trim();*/
      if(request.getParameter("newTutor") != null &&  request.getParameter("newPassword")!= null && request.getParameter("firstName") != null && 
         request.getParameter("lastName") != null && request.getParameter("newStatus")!=null){
          //out.print("test<br>");
          String newTutor=request.getParameter("newTutor");newTutor.trim();
          String newPassword=request.getParameter("newPassword");newPassword.trim();
          String firstName=request.getParameter("firstName");firstName.trim();
          String lastName=request.getParameter("lastName");lastName.trim();
          String newStatus=request.getParameter("newStatus");newStatus.trim();
          if(newTutor.length()>0&&newPassword.length()>0&&firstName.length()>0&&lastName.length()>0&& (newStatus.equals(dirStatus) || newStatus.equals(tutorStatus)))
          {
           while(resultSet_pre.next()){if(resultSet_pre.getString("username").equals(newTutor)){keywordRepetition=true;}}
          if(!keywordRepetition){String insertionQuery="INSERT INTO "+tutors_ta+" (username,password,fname,lname,status,start_date)"+
          " VALUES ('"+newTutor+"','"+newPassword+"','"+firstName+"','"+lastName+"','"+newStatus+"','"+sdf_short.format(cal.getTime())+"')";
          Statement statement_insertion=connection.createStatement();
          statement_insertion.executeUpdate(insertionQuery);
          out.print("<br> <font color=blue>A new "+newStatus+" was inserted: username= "+newTutor+", password= "+newPassword+".</font>");}
          else{out.print("<font color=red>The username: "+newTutor+" has already been used; please choose a different username.</font>");}
      }}
      Statement statement_display = connection.createStatement();
      // Send query to database and store results.
      ResultSet resultSet_display = statement_display.executeQuery(tutors_ta_Query);
    
      out.print("<center><form method=post action=\"addremovetutors\"><table cellspacing=\"30\"><tr><td align=\"center\"><input type=\"submit\" value=\"Delete or Add a tutor\"><td align=\"center\">" +
      "<input type=\"reset\" value=\"Reset\">");
      out.print("<tr><td valign=\"top\"><center><font color=#CC0000><u>Delete a tutor</u>:</center><br><center><table cellspacing=\"10\"><tr><td>username<td>name<td>status<td><font color=red>delete?</font>");
      while(resultSet_display.next()){
          String un=resultSet_display.getString("username");
          String st=resultSet_display.getString("status");
          String fn=resultSet_display.getString("fname");
          String ln=resultSet_display.getString("lname");
          //out.print(request.getParameter(un));
          if(request.getParameter(un)!=null){ 
             //out.print(request.getParameter(un));
             String termination_Query="UPDATE "+tutors_ta+" SET status='terminated',username='deleted_"+un+":"+sdf_deletion.format(cal.getTime())+"',end_date='"+sdf_short.format(cal.getTime())+"' WHERE username ='"+un+"'";
             Statement statement_termination=connection.createStatement();
             statement_termination.executeUpdate(termination_Query);
             st=terminatedStatus;
         }
          if(!st.equals(terminatedStatus)){out.print("<tr><td>"+un+"<td>"+fn+", "+ln+"<td>"+st+"<td>"); /*+
          "<input type=\"checkbox\" name="+un+" value="+un+">");*/
          if(!un.equals(logonUSER)){out.print("<input type=\"checkbox\" name="+un+" value="+un+">");}
          //the above line: current user can't delete self, but a director can delete other directors. so a director
          //can recreate a username for self. if director can't be deleted any how--supreme status, then change this line
          //to !st.equals(dirStatus). and create some other status other than director and tutor, and that status can access
          //AddRemoveTutors or GenerateReports but can be deleted by director, still shouldn't delete self.
         /*if(request.getParameter(un)!=null){ 
             out.print(request.getParameter(un));
             String termination_Query="UPDATE "+tutors_ta+" SET status='terminated',username='deleted_"+un+"' WHERE username ='"+un+"'";
             Statement statement_termination=connection.createStatement();
             statement_termination.executeUpdate(termination_Query);
         
         }*/}
          //if((Boolean.valueOf(request.getParameter(un))).booleanValue()){out.print("hello");}
      }
      /*out.print("<tr><form><input type=\"checkbox\" name=\"test\"  value=\"smart\">");
       if(request.getParameter("test")!=null){out.print("dada");out.print(request.getParameter("test"));}*/    
      out.print("</table></center>");
      out.print("<td valign=\"top\"><center><font color=#009900><strong>Add a new tutor:</strong></font></center><br><center><table cellspacing=\"10\"><tr><td>username:<td><input type=\"text\" name=\"newTutor\">");
      out.print("<tr><td>password:<td><input type=\"password\" name=\"newPassword\">");
      out.print("<tr><td>first name:<td><input type=\"text\" name=\"firstName\">");
      out.print("<tr><td>last name:<td><input type=\"text\" name=\"lastName\">");
      out.print("<tr><td>status:<td><select name=\"newStatus\" ><option value=\"tutor\" selected>tutor<option value=\"director\">director</select>");
      //out.print("status:<input type=\"text\" name=\"newStatus\" value=\"tutor\"><br>");
      out.print("</table></center></table></form></center>");
      


        
      
      
     
      connection.close();
    } catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }

        /* TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");
         
        out.println("</body>");
        out.println("</html>");
         */
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
