/*
 * ChangePass.java
 *
 * Created on July 7, 2005, 1:09 AM
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
public class ChangePass extends HttpServlet {
    
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
        //the following 3 lines prevent using back and forward buttons for security reasons
        //back-forward are client-side attributes, basically using cache
        response.setHeader("Cache-Control","no-cache");//http 1.1, using "no-store" over "no-cache" if the latter doesn't work
        response.setHeader("Pragma","no-cache");//http 1.0: compatibility with old versions
        response.setHeader("Expires","-1");//prevents caching at the proxy server
        
        PrintWriter out = response.getWriter();
        HttpSession session=request.getSession();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>password change</title>");
        out.println("</head>");
        out.println("<body>");
        String DatabaseUser=(String)session.getAttribute("DatabaseUser");
        String DatabasePassword=(String)session.getAttribute("DatabasePassword");
        String url=(String)session.getAttribute("url");
        String driver=(String)session.getAttribute("driver");
        String tutors_ta=(String)session.getAttribute("tutors_ta");
       // String records_ta=(String)session.getAttribute("records_ta");
       // String tutees_ta=(String)session.getAttribute("tutees_ta");
        String username=(String)session.getAttribute("username");
        String password=(String)session.getAttribute("password");
        
        Calendar cal_c=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf_c=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        out.print("<br><center><strong><font color=#0066FF>"+sdf_c.format(cal_c.getTime())+"</font></strong></center><br>");
        
        out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"changepassform \">" );
        out.println("<td><input type=\"submit\" value=\"back\"></FORM>");
        out.println("<FORM METHOD=\"POST\" ACTION=\"Tutoring.jsp \">");  
         out.println("<td><input type=\"submit\" value=\"to start page\"></FORM>");
        
        out.println("<FORM METHOD=\"POST\" ACTION=\"logout \">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div>");
         
         out.println("<br><br>");
         
         
       try{
           Class.forName(driver);
                Connection connection =DriverManager.getConnection(url, DatabaseUser, DatabasePassword);   
                Statement statement_INF = connection.createStatement();
                String query_INF="SELECT password FROM "+tutors_ta+" WHERE username ='"+username+"'";
                ResultSet resultSet_INF = statement_INF.executeQuery(query_INF);
                while(resultSet_INF.next()){
                password=resultSet_INF.getString("password");}
                connection.close();
                
       }
       catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }  
       String usernamet=request.getParameter("usernamet");
       String opassword=request.getParameter("opassword");
       String npassword=request.getParameter("npassword");
       String npasswordc=request.getParameter("npasswordc");
       
       String PasswordChangeStatusMessage;
       Pattern p_digit=Pattern.compile("[0-9]");
       Pattern p_letter=Pattern.compile("[a-zA-Z]");
       Matcher m_digit=p_digit.matcher(npassword);
       Matcher m_letter=p_letter.matcher(npassword);
       
        
       
        out.println("<center>Password Change Status:<br>");
        if(!usernamet.equals(username)){PasswordChangeStatusMessage="<font color=red>Failure</font>: you're not allowed to change your username.";}
        else if(!opassword.equals(password)){PasswordChangeStatusMessage="<font color=red>Failure</font>: you incorrectly typed your current password.";}
        else if(!npasswordc.equals(npassword)){PasswordChangeStatusMessage="<font color=red>Failure</font>: make sure to correctly retype your new password.";}
        else if(npassword.length()<4){PasswordChangeStatusMessage="<font color=red>Failure</font>: your password should contain at least 4 characters.";}
        else if(!m_digit.find(0)){PasswordChangeStatusMessage="<font color=red>Failure</font>: your password should contain at least one number.";}
        else if(!m_letter.find(0)){PasswordChangeStatusMessage="<font color=red>Failure</font>: your password should contain at least one letter.";}
        else {
            PasswordChangeStatusMessage="<font color=blue>Success</font>: your password has been successfully changed.<br>"+
                    "<font color=red>please remember to log on using the new password in the future</font>" ;
            try{
                Class.forName(driver);
                Connection connection =DriverManager.getConnection(url, DatabaseUser, DatabasePassword);   
                Statement statement = connection.createStatement();
                String qry="UPDATE "+tutors_ta+" SET password='"+npassword+"' WHERE username ='"+username+"'";
                statement.executeUpdate(qry);
                connection.close();
            }
            catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }        
        }
        out.println(PasswordChangeStatusMessage+"</center>");
        out.println("<br><br>");
         
    
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
