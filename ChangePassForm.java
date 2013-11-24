/*
 * ChangePassForm.java
 *
 * Created on July 7, 2005, 1:30 PM
 */

package LearningCenter;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.text.*;


public class ChangePassForm extends HttpServlet {
    
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
        String username=(String) session.getAttribute("username");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>password change form</title>");
        out.println("</head>");
        out.println("<body>");
        Calendar cal_c=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf_c=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        out.print("<br><center><strong><font color=#0066FF>"+sdf_c.format(cal_c.getTime())+"</font></strong></center><br>");
        
        out.println("<br>");
         out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"Tutoring.jsp \">");  
         out.println("<td><input type=\"submit\" value=\"back\"></FORM>");
        
        out.println("<FORM METHOD=\"POST\" ACTION=\"logout \">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div>");
         //out.println("<br><br>");
         
         out.println("<center><FORM METHOD=\"POST\" ACTION=\"changepass \">");
         out.println("<strong>change password</strong>");
         out.print("<pre>make sure your username is correct. DO NOT change your username;                               </pre>");
         out.print("<pre>new password must be at least 4 characters long, containing at least one letter and one number;</pre>");
         out.print("<pre>new password should be relatively hard to guess and contain no more than 20 characters.        </pre>");
         out.println("<TABLE>");
         out.println("<TR><TD align=\"right\">your username:</TD><TD align=\"center\"><INPUT TYPE=\"TEXT\" maxlength=\"20\" width=\"40\" Name=\"usernamet\" value=\""+username+"\">");
         out.println("<TR><TD align=\"right\">type old password:</TD><TD align=\"center\"> <INPUT TYPE=\"PASSWORD\" maxlength=\"20\" width=\"40\" NAME=\"opassword\">");
         out.println("<TR><TD align=\"right\">type new password: </TD><TD align=\"center\"><INPUT TYPE=\"PASSWORD\" maxlength=\"20\" width=\"40\" NAME=\"npassword\">");
         out.println("<TR><TD align=\"right\">retype new password: </TD><TD align=\"center\"><INPUT TYPE=\"PASSWORD\" maxlength=\"20\" width=\"40\" NAME=\"npasswordc\">");
         out.println("<TR>&nbsp;<TD></TD><TD align=\"left\" ><INPUT TYPE=\"SUBMIT\" VALUE=\"click to submit change\">");

         out.println("</Table></FORM></center>");
         out.print("</body></html>");
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
