<html>
<head>
<title>updating records</title>
<meta http-equiv="Cache-control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache">
<meta http-equiv="Expires" CONTENT="-1">

</head>

<body>
<%@ page import="java.io.*,java.util.*,java.sql.*,java.text.*,java.util.regex.*,java.net.*"%>


<%String DatabaseUser=(String)session.getAttribute("DatabaseUser");
  String DatabasePassword=(String)session.getAttribute("DatabasePassword");
  String url=(String)session.getAttribute("url");
  String driver=(String)session.getAttribute("driver");
  String tutors_ta=(String)session.getAttribute("tutors_ta");
  String records_ta=(String)session.getAttribute("records_ta");
  String tutees_ta=(String)session.getAttribute("tutees_ta");
  String logonUSER=(String)session.getAttribute("username");
  String logonPASS=(String)session.getAttribute("password");
  String TutorDirector_Query="SELECT fname,lname FROM "+tutors_ta+" WHERE status LIKE '%tor'";//tutor and director end with 'tor'
  Vector TutorDirectorList= new Vector(0); int TutorDirectorCount=0;
  Calendar cal=Calendar.getInstance(TimeZone.getDefault());
Calendar cal_c=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf_c=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        out.print("<br><center><strong><font color=#0066FF>"+sdf_c.format(cal_c.getTime())+"</font></strong></center><br>");
out.println("<br>");
         out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"Tutoring.jsp \">");  
         out.println("<td><input type=\"submit\" value=\"back\"></FORM>");
        
        out.println("<FORM METHOD=\"POST\" ACTION=\"logout \">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div>");
try{

Class.forName(driver);
   Connection connection = DriverManager.getConnection(url,DatabaseUser,DatabasePassword);

Statement statement_TutDir=connection.createStatement();
   ResultSet resultSet_TutDir=statement_TutDir.executeQuery(TutorDirector_Query);
   while(resultSet_TutDir.next()){TutorDirectorList.add(resultSet_TutDir.getString("fname")+
             "_"+resultSet_TutDir.getString("lname"));TutorDirectorCount++;}
String incomplete_Query = "SELECT * FROM "+records_ta;
Statement statement_fill=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
ResultSet resultSet_fill=statement_fill.executeQuery(incomplete_Query);
while(resultSet_fill.next()){
  String key=resultSet_fill.getString("visits_number");
  if(request.getParameter(key)!=null){
  String temp=(String)request.getParameter(key);temp.trim();
  if(temp.length()>0 &&request.getParameter(key+"e_check")!=null&& request.getParameter(key+"e_hour")!=null 
       && request.getParameter(key+"e_hour")!=null && request.getParameter(key+"e_minute")!=null
       && request.getParameter(key+"e_AP")!=null && request.getParameter(key+"tutored_by")!=null
       && request.getParameter(key+"e_recorded_by")!=null){
   String stt_time=resultSet_fill.getString("start_time");
   String etime=(String)request.getParameter(key+"e_hour")+":"+(String)request.getParameter(key+"e_minute")
               +":00 "+(String)request.getParameter(key+"e_AP");
   SimpleDateFormat sdf_tt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   java.util.Date edate=sdf_tt.parse(stt_time);
   Calendar cal_end=Calendar.getInstance(TimeZone.getDefault());
   cal_end.setTime(edate);
   etime= (cal_end.get(cal_end.MONTH)+1)+"/"+cal_end.get(cal_end.DAY_OF_MONTH)+"/"+cal_end.get(cal_end.YEAR)+" "+etime;
   SimpleDateFormat sdf_=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
   java.util.Date s_TIME=sdf_tt.parse(stt_time);//start_time printout is different from database entry format
   java.util.Date e_TIME=sdf_.parse(etime);
   Timestamp ts=new Timestamp(e_TIME.getTime());
   //if(s_TIME.compareTo(e_TIME)<0){out.print("YES");}else{out.print("NO");}
   String etutor=(String)request.getParameter(key+"tutored_by");
   String erecordedBy=(String)request.getParameter(key+"e_recorded_by");
   
  /* String insertEnd_Query="UPDATE "+records_ta+ " SET end_time='"+etime+"',tutored_by='"+etutor
                            +"',e_recorded_by='"+erecordedBy+"' WHERE visits_number='"+key+"'";
   Statement insertEnd_statement=connection.createStatement();*/
  // out.print("<br>"+insertEnd_Query+"<br>");
  // if(s_TIME.compareTo(e_TIME)<0){insertEnd_statement.executeUpdate(insertEnd_Query);}
    Calendar cal_TIME=Calendar.getInstance(TimeZone.getDefault());
    cal_TIME.setTime(e_TIME);
   // java.sql.Date ee_date=new java.sql.Date(cal_TIME.getTimeInMillis());
    java.sql.Time ee_time=new java.sql.Time(cal_TIME.getTimeInMillis());
  
 if(s_TIME.compareTo(e_TIME)<0){
   resultSet_fill.updateString("tutored_by",etutor);
   //out.print(etime);
   //date only contains date; time contains time with the current date as date
   resultSet_fill.updateTimestamp("end_time",ts);
  // resultSet_fill.updateDate("end_time",ee_date); 
   resultSet_fill.updateString("e_recorded_by",erecordedBy);resultSet_fill.updateRow();
   }
   }
  }
}

%>
<form method=POST action="AdjustRecords.jsp">
<%

   
   Statement incomplete_statement=connection.createStatement();
   ResultSet resultSet_incomplete=incomplete_statement.executeQuery(incomplete_Query);
   //ResultSetMetaData resultSetMetaData_incomplete =resultSet_incomplete.getMetaData();
   boolean NoEndTime;int num_incomplete=0;String tut_records_key;String st_time;//start_time
   out.print("<br><center><table cellspacing=\"5\">");
   out.print("<tr><td><font color=#CC00FF>completed?</font><td>visits_number<td>ID<td>course_number<td>professor<td>start_time<td><font color=red>end_time</font><td><font color=#00CD00>tutored_by</font><td>");
   while(resultSet_incomplete.next()){
   NoEndTime=false;
   String rsmi=resultSet_incomplete.getString("end_time");
   if(rsmi!=null){rsmi.trim();if(rsmi.length()==0){NoEndTime=true;}}
   else{NoEndTime=true;}
   if(NoEndTime){num_incomplete++;tut_records_key=resultSet_incomplete.getString("visits_number");
                     st_time=resultSet_incomplete.getString("start_time");%>
                 <tr><input type="HIDDEN" Name= <%=tut_records_key%> value=<%=tut_records_key%> >
                 <tr><td><INPUT type="CHECKBOX" name=<%=tut_records_key+"e_check"%> value=<%=tut_records_key+"e_check"%>>
                     <td><%=tut_records_key%>
                     <td><%=resultSet_incomplete.getString("ID")%>
                     <td><%=resultSet_incomplete.getString("course_number")%>
                     <td><%=resultSet_incomplete.getString("professor")%>
                     <td><%=st_time%> 
                     <td><SELECT name=<%=tut_records_key+"e_hour"%>  ><option value=<%=cal.get(cal.HOUR)%>><%=cal.get(cal.HOUR)%>
                       <%for(int i=1;i<=12;i++){%><option value=<%=i%>><%=i%><%}%>
                      </SELECT>
                       <SELECT name=<%=tut_records_key+"e_minute"%>  ><option value=<%=cal.get(cal.MINUTE)%>><%=cal.get(cal.MINUTE)%>
                       <%for(int i=0;i<12;i++){%><option value=<%=i*5%>><%=i*5%><%}%>
                      </SELECT>
                      <%String e_AP;if(cal.get(cal.HOUR_OF_DAY)>=12){e_AP="PM";}else{e_AP="AM";}%>
                 <SELECT name=<%=tut_records_key+"e_AP"%>  ><option value=<%=e_AP%>><%=e_AP%><option value="AM">AM<option value="PM">PM</SELECT>
                       <%SimpleDateFormat sdf_t=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                         java.util.Date s_date=sdf_t.parse(st_time);
                         Calendar cal_start=Calendar.getInstance(TimeZone.getDefault());
                         cal_start.setTime(s_date);
                         out.print(cal_start.get(cal_start.YEAR)+ "-"+(cal_start.get(cal_start.MONTH)+1)+"-"+cal_start.get(cal_start.DAY_OF_MONTH));
                     %>
                     <td><SELECT name=<%=tut_records_key+"tutored_by"%>  ><option value="tutors">Choose a tutor<option value="tutors">tutors
                  <%for(int i=0;i<TutorDirectorCount;i++){%><option value=<%= TutorDirectorList.get(i)%> ><%=TutorDirectorList.get(i)%><%}%></SELECT>

                     <td><INPUT TYPE="HIDDEN" Name=<%=tut_records_key+"e_recorded_by"%>  value=<%=logonUSER%>>

   <%}
   }
   out.print("</table></center>");
   if(num_incomplete==0){out.print("<center><font color=blue>There are no records to display.</font></center><br>");}
%>
<br><center><input type="submit" value="adjust">&nbsp;&nbsp;&nbsp;<input type="reset" value="reset"></center>
</form>
<%
connection.close();
}
catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }
%>

</body></html>