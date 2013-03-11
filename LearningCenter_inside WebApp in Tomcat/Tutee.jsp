<html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">;
<head>
<title>student access</title>
<meta http-equiv="REFRESH" content="300;url=logout">
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
  String[] courses=(String[])session.getAttribute("courses");
  int actualNum=((Integer)session.getAttribute("actualNumOfCourses")).intValue();
  String[][] prof_options=new String[actualNum][];
  for(int i=0;i<actualNum;i++){prof_options[i]=courses[i].split("@");}
Calendar cal_c=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf_c=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        out.print("<br><center><strong><font color=#0066FF>"+sdf_c.format(cal_c.getTime())+"</font></strong></center><br>");

out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"changepassform_student\">");  
         out.println("<td><input type=\"submit\" value=\"change password\"></FORM>");
out.println("<FORM METHOD=\"POST\" ACTION=\"logout\">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div>");

 try{

Class.forName(driver);
   Connection connection = DriverManager.getConnection(url,DatabaseUser,DatabasePassword);

Statement statement_TutDir=connection.createStatement();
   ResultSet resultSet_TutDir=statement_TutDir.executeQuery(TutorDirector_Query);
   while(resultSet_TutDir.next()){TutorDirectorList.add(resultSet_TutDir.getString("fname")+
             "_"+resultSet_TutDir.getString("lname"));TutorDirectorCount++;}
String incomplete_Query = "SELECT * FROM "+records_ta+ " WHERE ID='"+logonUSER+"'";
Statement statement_fill=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
ResultSet resultSet_fill=statement_fill.executeQuery(incomplete_Query);
//String fill_Cursor=resultSet_fill.getCursorName();
while(resultSet_fill.next()){
  String key=resultSet_fill.getString("visits_number");
  if(request.getParameter(key)!=null){
  String temp=(String)request.getParameter(key);temp.trim();
  if(temp.length()>0 &&request.getParameter(key+"e_check")!=null && request.getParameter(key+"e_hour")!=null 
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
   
   /*String insertEnd_Query="UPDATE "+records_ta+ " SET end_time='"+etime+"' ,tutored_by='"+etutor
                            +"',e_recorded_by='"+erecordedBy+"' WHERE CURRENT of "+fill_Cursor; //WHERE visits_number='"+key+"'";
   Statement insertEnd_statement=connection.createStatement();*/
  // out.print("<br>"+insertEnd_Query+"<br>");
   //if(s_TIME.compareTo(e_TIME)<0){insertEnd_statement.executeUpdate(insertEnd_Query);}
    Calendar cal_TIME=Calendar.getInstance(TimeZone.getDefault());
    cal_TIME.setTime(e_TIME);
   // java.sql.Date ee_date=new java.sql.Date(cal_TIME.getTimeInMillis());
    java.sql.Time ee_time=new java.sql.Time(cal_TIME.getTimeInMillis());
   if(s_TIME.compareTo(e_TIME)<0){
   resultSet_fill.updateString("tutored_by",etutor);
   //out.print(etime);
   //date only contains date; time contains time with the current date as date
   resultSet_fill.updateTimestamp("end_time",ts);//ee_time
  // resultSet_fill.updateDate("end_time",ee_date); 
   resultSet_fill.updateString("e_recorded_by",erecordedBy);resultSet_fill.updateRow();
   }
   }
  }
}

//the following: entry of new visit
if(request.getParameter("course_number")!=null&& request.getParameter("professor")!=null
              &&request.getParameter("s_recorded_by")!=null){
 String cnum=(String)request.getParameter("course_number");cnum.trim();
 String prof=(String)request.getParameter("professor");prof.trim();
 String s_rec=request.getParameter("s_recorded_by");
 if(cnum.length()>0&&prof.length()>0&&s_rec.length()>0){
  String entry_t=(cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR);
  entry_t=entry_t+" "+(String)request.getParameter("s_hour")+":"+(String)request.getParameter("s_minute") 
                     +":00 "+(String)request.getParameter("s_AP");
  String entry_str="INSERT INTO "+records_ta+" (ID,course_number,professor,start_time,s_recorded_by) "
                                +"VALUES ('"+logonUSER+"','"+cnum+"','"+prof+"','"+entry_t+"','"+s_rec+"')";
  Statement st_etry=connection.createStatement();
  st_etry.executeUpdate(entry_str);
 }
}
%>
<form method=POST name=TuteeForm action="Tutee.jsp">
<%

   
   Statement incomplete_statement=connection.createStatement();
   ResultSet resultSet_incomplete=incomplete_statement.executeQuery(incomplete_Query);
   //ResultSetMetaData resultSetMetaData_incomplete =resultSet_incomplete.getMetaData();
   boolean NoEndTime;int num_incomplete=0;String tut_records_key;String st_time;//start_time
   out.print("<br>For these learning center visits, you should <u><span style=\"background-color: #FFFF00\">complete the end_times</span></u> when you leave:<br>A. check the box near a visit;<br>B. "+
              "choose end_time to be earlier than start_time for that visit;<br>C. click the <strong><font color=#00B2EE>record and register</font></strong> button.<br><br>");
   out.print("<center><table cellspacing=\"15\">");
   out.print("<tr><td align=\"center\"><strong><font color=#CC00FF><u>completed?</u></font></strong><td align=\"center\">visits_number<td>ID<td align=\"center\">course_number<td align=\"center\">professor<td align=\"center\">start_time<td align=\"center\"><font color=red>end_time</font><td align=\"center\"><font color=#00CD00>tutored_by</font><td>");
   while(resultSet_incomplete.next()){
   NoEndTime=false;
   String rsmi=resultSet_incomplete.getString("end_time");
   if(rsmi!=null){rsmi.trim();if(rsmi.length()==0){NoEndTime=true;}}
   else{NoEndTime=true;}
   if(NoEndTime){num_incomplete++;tut_records_key=resultSet_incomplete.getString("visits_number");
                     st_time=resultSet_incomplete.getString("start_time");%>
                 <tr><input type="HIDDEN" Name= <%=tut_records_key%> value=<%=tut_records_key%> >
                 <tr><td align="center" ><INPUT type="CHECKBOX" name=<%=tut_records_key+"e_check"%> value=<%=tut_records_key+"e_check"%>>
                     <td align="center"><%=tut_records_key%>
                     <td align="center"><%=resultSet_incomplete.getString("ID")%>
                     <td align="center"><%=resultSet_incomplete.getString("course_number")%>
                     <td align="center"><%=resultSet_incomplete.getString("professor")%>
                     <td align="center"><%=st_time%> 
                     <td align="center"><SELECT name=<%=tut_records_key+"e_hour"%>  ><option value=<%=cal.get(cal.HOUR)%>><%=cal.get(cal.HOUR)%>
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
                     <td align="center"><SELECT name=<%=tut_records_key+"tutored_by"%>  ><option value="tutors">Choose a tutor<option value="tutors">tutors
                  <%for(int i=0;i<TutorDirectorCount;i++){%><option value=<%= TutorDirectorList.get(i)%> ><%=TutorDirectorList.get(i)%><%}%></SELECT>

                     <td><INPUT TYPE="HIDDEN" Name=<%=tut_records_key+"e_recorded_by"%>  value=<%=logonUSER%>>

   <%}
   }
      
   out.print("</table></center>");
  if(num_incomplete==0){out.print("<center><font color=blue>There are no records to display.</font></center><br>");}

   %>
<br><center><input type="submit" value="record and register">&nbsp; &nbsp;&nbsp; <input type="reset" value="reset"></center><br>
To <u><span style="background-color: #FFFF00">register a new visit</span></u>:
<br>A. you must choose a course_number, professor of the course and time of the current date;
<br>B. click the <strong><font color=#00B2EE>record and register</font></strong> button. 
<br>You can register multiple visits by repeating the procedure.
<br><font color=red><strong>Remember to logon again to complete the end_time just before you leave the Learning Center.</strong></font>
<br><br>
<center><table >
<tr><td>Course #: <td><SELECT name="course_number" onChange="courseToProf()">
                 <option value=''>choose a course
                <% for(int i=0;i<actualNum;i++){%>
                <option value=<%= courses[i].substring(0,6)%>><%=courses[i].substring(0,6)%><%}%></SELECT>
<tr><td>Professor:<td><Select name="professor"><option value=''>choose the professor</select>
<tr><td>Start time:<td> <SELECT name="s_hour"><option value=<%=cal.get(cal.HOUR)%>><%=cal.get(cal.HOUR)%>
                       <%for(int i=1;i<=12;i++){%><option value=<%=i%>><%=i%><%}%>
                      </SELECT>
                       <SELECT name="s_minute"><option value=<%=cal.get(cal.MINUTE)%>><%=cal.get(cal.MINUTE)%>
                       <%for(int i=0;i<12;i++){%><option value=<%=i*5%>><%=i*5%><%}%>
                      </SELECT>
                      <%String s_AP;if(cal.get(cal.HOUR_OF_DAY)>=12){s_AP="PM";}else{s_AP="AM";}%>
                 <SELECT name="s_AP"><option value=<%=s_AP%>><%=s_AP%><option value="AM">AM<option value="PM">PM</SELECT>
                 <%=(cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)%>
<tr><td><td> <INPUT TYPE="HIDDEN" Name="s_recorded_by" value=<%=logonUSER%>>
</table></center>
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
<script language="javascript">
<!--
  var courseCol=document.TuteeForm.course_number;
  var profCol=document.TuteeForm.professor;
  function courseToProf(){
  for(i=1;i<profCol.length;i++){profCol.options[i]=null;}
  <%for(int i=0;i<actualNum;i++){for(int j=1;j<prof_options[i].length;j++){%>
  for(i=1;i<courseCol.length;i++){
  if(courseCol.options[i].selected==true && i==<%=i+1%>){
profCol.options[<%=j%>]=new Option( "<%=prof_options[i][j]%>" ,"<%=prof_options[i][j]%>");
}
   }<%}}%>
 }
//-->
</script>

</body></html>
