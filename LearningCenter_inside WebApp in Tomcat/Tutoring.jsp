<html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">;
<head>
<title>tutor access</title>
<meta http-equiv="REFRESH" content="600;url=Tutoring.jsp">
<meta http-equiv="Cache-control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache">
<meta http-equiv="Expires" CONTENT="-1">
</head>
<body>
<%@ page import="java.io.*,java.util.*,java.sql.*,java.text.*,java.util.regex.*,java.net.*"%>
<%String status=(String)session.getAttribute("status");
  String dirStatus=(String)session.getAttribute("dirStatus");
  String DatabaseUser=(String)session.getAttribute("DatabaseUser");
  String DatabasePassword=(String)session.getAttribute("DatabasePassword");
  String url=(String)session.getAttribute("url");
  String driver=(String)session.getAttribute("driver");
  String tutors_ta=(String)session.getAttribute("tutors_ta");
  String records_ta=(String)session.getAttribute("records_ta");
  String tutees_ta=(String)session.getAttribute("tutees_ta");
  String[] courses=(String[])session.getAttribute("courses");
  String logonUSER=(String)session.getAttribute("username");
  String logonPASS=(String)session.getAttribute("password");
  
  String generalQuery="SELECT * FROM "+records_ta;
  String TutorDirector_Query="SELECT fname,lname FROM "+tutors_ta+" WHERE status LIKE '%tor'";//tutor and director end with 'tor'
  Vector TutorDirectorList= new Vector(0); int TutorDirectorCount=0;
  int actualNum=((Integer)session.getAttribute("actualNumOfCourses")).intValue();
    String[][] prof_options=new String[actualNum][];
    
  
  for(int i=0;i<actualNum;i++){prof_options[i]=courses[i].split("@");}

  Calendar cal=Calendar.getInstance(TimeZone.getDefault());
 /* String newInsertionQuery = "INSERT INTO "+records_ta+" (ID,course_number)"//, course_number,professor,start_time,end_time,tutored_by,s_recorded_by}"
                             +" VALUES ('"+(String)request.getParameter("id")+"','"+(String)request.getParameter("cs")+"')";*/
  int columnCount_records_ta=0;
  Calendar cal_c=Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf_c=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        out.print("<br><center><strong><font color=#0066FF>"+sdf_c.format(cal_c.getTime())+"</font></strong></center><br>");

out.println("<div align=\"right\"><table><tr><FORM METHOD=\"POST\" ACTION=\"changepassform\">");  
         out.println("<td><input type=\"submit\" value=\"change password\"></FORM>");
out.println("<FORM METHOD=\"POST\" ACTION=\"logout\">");  
         out.println("<td><input type=\"submit\" value=\"logout\"></FORM></table></div>");
        
        
  try{
   Class.forName(driver);
   Connection connection = DriverManager.getConnection(url,DatabaseUser,DatabasePassword);
   
   Statement statement_TutDir=connection.createStatement();
   ResultSet resultSet_TutDir=statement_TutDir.executeQuery(TutorDirector_Query);
   while(resultSet_TutDir.next()){TutorDirectorList.add(resultSet_TutDir.getString("fname")+
             "_"+resultSet_TutDir.getString("lname"));TutorDirectorCount++;}//doesn't give right answer:out.print(TutorDirectorList.capacity());

   Statement statement_general=connection.createStatement();
   ResultSet resultSet_general=statement_general.executeQuery(generalQuery);
   ResultSetMetaData resultSetMetaData_general =resultSet_general.getMetaData();
   columnCount_records_ta=resultSetMetaData_general.getColumnCount();
   
   /*Hashtable HashedParameters=new Hashtable();*/
   String[] RequestedParameters=new String[columnCount_records_ta-1];
   for(int i=2;i<=columnCount_records_ta;i++){RequestedParameters[i-2]=(String)request.getParameter(resultSetMetaData_general.getColumnName(i));}
   //make sure in the tut_records table starting_time appears at 5th column; end_time at 6th column
   RequestedParameters[3]= (String)request.getParameter("s_day")+" "+(String)request.getParameter("s_hour")+":"+
                 (String)request.getParameter("s_minute")+":00 "+(String)request.getParameter("s_AP");
   RequestedParameters[4]= (String)request.getParameter("e_day")+" "+(String)request.getParameter("e_hour")+":"+
                 (String)request.getParameter("e_minute")+":00 "+(String)request.getParameter("e_AP");
   /*for(int i=2;i<=columnCount_records_ta;i++){HashedParameters.put(resultSetMetaData_general.getColumnName(i),RequestedParameters[i-2]);}
   String Fields="(";String Values="('";
   for(Enumeration e=HashedParameters.keys();e.hasMoreElements();){String ss=(String)e.nextElement();Fields=Fields+ss+",";Values=Values+HashedParameters.get(ss)+"','";}
   out.print(Fields+"<br>");out.print(Values+"<br>"); */
   /*String newInsertionQuery="INSERT INTO "+records_ta+" (";
   for(int i=2;i<columnCount_records_ta;i++)newInsertionQuery=newInsertionQuery+resultSetMetaData_general.getColumnName(i)+",";
   newInsertionQuery=newInsertionQuery+resultSetMetaData_general.getColumnName(columnCount_records_ta)+") VALUES ('";
   for(int i=0;i<columnCount_records_ta-2;i++)newInsertionQuery=newInsertionQuery+RequestedParameters[i]+"','";
   newInsertionQuery= newInsertionQuery+RequestedParameters[columnCount_records_ta-2]+"')";
   Statement statement_insertion = connection.createStatement();*/
   /*if(request.getParameter("ID")!=null && request.getParameter("course_number")!=null && 
      request.getParameter("professor")!=null && request.getParameter("tutored_by")!=null)
    {out.print(newInsertionQuery);statement_insertion.executeUpdate(newInsertionQuery);}*/
   if(request.getParameter("ID")!=null && request.getParameter("course_number")!=null && request.getParameter("professor")!=null
         && request.getParameter("tutored_by")!=null && RequestedParameters[3]!=null && RequestedParameters[4]!=null && request.getParameter("end_bool")!=null){
   String tempID=(String)request.getParameter("ID"); tempID.trim();
   String tempcourse_no= (String)request.getParameter("course_number");tempcourse_no.trim();
   String tempProf=(String)request.getParameter("professor");tempProf.trim();
   String tempTutor=(String)request.getParameter("tutored_by");tempTutor.trim();
   String tempStime=(RequestedParameters[3]).trim();String tempEtime=(RequestedParameters[4]).trim();
   String tempEndBool=(String)request.getParameter("end_bool");tempEndBool.trim();
   if(tempID.length()>0 && tempcourse_no.length()>0 && tempProf.length()>0 && tempTutor.length()>0 && tempStime.length()>0 
                      && tempEtime.length()>0 && tempEndBool.length()>0){
     Hashtable HashedParameters=new Hashtable();
   //String[] RequestedParameters=new String[columnCount_records_ta-1];
   //for(int i=2;i<=columnCount_records_ta;i++){RequestedParameters[i-2]=(String)request.getParameter(resultSetMetaData_general.getColumnName(i));}
   //make sure in the tut_records table starting_time appears at 5th column; end_time at 6th column
   /*RequestedParameters[3]= (String)request.getParameter("s_day")+" "+(String)request.getParameter("s_hour")+":"+
                 (String)request.getParameter("s_minute")+":00 "+(String)request.getParameter("s_AP");
   RequestedParameters[4]= (String)request.getParameter("e_day")+" "+(String)request.getParameter("e_hour")+":"+
                 (String)request.getParameter("e_minute")+":00 "+(String)request.getParameter("e_AP");*/
   for(int i=2;i<=columnCount_records_ta;i++){HashedParameters.put(resultSetMetaData_general.getColumnName(i),RequestedParameters[i-2]);}
   
   
     String tutees_Query="SELECT ID FROM "+tutees_ta;
     Statement statement_tutees=connection.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE*/);
     ResultSet resultSet_tutees=statement_tutees.executeQuery(tutees_Query);
     boolean isRepeated=false;
     String tutees_insertion="INSERT INTO "+tutees_ta+" (ID, password) VALUES ('"+tempID+"','learningcenter')";
     Statement statement_tutees_insertion=connection.createStatement();
     while(resultSet_tutees.next()){if(resultSet_tutees.getString("ID").equals(tempID)){isRepeated=true;}}
     
     boolean isUsedAsTutor=false;
     Statement stmt_or=connection.createStatement();
     ResultSet rs_or=stmt_or.executeQuery("SELECT username FROM "+tutors_ta);
     while(rs_or.next()){if(rs_or.getString("username").equals(tempID))isUsedAsTutor=true;}
     if(!isUsedAsTutor)
     {if(tempcourse_no.equals("generic")&&tempProf.equals("generic")){
          if(!isRepeated)statement_tutees_insertion.executeUpdate(tutees_insertion); }
     if(!tempcourse_no.equals("generic")&& !tempProf.equals("generic")){
          
     if(tempEndBool.equals("no")&& ((cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)).equals((String)request.getParameter("s_day"))){
      if(!isRepeated)statement_tutees_insertion.executeUpdate(tutees_insertion);
      HashedParameters.remove("end_time");HashedParameters.remove("e_recorded_by");
      HashedParameters.remove("tutored_by");
      String Fields="(";String Values="('";
      for(Enumeration e=HashedParameters.keys();e.hasMoreElements();){String ss=(String)e.nextElement();Fields=Fields+ss+",";Values=Values+HashedParameters.get(ss)+"','";}
      Fields=Fields.substring(0,Fields.length()-1);Values=Values.substring(0,Values.length()-2);
      Fields=Fields+")";Values=Values+")";
      String newInsertionQuery="INSERT INTO "+records_ta+" "+Fields+" VALUES "+Values;
      Statement statement_insertion=connection.createStatement();
      statement_insertion.executeUpdate(newInsertionQuery);
      }
     else if(tempEndBool.equals("yes")&& ((String)request.getParameter("s_day")).equals((String)request.getParameter("e_day"))){
       SimpleDateFormat sdf_t=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
       java.util.Date s_date=sdf_t.parse((String)HashedParameters.get("start_time"));
       java.util.Date e_date=sdf_t.parse((String)HashedParameters.get("end_time"));

      if(s_date.compareTo(e_date)<0){
       if(!isRepeated)statement_tutees_insertion.executeUpdate(tutees_insertion);
        String Fields="(";String Values="('";
      for(Enumeration e=HashedParameters.keys();e.hasMoreElements();){String ss=(String)e.nextElement();Fields=Fields+ss+",";Values=Values+HashedParameters.get(ss)+"','";}
      Fields=Fields.substring(0,Fields.length()-1);Values=Values.substring(0,Values.length()-2);
      Fields=Fields+")";Values=Values+")";
      String newInsertionQuery="INSERT INTO "+records_ta+" "+Fields+" VALUES "+Values;
      Statement statement_insertion=connection.createStatement();
      statement_insertion.executeUpdate(newInsertionQuery);
      
       }
      }
     }

   }
  else{out.print("<br><font color=red>Please choose a different ID as the one you chose was already used as the username of a tutor.</font><br>");}
   }}
   
   connection.close();
   }
  catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
      System.err.println("Error connecting: " + sqle);
    } catch(Exception ex) {
      System.err.println("Error with input: " + ex);
    }
  if(status.equals(dirStatus)){%>
<br><center><table><tr>
  <form method=POST action="addremovetutors">
  <td><input type="submit" value="Click to add or remove Tutors"></td>
  </form>
  <form method=POST action="generatereports">
  <td><input type="submit" value="Click to generate reports"></td>
  </form></table></center>
  <%} %>
<% //String RECORD=(String)request.getParameter("id");out.print(RECORD); %>
<br><Center><form name=ParaForm method=POST action="Tutoring.jsp" >
<table>
<tr>
<td>Enter LnCtr ID: <td><INPUT TYPE="TEXT" Name="ID">
<tr>
<td>Course Number: <td><SELECT name="course_number" onChange="courseToProf()">
                 <option value="generic">choose a course
                <% for(int i=0;i<actualNum;i++){%>
                <option value=<%= courses[i].substring(0,6)%>><%=courses[i].substring(0,6)%><%}%></SELECT>
<tr><td>Professor :<td><Select name="professor"><option value="generic">choose the professor</select>
<tr><td>Start Time:<td> <SELECT name="s_hour"><option value=<%=cal.get(cal.HOUR)%>><%=cal.get(cal.HOUR)%>
                       <%for(int i=1;i<=12;i++){%><option value=<%=i%>><%=i%><%}%>
                      </SELECT>
                       <SELECT name="s_minute"><option value=<%=cal.get(cal.MINUTE)%>><%=cal.get(cal.MINUTE)%>
                       <%for(int i=0;i<12;i++){%><option value=<%=i*5%>><%=i*5%><%}%>
                      </SELECT>
                      <%String s_AP;if(cal.get(cal.HOUR_OF_DAY)>=12){s_AP="PM";}else{s_AP="AM";}%>
                 <SELECT name="s_AP"><option value=<%=s_AP%>><%=s_AP%><option value="AM">AM<option value="PM">PM</SELECT>
                 <input size=8 maxlength=10 type="TEXT" name="s_day" value=<%=(cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)%>>
<tr><td><SELECT name="end_bool"> <option value="no" selected>End Time? No<option value="yes">End Time? Yes </SELECT>
 <td> <SELECT name="e_hour"><option value=<%=cal.get(cal.HOUR)%>><%=cal.get(cal.HOUR)%>
                       <%for(int i=1;i<=12;i++){%><option value=<%=i%>><%=i%><%}%>
                      </SELECT>
                       <SELECT name="e_minute"><option value=<%=cal.get(cal.MINUTE)%>><%=cal.get(cal.MINUTE)%>
                       <%for(int i=0;i<12;i++){%><option value=<%=i*5%>><%=i*5%><%}%>
                      </SELECT>
                      <%String e_AP;if(cal.get(cal.HOUR_OF_DAY)>=12){e_AP="PM";}else{e_AP="AM";}%>
                 <SELECT name="e_AP"><option value=<%=e_AP%>><%=e_AP%><option value="AM">AM<option value="PM">PM</SELECT>
                 <input size=8 maxlength=10 type="TEXT" name="e_day" value=<%=(cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)%>>

<tr><td>Tutored by:<td><SELECT name="tutored_by"><option value="tutors">choose a tutor<option value="tutors">tutors
                  <%for(int i=0;i<TutorDirectorCount;i++){%><option value=<%= TutorDirectorList.get(i)%> ><%=TutorDirectorList.get(i)%><%}%></SELECT>
<tr><td> <INPUT TYPE="HIDDEN" Name="s_recorded_by" value=<%=logonUSER%>>
 <td> <INPUT TYPE="HIDDEN" Name="e_recorded_by" value=<%=logonUSER%>>

<tr><td><td>
<input type="submit" value="record">&nbsp;&nbsp;&nbsp;<input type="reset" value="reset"></table>
</form></Center>
<center><a href="javascript:void(window.open('help.html','help','width=280, height=650,left=0,
top=0,location=no,menubar=no,status=no,toolbar=no,scrollbars=yes,resizable=no,fullscreen=no'))">help</a></center><hr><br><center><strong><font color=#CC0066>These students have come-in times but not leaving-times yet.</font></strong></center><br>
<Center><form method=POST action="AdjustRecords.jsp"><table><tr><td>To adjust their records, click <td><input type="submit" value="adjust"></table></form></center>
<% 
try{
   Class.forName(driver);
   Connection connection = DriverManager.getConnection(url,DatabaseUser,DatabasePassword);
   String incomplete_Query = "SELECT * FROM "+records_ta;
   Statement incomplete_statement=connection.createStatement();
   ResultSet resultSet_incomplete=incomplete_statement.executeQuery(incomplete_Query);
   //ResultSetMetaData resultSetMetaData_incomplete =resultSet_incomplete.getMetaData();
   boolean NoEndTime;int num_incomplete=0;
   out.print("<center><table cellspacing=\"20\">");
   out.print("<tr><td>visits_number<td>ID<td>course_number<td>professor<td>start_time");
   while(resultSet_incomplete.next()){
   NoEndTime=false;
   String rsmi=resultSet_incomplete.getString("end_time");
   if(rsmi!=null){rsmi.trim();if(rsmi.length()==0){NoEndTime=true;}}
   else{NoEndTime=true;}
   if(NoEndTime){num_incomplete++;
                 out.print("<tr><td>"+resultSet_incomplete.getString("visits_number")+"<td>"
                        +resultSet_incomplete.getString("ID")+"<td>"
                        +resultSet_incomplete.getString("course_number")+"<td>"
                        +resultSet_incomplete.getString("professor")+"<td>"
                        +resultSet_incomplete.getString("start_time")); }
   }
   out.print("</table></Center>");
   if(num_incomplete==0){out.print("<center><font color=blue>There are no records to display.</font></center>");}
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
  var courseCol=document.ParaForm.course_number;
  var profCol=document.ParaForm.professor;
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