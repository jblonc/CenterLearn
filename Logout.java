/*
 * Logout.java
 *
 * Created on July 7, 2005, 1:40 AM
 */

package LearningCenter;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Jinbo Lu
 * @version
 */
public class Logout extends HttpServlet {
    
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
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN>\"");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>logout</title>");
        out.println("<meta http-equiv=\"REFRESH\" content=\"0;url=begin.html\"> ");
        out.println("</head>");
        out.println("<body>");
        out.print("<br><br>");
        
        
        //out.println("<a href="+CourseHome+">Click here to go back to the course home.</a>");}
        out.println("</body>");
        out.println("</html>");
         
        out.close();
        session.removeAttribute("username");
        session.removeAttribute("password");
        session.removeAttribute("driver");session.removeAttribute("url");
        session.removeAttribute("DatabaseUser");
        session.removeAttribute("DatabasePassword");
        session.removeAttribute("tutors_ta");session.removeAttribute("records_ta");session.removeAttribute("tutees_ta");
        //session.removeAttribute("TimeOut");//is this needed
        
        //RequestDispatcher view= request.getRequestDispatcher("begin.jsp");
               // view.forward(request,response);
                session.invalidate();
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
