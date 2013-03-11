/*
 * GenerateReports.java
 *
 * Created on July 1, 2005, 8:46 PM
 */

package LearningCenter;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.text.*;
import java.util.regex.*;
/**
 *
 * @author  Jinbo Lu
 * @version
 */
public class GenerateReports extends HttpServlet {
    
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
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Reports</title>");
        out.println("</head>");
        out.println("<body>");
         
        String DatabaseUser=(String)session.getAttribute("DatabaseUser");
        String DatabasePassword=(String)session.getAttribute("DatabasePassword");
        String url=(String)session.getAttribute("url");
        String driver=(String)session.getAttribute("driver");
        String tutors_ta=(String)session.getAttribute("tutors_ta");
        String records_ta=(String)session.getAttribute("records_ta");
        String tutees_ta=(String)session.getAttribute("tutees_ta");
        Calendar cal_today=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat _sdf_=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        java.util.Date tomm=new java.util.Date(cal_today.getTimeInMillis()+24*60*60*1000);
        Calendar cal_tomm=Calendar.getInstance(TimeZone.getDefault());
        cal_tomm.setTime(tomm);
        
        Calendar cal_c=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf_c=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        out.print("<br><center><strong><font color=#0066FF>"+sdf_c.format(cal_c.getTime())+"</font></strong></center><br>");
        out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"Tutoring.jsp \">" );
        out.println("<td><input type=\"submit\" value=\"back\"></FORM>");
        out.println("<FORM METHOD=\"POST\" ACTION=\"logout \">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div><br>");
        try{
            Class.forName(driver);
            Connection con =DriverManager.getConnection(url, DatabaseUser, DatabasePassword);
            Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stmt.executeQuery("SELECT * FROM "+records_ta+" ORDER BY course_number, professor");
        if(request.getParameter("begin_m")!=null && request.getParameter("begin_d")!=null &&request.getParameter("begin_y")!=null &&
              request.getParameter("end_m")!=null &&request.getParameter("end_d")!=null &&request.getParameter("end_y")!=null)    {
                  String begin_date_str=request.getParameter("begin_m")+"/"+request.getParameter("begin_d")+"/"+request.getParameter("begin_y")
                                    +" 01:00:00 AM";
                  String end_date_str=request.getParameter("end_m")+"/"+request.getParameter("end_d")+"/"+request.getParameter("end_y")
                                    +" 01:00:00 AM";
                  SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                  java.util.Date begin_date=sdf.parse(begin_date_str);
                  java.util.Date end_date=sdf.parse(end_date_str);
                  Calendar cal_begin=Calendar.getInstance(TimeZone.getDefault());
                  Calendar cal_end=Calendar.getInstance(TimeZone.getDefault());
                  cal_begin.setTime(begin_date);cal_end.setTime(end_date);
                  
                  if(begin_date.compareTo(end_date)<0){
                if(request.getParameter("check")!=null){
                    out.print("<br><center>"+(String)request.getParameter("generalquery")+": <font color=blue>" +
                    "no time limits; however, end_date>start_date must be specified</font></center><br>");
                    String gen_qry=(String)request.getParameter("generalquery");
                    Statement gen_stmt=con.createStatement();
                    ResultSet gen_rs=gen_stmt.executeQuery(gen_qry);
                    ResultSetMetaData gen_md=gen_rs.getMetaData();
                    out.println("<center><table border=\"1\"><tr>");
                    for(int i=1;i<=gen_md.getColumnCount();i++){
                        out.println("<td>"+gen_md.getColumnName(i));
                    }
                    while(gen_rs.next()){
                        out.println("<tr>");
                        for(int i=1;i<=gen_md.getColumnCount();i++){
                        out.println("<td>"+gen_rs.getString(i));
                    }}
                    out.println("</table></center>");
                }
        else{
            if(request.getParameter("whichquery")!=null){
                String whichq=(String)request.getParameter("whichquery");whichq.trim();
                if(whichq.length()>0){
                    if(whichq.equals("query0")){
                        out.println("<br><center>Between <strong><font color=#3299CC> "+_sdf_.format(cal_begin.getTime())+"</font></strong> and <strong><font color=#3299CC>"+_sdf_.format(cal_end.getTime())+"</font></strong></center><br>");
                        out.println("<center><table border=\"1\"><tr><td>visits_number<td>id<td>course_number<td>professor<td>tutored_by<td>start_recorded_by<td>end_recorded_by");
                        while(rs.next()){
                                            
                                        String s_t=rs.getString("start_time");
                                        if(s_t!=null){
                                            SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            java.util.Date s_ddd=_sdf.parse(s_t);
                                            Calendar cal_s=Calendar.getInstance(TimeZone.getDefault());
                                           cal_s.setTime(s_ddd);
                                           if(s_ddd.compareTo(begin_date)>=0 && s_ddd.compareTo(end_date)<=0){
                                              out.println("<tr><td>"+rs.getString("visits_number")+"<td>"+rs.getString("ID")+"<td>"+rs.getString("course_number")+"<td>"
                                              +rs.getString("professor")+"<td>"+rs.getString("tutored_by")+"<td>"+rs.getString("s_recorded_by")+"<td>"+rs.getString("e_recorded_by")) ;
                                               }}
                                    
                                }
                        out.println("</table></center>");
                        rs.beforeFirst();
                    }
                    //query1: stats general: in given period: # of students, visits and hours
                    else if(whichq.equals("query1")){
                        //out.println("<br>Between "+begin_date_str+" and "+end_date_str+"<br>");
                        out.println("<br><center>Between <strong><font color=#3299CC> "+_sdf_.format(cal_begin.getTime())+"</font></strong> and <strong><font color=#3299CC>"+_sdf_.format(cal_end.getTime())+"</font></strong></center><br>");
                        String qry_2="SELECT DISTINCT ID FROM "+records_ta;
                            Statement stmt_2=con.createStatement();
                            ResultSet rs_2=stmt_2.executeQuery(qry_2);
                            int num_of_s=0,num_of_v=0,counted_time_v=0,total_t=0;
                            //total#tutees,visits,time in period, a visit is counted if its start_time in period
                            //a counted_time_v is counted if the end_time>start_time and on the same day, end_time<=end of period
                            //begin_date at 1am of begin day, end_date at 11 pm of end day
                            while(rs_2.next()){
                                String id=rs_2.getString("ID");int ind_num_of_v=0;
                                while(rs.next()){
                                    
                                    if(rs.getString("ID").equals(id)){
                                        
                                        String s_t=rs.getString("start_time");
                                        if(s_t!=null){
                                            SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            java.util.Date s_ddd=_sdf.parse(s_t);
                                            Calendar cal_s=Calendar.getInstance(TimeZone.getDefault());
                                           cal_s.setTime(s_ddd);
                                           if(s_ddd.compareTo(begin_date)>=0 && s_ddd.compareTo(end_date)<=0){
                                               num_of_v++;ind_num_of_v++;
                                               String e_t=rs.getString("end_time");
                                               if(e_t!=null){
                                        
                                           java.util.Date e_ddd=_sdf.parse(e_t);
                                           
                                           Calendar cal_e=Calendar.getInstance(TimeZone.getDefault());
                                           cal_e.setTime(e_ddd);
                                           
                                          if(s_ddd.compareTo(e_ddd)<0){ if(cal_e.get(cal_e.DAY_OF_MONTH)==cal_s.get(cal_s.DAY_OF_MONTH) && 
                                          cal_e.get(cal_e.MONTH)==cal_s.get(cal_s.MONTH) && cal_e.get(cal_e.YEAR)==cal_s.get(cal_s.YEAR)
                                            &&  e_ddd.compareTo(end_date)<=0){
                                           total_t+=(int)Math.ceil((double)(cal_e.getTimeInMillis()-cal_s.getTimeInMillis())/(1000*60));
                                               counted_time_v++;
                                           }}
                                        }}}
                                    }
                                }
                                if(ind_num_of_v>0){num_of_s++;}
                                rs.beforeFirst();
                            }
                            int total_h=(int)Math.floor((double)total_t/60);int total_m=total_t-total_h*60;
                            out.println("<br>number of tutees: "+num_of_s+"<br>number of visits: " +
                            num_of_v+"<br>total time: "+total_h+" hrs "+total_m+" mins<br><br>MESSAGE: "+(num_of_v-counted_time_v)+" visit(s) is/are not counted" +
                            " toward the computation of total time due to errors or incompleteness in records.<br>");
                        
                    }
                    //if query2: stats per professor in given period: # of students, visits and hours
                    
                    else  if(whichq.equals("query2")){
                        String qry_1="SELECT DISTINCT professor FROM "+records_ta+" ORDER BY professor";
                        Statement stmt_1=con.createStatement();
                        ResultSet rs_1=stmt_1.executeQuery(qry_1);
                        //out.println("<br>Between "+begin_date_str+" and "+end_date_str+"<br>");
                        out.println("<br><center>Between <strong><font color=#3299CC> "+_sdf_.format(cal_begin.getTime())+"</font></strong> and <strong><font color=#3299CC>"+_sdf_.format(cal_end.getTime())+"</font></strong></center><br>");
                        out.println("<center><table border=\"1\"><tr><td>professor<td>number_of_students<td>number_of_visits<td>total_time<td>zero_is_perfect");
                        while(rs_1.next()){
                            String prof=rs_1.getString("professor");
                            String qry_2="SELECT DISTINCT ID FROM "+records_ta+" WHERE professor='"+prof+"'";
                            Statement stmt_2=con.createStatement();
                            ResultSet rs_2=stmt_2.executeQuery(qry_2);
                            int num_of_s=0,num_of_v=0,counted_time_v=0,total_t=0;
                            //total#tutees,visits,time in period, a visit is counted if its start_time in period
                            //a counted_time_v is counted if the end_time>start_time and on the same day, end_time<=end of period
                            //begin_date at 1am of begin day, end_date at 11 pm of end day
                            while(rs_2.next()){
                                String id=rs_2.getString("ID");int ind_num_of_v=0;
                                while(rs.next()){
                                    
                                    if(rs.getString("professor").equals(prof)&&rs.getString("ID").equals(id)){
                                        
                                        String s_t=rs.getString("start_time");
                                        if(s_t!=null){
                                            SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            java.util.Date s_ddd=_sdf.parse(s_t);
                                            Calendar cal_s=Calendar.getInstance(TimeZone.getDefault());
                                           cal_s.setTime(s_ddd);
                                           if(s_ddd.compareTo(begin_date)>=0 && s_ddd.compareTo(end_date)<=0){
                                               num_of_v++;ind_num_of_v++;
                                               String e_t=rs.getString("end_time");
                                               if(e_t!=null){
                                        
                                           java.util.Date e_ddd=_sdf.parse(e_t);
                                           
                                           Calendar cal_e=Calendar.getInstance(TimeZone.getDefault());
                                           cal_e.setTime(e_ddd);
                                           
                                          if(s_ddd.compareTo(e_ddd)<0){ if(cal_e.get(cal_e.DAY_OF_MONTH)==cal_s.get(cal_s.DAY_OF_MONTH) && 
                                          cal_e.get(cal_e.MONTH)==cal_s.get(cal_s.MONTH) && cal_e.get(cal_e.YEAR)==cal_s.get(cal_s.YEAR)
                                            &&  e_ddd.compareTo(end_date)<=0){
                                           total_t+=(int)Math.ceil((double)(cal_e.getTimeInMillis()-cal_s.getTimeInMillis())/(1000*60));
                                               counted_time_v++;
                                           }}
                                        }}}
                                    }
                                }
                                if(ind_num_of_v>0){num_of_s++;}
                                rs.beforeFirst();
                            }
                            int total_h=(int)Math.floor((double)total_t/60);int total_m=total_t-total_h*60;
                            if(num_of_v>0){out.println("<tr><td>"+prof+"<td>"+num_of_s+"<td>"+num_of_v+"<td>"+total_h+" hrs "+total_m+" mins<td>"+(num_of_v-counted_time_v));
                            }}
                       out.println("</table></center>"); 
                    }
                    //query3: stats per course_number in given period: # of students, visits and hours
                    else if(whichq.equals("query3")){
                        String qry_1="SELECT DISTINCT course_number FROM "+records_ta+" ORDER BY course_number";
                        Statement stmt_1=con.createStatement();
                        ResultSet rs_1=stmt_1.executeQuery(qry_1);
                        //out.println("<br>Between "+begin_date_str+" and "+end_date_str+"<br>");
                        out.println("<br><center>Between <strong><font color=#3299CC> "+_sdf_.format(cal_begin.getTime())+"</font></strong> and <strong><font color=#3299CC>"+_sdf_.format(cal_end.getTime())+"</font></strong></center><br>");
                        out.println("<center><table border=\"1\"><tr><td>course_number<td>number_of_students<td>number_of_visits<td>total_time<td>zero_is_perfect");
                        while(rs_1.next()){
                            String course=rs_1.getString("course_number");
                            String qry_2="SELECT DISTINCT ID FROM "+records_ta+" WHERE course_number='"+course+"'";
                            Statement stmt_2=con.createStatement();
                            ResultSet rs_2=stmt_2.executeQuery(qry_2);
                            int num_of_s=0,num_of_v=0,counted_time_v=0,total_t=0;
                            while(rs_2.next()){
                                String id=rs_2.getString("ID");int ind_num_of_v=0;
                                while(rs.next()){
                                    
                                    if(rs.getString("course_number").equals(course)&&rs.getString("ID").equals(id)){
                                        
                                        String s_t=rs.getString("start_time");
                                        if(s_t!=null){
                                            SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            java.util.Date s_ddd=_sdf.parse(s_t);
                                            Calendar cal_s=Calendar.getInstance(TimeZone.getDefault());
                                           cal_s.setTime(s_ddd);
                                           if(s_ddd.compareTo(begin_date)>=0 && s_ddd.compareTo(end_date)<=0){
                                               num_of_v++;ind_num_of_v++;
                                               String e_t=rs.getString("end_time");
                                               if(e_t!=null){
                                        
                                           java.util.Date e_ddd=_sdf.parse(e_t);
                                           
                                           Calendar cal_e=Calendar.getInstance(TimeZone.getDefault());
                                           cal_e.setTime(e_ddd);
                                           
                                          if(s_ddd.compareTo(e_ddd)<0){ if(cal_e.get(cal_e.DAY_OF_MONTH)==cal_s.get(cal_s.DAY_OF_MONTH) && 
                                          cal_e.get(cal_e.MONTH)==cal_s.get(cal_s.MONTH) && cal_e.get(cal_e.YEAR)==cal_s.get(cal_s.YEAR)
                                            &&  e_ddd.compareTo(end_date)<=0){
                                           total_t+=(int)Math.ceil((double)(cal_e.getTimeInMillis()-cal_s.getTimeInMillis())/(1000*60));
                                               counted_time_v++;
                                           }}
                                        }}}
                                    }
                                }
                                if(ind_num_of_v>0){num_of_s++;}
                                rs.beforeFirst();
                            }
                            int total_h=(int)Math.floor((double)total_t/60);int total_m=total_t-total_h*60;
                            if(num_of_v>0){out.println("<tr><td>"+course+"<td>"+num_of_s+"<td>"+num_of_v+"<td>"+total_h+" hrs "+total_m+" mins<td>"+(num_of_v-counted_time_v));
                            }}
                       out.println("</table></center>");
                    }
                    //query 4: stats per tutee in given period: # of courses, visits and hours
                    else if(whichq.equals("query4")){
                        String qry_1="SELECT DISTINCT ID FROM "+records_ta+ " ORDER BY ID";
                        Statement stmt_1=con.createStatement();
                        ResultSet rs_1=stmt_1.executeQuery(qry_1);
                        //out.println("<br>Between "+begin_date_str+" and "+end_date_str+"<br>");
                        out.println("<br><center>Between <strong><font color=#3299CC> "+_sdf_.format(cal_begin.getTime())+"</font></strong> and <strong><font color=#3299CC>"+_sdf_.format(cal_end.getTime())+"</font></strong></center><br>");
                        out.println("<center><table border=\"1\"><tr><td>ID<td>number_of_courses<td>number_of_visits<td>total_time<td>zero_is_perfect");
                        while(rs_1.next()){
                            String id=rs_1.getString("ID");
                            String qry_2="SELECT DISTINCT course_number FROM "+records_ta+" WHERE ID='"+id+"'";
                            Statement stmt_2=con.createStatement();
                            ResultSet rs_2=stmt_2.executeQuery(qry_2);
                            int num_of_c=0,num_of_v=0,counted_time_v=0,total_t=0;
                            while(rs_2.next()){
                                String course=rs_2.getString("course_number");int ind_num_of_v=0;
                                while(rs.next()){
                                    
                                    if(rs.getString("course_number").equals(course)&&rs.getString("ID").equals(id)){
                                        
                                        String s_t=rs.getString("start_time");
                                        if(s_t!=null){
                                            SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            java.util.Date s_ddd=_sdf.parse(s_t);
                                            Calendar cal_s=Calendar.getInstance(TimeZone.getDefault());
                                           cal_s.setTime(s_ddd);
                                           if(s_ddd.compareTo(begin_date)>=0 && s_ddd.compareTo(end_date)<=0){
                                               num_of_v++;ind_num_of_v++;
                                               String e_t=rs.getString("end_time");
                                               if(e_t!=null){
                                        
                                           java.util.Date e_ddd=_sdf.parse(e_t);
                                           
                                           Calendar cal_e=Calendar.getInstance(TimeZone.getDefault());
                                           cal_e.setTime(e_ddd);
                                           
                                          if(s_ddd.compareTo(e_ddd)<0){ if(cal_e.get(cal_e.DAY_OF_MONTH)==cal_s.get(cal_s.DAY_OF_MONTH) && 
                                          cal_e.get(cal_e.MONTH)==cal_s.get(cal_s.MONTH) && cal_e.get(cal_e.YEAR)==cal_s.get(cal_s.YEAR)
                                            &&  e_ddd.compareTo(end_date)<=0){
                                           total_t+=(int)Math.ceil((double)(cal_e.getTimeInMillis()-cal_s.getTimeInMillis())/(1000*60));
                                               counted_time_v++;
                                           }}
                                        }}}
                                    }
                                }
                                if(ind_num_of_v>0){num_of_c++;}
                                rs.beforeFirst();
                            }
                            int total_h=(int)Math.floor((double)total_t/60);int total_m=total_t-total_h*60;
                            if(num_of_v>0){out.println("<tr><td>"+id+"<td>"+num_of_c+"<td>"+num_of_v+"<td>"+total_h+" hrs "+total_m+" mins<td>"+(num_of_v-counted_time_v));}
                        }
                       out.println("</table></center>");
                    }
                    //general stats, weekly time series between two days in a year
                    else if(whichq.equals("query5")){
                        if(cal_begin.get(cal_begin.YEAR)==cal_end.get(cal_end.YEAR)){
                           //String _cal_begin=cal_begin.get(cal_begin.MONTH)+"/"+cal_begin.get(cal_begin.DAY_OF_MONTH)+"/"+cal_begin.get(cal_begin.YEAR);
                           //String _cal_end=cal_end.get(cal_end.MONTH)+"/"+cal_end.get(cal_end.DAY_OF_MONTH)+"/"+cal_end.get(cal_end.YEAR);
                          // out.println("<br>Between "+_cal_begin+" and "+_cal_end+"<br>");
                           out.println("<br><center>Between <strong><font color=#3299CC> "+_sdf_.format(cal_begin.getTime())+"</font></strong> and <strong><font color=#3299CC>"+_sdf_.format(cal_end.getTime())+"</font></strong></center><br>");
                           int count_begin=cal_begin.get(cal_begin.DAY_OF_YEAR);
                           int count_end=cal_end.get(cal_end.DAY_OF_YEAR);
                           out.println("<center><table border=\"1\"><tr><td>week<td>number_of_tutees<td>number_of_visits<td>total_time<td>zero_is_perfect");
                           for(int i=0;count_begin+7*i<count_end;i++){
                               int wk_begin=count_begin+7*i;int wk_end=wk_begin+7;
                               String qry_2="SELECT DISTINCT ID FROM "+records_ta;
                            Statement stmt_2=con.createStatement();
                            ResultSet rs_2=stmt_2.executeQuery(qry_2);
                            int num_of_s=0,num_of_v=0,counted_time_v=0,total_t=0;
                           
                            while(rs_2.next()){
                                String id=rs_2.getString("ID");int ind_num_of_v=0;
                                while(rs.next()){
                                    
                                    if(rs.getString("ID").equals(id)){
                                        
                                        String s_t=rs.getString("start_time");
                                        if(s_t!=null){
                                            SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            java.util.Date s_ddd=_sdf.parse(s_t);
                                            Calendar cal_s=Calendar.getInstance(TimeZone.getDefault());
                                           cal_s.setTime(s_ddd);
                                           if(cal_s.get(cal_s.YEAR)==cal_begin.get(cal_begin.YEAR)&&cal_s.get(cal_s.DAY_OF_YEAR)>=wk_begin 
                                                                           && cal_s.get(cal_s.DAY_OF_YEAR)<wk_end){
                                               num_of_v++;ind_num_of_v++;
                                               String e_t=rs.getString("end_time");
                                               if(e_t!=null){
                                        
                                           java.util.Date e_ddd=_sdf.parse(e_t);
                                           
                                           Calendar cal_e=Calendar.getInstance(TimeZone.getDefault());
                                           cal_e.setTime(e_ddd);
                                           
                                          if(s_ddd.compareTo(e_ddd)<0){ if(cal_e.get(cal_e.DAY_OF_YEAR)==cal_s.get(cal_s.DAY_OF_YEAR)/*cal_e.get(cal_e.DAY_OF_MONTH)==cal_s.get(cal_s.DAY_OF_MONTH) && 
                                          cal_e.get(cal_e.MONTH)==cal_s.get(cal_s.MONTH) && cal_e.get(cal_e.YEAR)==cal_s.get(cal_s.YEAR)
                                            &&  e_ddd.compareTo(end_date)<=0*/){
                                           total_t+=(int)Math.ceil((double)(cal_e.getTimeInMillis()-cal_s.getTimeInMillis())/(1000*60));
                                               counted_time_v++;
                                           }}
                                        }}}
                                    }
                                }
                                if(ind_num_of_v>0){num_of_s++;}
                                rs.beforeFirst();
                            }
                            int total_h=(int)Math.floor((double)total_t/60);int total_m=total_t-total_h*60;
                            out.println("<tr><td>"+(i+1)+"<td>"+num_of_s+"<td>"+num_of_v+"<td>"+total_h+" hrs "+total_m+" mins<td>"+(num_of_v-counted_time_v));
                           }
                           out.println("</table></center>");
                        }
                        else{out.println("<br><font color=red>Please make sure the two dates are in the same year.</font><br>");}
                    }
                    else{/*queries 6 are for future use need to add selections to query choices*/}
                }
            }
        }}
            else{out.println("<br><font color=red>Please enter the period for your reports and make sure end_date>begin_date</font><br>");}
        }
            else{out.println("<br><font color=red>Please enter the period for your reports and make sure end_date>begin_date</font><br>");}
            con.close();
        }
        catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }
        //produce html form
        String inputs="<SELECT name=\"whichquery\"><option value=\"query0\">raw data between selected dates";
        /*for(int i=0;i<5;i++){
            inputs=inputs+ "<option value=\"query"+(i+1)+"\">query"+" "+(i+1);
        }*/
        inputs=inputs+"<option value=\"query1\">general stats between selected dates";
        inputs=inputs+"<option value=\"query2\">stats between selected dates per prof.";
        inputs=inputs+"<option value=\"query3\">stats between selected dates per course";
        inputs=inputs+"<option value=\"query4\">stats between selected dates per student";
        inputs=inputs+"<option value=\"query5\">weekly time series between selected dates";
        inputs=inputs+"</Select>";
        String begin="<input size=2 maxlength=2 name=\"begin_m\" type=\"TEXT\" value=\""+(cal_today.get(cal_today.MONTH)+1)+"\">";
        begin+="-"+"<input size=2 maxlength=2 name=\"begin_d\" type=\"TEXT\" value=\""+cal_today.get(cal_today.DAY_OF_MONTH)+"\">";
        begin+="-"+"<input size=4 maxlength=4 name=\"begin_y\" type=\"TEXT\" value=\""+cal_today.get(cal_today.YEAR)+"\">";
        //begin+="(mm/dd/yyyy)";
        String end="<input size=2 maxlength=2 name=\"end_m\" type=\"TEXT\" value=\""+(cal_tomm.get(cal_tomm.MONTH)+1)+"\">";
        end+="-"+"<input size=2 maxlength=2 name=\"end_d\" type=\"TEXT\" value=\""+cal_tomm.get(cal_tomm.DAY_OF_MONTH)+"\">";
        end+="-"+"<input size=4 maxlength=4 name=\"end_y\" type=\"TEXT\"  value=\""+cal_tomm.get(cal_tomm.YEAR)+"\">";
        //end+="(mm/dd/yyyy)";
        /*String begin="<Select name=\"begin_m\">";
        for(int i=1;i<=12;i++){
            begin=begin+"<option value=\""+i+"\">"+i;
        }
        begin=begin+"</Select>"+"<Select name=\"begin_d\">";
        for(int i=1;i<=31;i++){
            begin=begin+"<option value=\""+i+"\">"+i;
        }
        begin=begin+"</Select>"+"<input size=5 maxlength=4 name=\"begin_y\" type=\"TEXT\" value=\"2005\">";
        String end="<Select name=\"end_m\">";
        for(int i=1;i<=12;i++){
            end=end+"<option value=\""+i+"\">"+i;
        }
        end=end+"</Select>"+"<Select name=\"end_d\">";
        for(int i=1;i<=31;i++){
            end=end+"<option value=\""+i+"\">"+i;
        }
        end=end+"</Select>"+"<input size=5 maxlength=4 name=\"end_y\" type=\"TEXT\" value=\"2005\">";*/
        out.print("<hr><form method=POST action=\"generatereports\">"
        +"Please choose a query and pick the begin and end dates for the query:<br><br>"
        +"<table><tr><td>begin date<td>end date<td>preset queries"
        +"<tr><td>"+begin+"<td>"+end+"<td>"+inputs +
        "<tr><td> (mm-dd-yyyy)<td> (mm-dd-yyyy)<td></table><br>"+
        "<input type=\"submit\" value=\"CLICK FOR QUERY RESULTS\"><br><br>"+
        "<input name=\"check\" type=\"CHECKBOX\">Check this box if you want to query using an SQL string (Please enter in the textarea below)<br>"+
        "<TEXTAREA name=\"generalquery\" ROWS=3 COLS=70>SELECT * FROM tut_records</TEXTAREA></form><hr>");//text for dates
        //info about tables anf fields
        out.println("tables and fields :<br>");
        out.println("The fields in the table <strong>tut_records</strong> are:visits_number, ID, course_number, professor, start_time, end_time, tutored_by," +
        "s_recorded_by, e_recorded_by;<br>The fields in the table <strong>tutors</strong> are: username, password, fname, lname, status, start_date, end_date;" +
        "<br>The fields in the table <strong>tutees</strong> are: ID and password;<br>The fields in the table <strong>courses</strong> are: course_number and professor.");
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
