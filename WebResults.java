import java.sql.*;
//wesley Oliver v8
import java.util.*;
import java.io.*;
import javax.swing.*;
public class WebResults extends Thread
{
    private static boolean process = true;
    private static Connection DataBase;
    private static Vector Athletes = new Vector (300, 25), Clubs = new Vector (30, 3), Events = new Vector (80, 10), Type = new Vector (2, 1), Results = new Vector (300, 100);
    private static String HTML_Athletes = "ath = new Array(\"",
	HTML_Clubs = "clubs = new Array(\"",
	HTML_Events = "events = new Array(\"",
	HTML_Results = "res = new Array(\"",
	HTML_Relay = "relay = new Array(\"",
	HTML_Loc = "loc = new Array(\"",
	HTML_ath = "athres = new Array(",
	HTML_Type = "type = new Array(\"Finals\",\"Prelims\",\"Semi-Final\");";
    private static String publishdir, templete, published;
    private static int days;
    private static boolean addToIndex;

    public WebResults (Connection Con)
    {

	DataBase = Con;
	//Adds default types for word conversion
	//mainly will play a roll in upgrading to work with meet manager wich supports up to 10 prelims i.e semi's , quaters
	Type.addElement (new String ("F"));
	Type.addElement (new String ("P"));
	Type.addElement (new String ("S"));
	try
	{
	    ValidateDatabase ();
	}
	catch (Exception e)
	{
	    Manager.processWin.Meets.append ("\nerror :" + e);
	}
    }


    static int getClubsIndex (String TCode)
    {
	int i = 0;
	for (; i < Clubs.size () ; i++)
	{
	    if (String.valueOf (Clubs.elementAt (i)).compareTo (TCode) == 0)
	    {
		break;
	    }
	}
	return (i * 2); //makes provision for all clubs in selection
    }


    static void getClubs (String meet) throws SQLException
    {
	Clubs.removeAllElements ();
	HTML_Clubs = "clubs = new Array(\"";
	Statement DataRequest = null;
	ResultSet res = null;
	//Excuting Query to Retrive Club List for HTML AND Adds to a vector for search use later
	DataRequest = DataBase.createStatement ();
	res = DataRequest.executeQuery ("SELECT TEAM.TName, TEAM.TCode, TEAM.LSC FROM RESULT INNER JOIN TEAM ON RESULT.TEAM = TEAM.Team GROUP BY TEAM.TName, TEAM.TCode, TEAM.LSC, RESULT.MEET HAVING (((RESULT.MEET)=" + meet + "))ORDER BY TEAM.TName;");
	String t1, t2, t3;
	while (res.next ())
	{
	    t1 = res.getString (1);
	    t2 = res.getString (2);
	    t3 = res.getString (3);
	    Clubs.addElement (t2);
	    HTML_Clubs += t1 + "\",\"";
	    HTML_Clubs += t2 + "-" + t3 + "\",\"";
	}

	HTML_Clubs += "\",\"\");";
	Clubs.trimToSize ();
	res.close ();
	DataRequest.close ();

    }


    static void getAthletes (String meet) throws SQLException
    {
	//Retrives a list of althetes for indivadual results
	Athletes.removeAllElements ();
	HTML_Athletes = "ath = new Array(\"-\",\"-\",\"-\",\"-\",\"-\",\"";
	Statement DataRequest = null;
	ResultSet res = null;
	DataRequest = DataBase.createStatement ();
	res = DataRequest.executeQuery ("SELECT Athlete.Last, Athlete.First, TEAM.TCode, Athlete.Sex, RESULT.AGE, RESULT.ATHLETE FROM (RESULT INNER JOIN Athlete ON RESULT.ATHLETE = Athlete.Athlete) INNER JOIN TEAM ON RESULT.TEAM = TEAM.Team GROUP BY Athlete.Last, Athlete.First, TEAM.TCode, Athlete.Sex, RESULT.AGE, RESULT.ATHLETE, TEAM.TName, RESULT.MEET HAVING (((RESULT.MEET)=" + meet + ")) ORDER BY TEAM.TName, Athlete.Last, Athlete.First;");

	String t1, t2, t3, t4, t5, t6;
	while (res.next ())
	{
	    t1 = format (res.getString (1));
	    t2 = format (res.getString (2));
	    t3 = res.getString (3);
	    t4 = res.getString (4);
	    t5 = res.getString (5);
	    t6 = res.getString (6);
	    HTML_Athletes += t1 + "\",\"" + t2 + "\"," + getClubsIndex (t3) + ",\"" + t4 + "\"," + t5 + ",\"";
	    Athletes.addElement (t6);
	}
	//for relay in case did not take part in any indivi
	res = DataRequest.executeQuery ("SELECT RELAY.[ATH(1)], Athlete.Last, Athlete.First, Athlete.Sex, RELAY.[ATH(2)], Athlete_1.Last, Athlete_1.First, Athlete_1.Sex, RELAY.[ATH(3)], Athlete_2.Last, Athlete_2.First, Athlete_2.Sex, RELAY.[ATH(4)], Athlete_3.Last, Athlete_3.First, Athlete_3.Sex FROM (((((RELAY INNER JOIN RESULT ON RELAY.RELAY = RESULT.ATHLETE) INNER JOIN Athlete ON RELAY.[ATH(1)] = Athlete.Athlete) INNER JOIN Athlete AS Athlete_1 ON RELAY.[ATH(2)] = Athlete_1.Athlete) INNER JOIN Athlete AS Athlete_2 ON RELAY.[ATH(3)] = Athlete_2.Athlete) INNER JOIN Athlete AS Athlete_3 ON RELAY.[ATH(4)] = Athlete_3.Athlete) INNER JOIN TEAM ON RESULT.TEAM = TEAM.Team GROUP BY RELAY.[ATH(1)], Athlete.Last, Athlete.First, Athlete.Sex, RELAY.[ATH(2)], Athlete_1.Last, Athlete_1.First, Athlete_1.Sex, RELAY.[ATH(3)], Athlete_2.Last, Athlete_2.First, Athlete_2.Sex, RELAY.[ATH(4)], Athlete_3.Last, Athlete_3.First, Athlete_3.Sex, RELAY.MEET HAVING (((RELAY.MEET)=" + meet + "));");
	while (res.next ())
	{
	    for (int c = 0 ; c < 4 ; c++)
	    {
		t1 = res.getString ((c * 4) + 1);
		if (getAthletesIndex (t1) == -1)
		{
		    t2 = format (res.getString ((c * 4) + 2));
		    t3 = format (res.getString ((c * 4) + 3));
		    t4 = res.getString ((c * 4) + 4);
		    HTML_Athletes += t2 + "\",\"" + t3 + "\",\"\",\"" + t4 + "\",,\"";
		    Athletes.addElement (t1);
		}
	    }
	}
	//closing streams
	HTML_Athletes += "\",\"\");";
	Athletes.trimToSize ();
	res.close ();
	DataRequest.close ();
    }


    static int getAthletesIndex (String number)
    {
	int index = -1;
	for (int i = 0 ; i < Athletes.size () ; i++)
	{

	    if (String.valueOf (Athletes.elementAt (i)).compareTo (number) == 0)
	    {
		index = i;
		break;
	    }
	}
	return (index + 1) * 5;
    }


    static void getResults (String meet) throws SQLException
    {
	// Manager.processWin.Meets.append ("\nIndividual");
	//retrives Individual Results
	Vector tres = new Vector (3, 1);
	HTML_Results = "res = new Array(";
	String temp = "";
	Statement DataRequest = null;
	ResultSet res = null;
	DataRequest = DataBase.createStatement ();
	res = DataRequest.executeQuery ("SELECT  RESULT.MTEVENT, RESULT.ATHLETE, RESULT.SCORE, RESULT.PLACE, RESULT.RESULT, RESULT.F_P FROM ((RESULT INNER JOIN Athlete ON RESULT.ATHLETE = Athlete.Athlete) INNER JOIN TEAM ON RESULT.TEAM = TEAM.Team) INNER JOIN MTEVENT ON RESULT.MTEVENT = MTEVENT.MtEvent WHERE (((RESULT.I_R)='I') AND (((RESULT.F_P)='F') OR ((RESULT.F_P)='P')) AND ((RESULT.MEET)=" + meet + ")) ORDER BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, RESULT.F_P, RESULT.PLACE;");
	String t2, t3, t4, event = "", r, swtchlast = "F", swtch;
	int t1;
	while (res.next ()) //intoduce a type swicth on final or prelims to correct no results error
	{
	    t4 = res.getString (1);
	    t1 = getAthletesIndex (res.getString (2));
	    t2 = time (res.getString (3));
	    t3 = res.getString (4);
	    r = res.getString (5);
	    swtch = res.getString (6);
	    if (!t4.equals (event) || !swtch.equals (swtchlast))
	    {
		HTML_Results += temp;
		temp = "";
		for (int s = 0 ; s < tres.size () ; s++)
		{
		    Results.addElement (tres.elementAt (s));
		}
		tres.clear ();
		tres.trimToSize ();
	    }
	    if (t3.equals ("0") || t2.equals ("No Result"))
	    {
		temp += "\"\"," + t1 + ",\"" + t2 + "\","; //mite be a problem on t3
		tres.addElement (r);
	    }
	    else
	    {
		Results.addElement (r);
		HTML_Results += t3 + "," + t1 + ",\"" + t2 + "\",";
	    }
	    event = t4;
	    swtchlast = swtch;
	}
	for (int s = 0 ; s < tres.size () ; s++)
	{
	    Results.addElement (tres.elementAt (s));
	}
	HTML_Results += temp;
	temp = "";
	HTML_Results += "\"\",\"\");";
	//  Manager.processWin.Meets.append ("\nRelay");
	//Retrives Relay Results
	HTML_Relay = "relay = new Array(\"";
	res = DataRequest.executeQuery ("SELECT RESULT.MTEVENT, TEAM.TName, RELAY.LETTER, RELAY.SEX, RESULT.SCORE, RELAY.[ATH(1)], RELAY.[ATH(2)], RELAY.[ATH(3)], RELAY.[ATH(4)], RESULT.PLACE FROM ((RELAY INNER JOIN RESULT ON RELAY.RELAY = RESULT.ATHLETE) INNER JOIN TEAM ON RESULT.TEAM = TEAM.Team) INNER JOIN MTEVENT ON RESULT.MTEVENT = MTEVENT.MtEvent WHERE (((RESULT.MEET)=" + meet + ") AND ((RESULT.I_R)='R') AND ((RESULT.F_P)='F')) ORDER BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, RESULT.PLACE;");
	String t7, t5, t6;
	int a1, a2, a3, a4;
	event = "";
	while (res.next ())
	{
	    t7 = res.getString (1);
	    t2 = res.getString (2);
	    t3 = res.getString (3);
	    t4 = res.getString (4);
	    t5 = time (res.getString (5));
	    try
	    {
		a1 = getAthletesIndex (res.getString (6));
	    }
	    catch (NullPointerException e)
	    {
		a1 = 0;
	    }
	    try
	    {
		a2 = getAthletesIndex (res.getString (7));
	    }
	    catch (NullPointerException e)
	    {
		a2 = 0;
	    }
	    try
	    {
		a3 = getAthletesIndex (res.getString (8));
	    }
	    catch (NullPointerException e)
	    {
		a3 = 0;
	    }
	    try
	    {
		a4 = getAthletesIndex (res.getString (9));
	    }
	    catch (NullPointerException e)
	    {
		a4 = 0;
	    }
	    t6 = res.getString (10);
	    if (!t7.equals (event))
	    {
		HTML_Relay += temp;
		temp = "";
	    }
	    if (t6.equals ("0") || t5.equals ("No Result"))
	    {
		temp += t6 + "\",\"" + t2 + "\",\"" + t3 + "\",\"" + t4 + "\",\"" + t5 + "\"," + a1 + "," + a2 + "," + a3 + "," + a4 + ",\"";
	    }
	    else
	    {
		HTML_Relay += t6 + "\",\"" + t2 + "\",\"" + t3 + "\",\"" + t4 + "\",\"" + t5 + "\"," + a1 + "," + a2 + "," + a3 + "," + a4 + ",\"";
	    }
	    event = t7;
	}
	HTML_Relay += temp;
	temp = "";
	HTML_Relay += "\",\"\");";

	res.close ();
	DataRequest.close ();

    }


    static int getResultsIndex (String T)
    {
	int i = 0;
	for (; i < Results.size () ; i++)
	{
	    if (String.valueOf (Results.elementAt (i)).compareTo (T) == 0)
	    {
		break;
	    }
	}
	return i;
    }


    static void getEvents (String meet) throws SQLException
    {
	//  Manager.processWin.Meets.append ("\nEvents");
	Events.removeAllElements ();
	HTML_Events = "events = new Array(";
	Statement DataRequest = null;
	ResultSet res = null;
	DataRequest = DataBase.createStatement ();
	// Manager.processWin.Meets.append ("\nEvents");
	//res = DataRequest.executeQuery ("SELECT MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, MTEVENT.Sex, MTEVENT.Lo_Hi, RESULT.DISTANCE, RESULT.STROKE, RESULT.I_R, MTEVENT.MtEvent FROM MTEVENT INNER JOIN RESULT ON MTEVENT.MtEvent = RESULT.MTEVENT GROUP BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, MTEVENT.Sex, MTEVENT.Lo_Hi, RESULT.DISTANCE, RESULT.STROKE, RESULT.I_R, MTEVENT.MtEvent, MTEVENT.Meet HAVING (((RESULTS.I_R)='I' Or (RESULTS.I_R)='R') AND ((MTEVENT.Meet)=" + meet + ")) ORDER BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX;");

	res = DataRequest.executeQuery ("SELECT MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, MTEVENT.Sex, MTEVENT.Lo_Hi, RESULT.DISTANCE, RESULT.STROKE, RESULT.I_R, MTEVENT.MtEvent FROM MTEVENT INNER JOIN RESULT ON MTEVENT.MtEvent = RESULT.MTEVENT WHERE (((MTEVENT.Meet)=" + meet + ") AND (((RESULT.F_P)='F') OR ((RESULT.F_P)='P'))) GROUP BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, MTEVENT.Sex, MTEVENT.Lo_Hi, RESULT.DISTANCE, RESULT.STROKE, RESULT.I_R, MTEVENT.MtEvent HAVING (((RESULT.I_R)='I' Or (RESULT.I_R)='R')) ORDER BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX;");


	String t1, t2, t3, t4, t5, t6, t7, t8, t9;
	// Manager.processWin.Meets.append ("\nEvents");
	while (res.next ())
	{
	    t1 = res.getString (1);
	    t2 = res.getString (2);
	    t3 = res.getString (3);
	    t4 = res.getString (4);
	    t5 = res.getString (5);
	    t6 = res.getString (6);
	    t7 = res.getString (7);
	    t8 = res.getString (8);
	    t9 = res.getString (9);
	    HTML_Events += t1 + ",\"" + t2 + " " + t3 + "\",\"" + t4 + "\"," + t5 + "," + t6 + "," + t7 + ",\"" + t8 + "\",";
	    Events.addElement (t9);
	}
	HTML_Events += "\"\",\"\");";
	Events.trimToSize ();
	res.close ();
	DataRequest.close ();
	// Manager.processWin.Meets.append ("\nEvents");
    }


    static int getEventsIndex (String T)
    {
	int i = 0;
	for (; i < Events.size () ; i++)
	{
	    if (String.valueOf (Events.elementAt (i)).compareTo (T) == 0)
	    {
		break;
	    }
	}
	return i;
    }


    static int getTypeIndex (String T)
    {
	int i = 0;
	for (; i < Type.size () ; i++)
	{
	    if (String.valueOf (Type.elementAt (i)).compareTo (T) == 0)
	    {
		break;
	    }
	}
	return i;
    }


    static void getResLocation (String meet) throws SQLException
    {
	//Determine the null result exception if results contaian non exsistant athletes

	Statement DataRequest = null;
	ResultSet res = null;
	DataRequest = DataBase.createStatement ();
	HTML_Loc = "loc = new Array(";
	// Manager.processWin.Meets.append ("\nLocation");
	// res = DataRequest.executeQuery ("SELECT RESULT.I_R, RESULT.F_P, RESULT.MTEVENT, Count(RESULT.RESULT) AS CountOfRESULT FROM RESULT INNER JOIN MTEVENT ON RESULT.MTEVENT = MTEVENT.MtEvent GROUP BY RESULT.F_P, RESULT.MTEVENT, MTEVENT.Meet, RESULT.I_R, MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX HAVING (((MTEVENT.Meet)=" + meet + ") AND ((RESULT.I_R)='I' Or (RESULT.I_R)='R')) ORDER BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX;");

	res = DataRequest.executeQuery ("SELECT RESULT.I_R, RESULT.F_P, RESULT.MTEVENT, Count(RESULT.RESULT) AS CountOfRESULT FROM RESULT INNER JOIN MTEVENT ON RESULT.MTEVENT = MTEVENT.MtEvent GROUP BY RESULT.I_R, RESULT.F_P, RESULT.MTEVENT, MTEVENT.Meet, RESULT.I_R, MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX HAVING (((RESULT.I_R)='I') AND ((MTEVENT.Meet)=" + meet + ")) OR (((RESULT.I_R)='R') AND ((RESULT.F_P)='F') AND ((MTEVENT.Meet)=" + meet + ")) ORDER BY MTEVENT.Session, MTEVENT.MtEv, MTEVENT.MtEvX, RESULT.F_P;");


	String t1, t2, t4, t3;
	int type = 0, eventnum = -1;
	String event = "";
	while (res.next ())
	{
	    t2 = res.getString (1);
	    t4 = res.getString (2);
	    // if (t2.equals ("R") & t4.equals ("F") || t2.equals ("I"))
	    // {
	    try
	    {
		type = getTypeIndex (t4);
	    }
	    catch (NullPointerException e)
	    {
		type = 0;
		Manager.processWin.Meets.append ("\nError");
	    }
	    t1 = res.getString (3);
	    t3 = res.getString (4);
	    // if (!t3.equals("0"))
	    // {
	    eventnum = getEventsIndex (t1);
	    HTML_Loc += eventnum + "," + type + "," + t3 + ",";
	    // }
	    // }
	}
	HTML_Loc += "\"\",\"\");";
	res.close ();
	DataRequest.close ();
    }


    static void ValidateDatabase () throws SQLException  //Cheaks the database data that it does not contain any miss match data
    {
	//remove results entries that have no exsisting athletes link
	Statement DataRequest = null;
	DataRequest = DataBase.createStatement ();
	DataRequest.executeUpdate ("DELETE RESULT.*, RESULT.I_R, Athlete.Athlete FROM RESULT LEFT JOIN Athlete ON RESULT.ATHLETE = Athlete.Athlete WHERE (((RESULT.I_R)='I') AND ((Athlete.Athlete) Is Null));");
	DataRequest.close ();

	//checks meet events for in consistance with meet individual results and relay results
    }



    public static String format (String last)
    {
	last = last.toLowerCase ();
	last = last.substring (0, 1).toUpperCase () + last.substring (1, last.length ());
	int space = -1;
	for (int i = 1 ; i < last.length () ; i++)
	{
	    space = last.indexOf (" ", i);
	    if (space != -1)
	    {
		last = last.substring (0, space + 1) + Character.toUpperCase (last.charAt (space + 1)) + last.substring (space + 2, last.length ());
		i = space + 2;
	    }
	}


	return last;
    }


    public static String time (String str)
    {
	int mi = 0, se = 0, mu = 0;
	int time = Integer.parseInt (str);
	String stime = "";
	mi = (time % 100);
	time = (int) (time / 100);
	se = (time % 60);
	time = (int) (time / 60);
	mu = (time % 60);
	time = (int) (time / 60);
	if (mu < 10)
	{
	    stime = stime + "0" + mu;
	}


	else
	{
	    stime = stime + mu;
	}


	if (se < 10)
	{
	    stime = stime + ":0" + se;
	}


	else
	{
	    stime = stime + ":" + se;
	}


	if (mi < 10)
	{

	    stime = stime + ".0" + mi;
	}


	else
	{
	    stime = stime + "." + mi;
	}


	if (mi == 0 & mu == 0 & se == 0)
	{
	    stime = "no result";
	}


	return stime;
    }


    public static void stopCompile ()
    {
	process = false;
    }


    public void run ()
    {
	process = true;
	Manager.processWin.Meets.append ("Compling meets");
	//Querying
	Statement DataRequest = null;
	ResultSet meetsres = null;
	String day = "";
	try
	{
	    DataRequest = DataBase.createStatement ();
	    if (days != 0)
	    {
		day = "And ((Meet.Start)>(Date()-" + days + "))";
	    }
	    meetsres = DataRequest.executeQuery ("SELECT MEET.Meet, MEET.MName, MEET.Course, MEET.Start, MEET.End, MEET.Location, MEET.Meet FROM MEET WHERE (((Meet.Start)<Date())" + day + ") ORDER BY MEET.Start DESC;");
	}


	catch (SQLException error)
	{
	    System.err.println ("SQL error. hh" + error);
	}


	String begin = "", end = "";
	try
	{
	    BufferedReader in1 = new BufferedReader (new FileReader (templete));
	    String v1 = in1.readLine ();
	    while (v1 != null)
	    {
		if (v1.indexOf ("<--WebResults-->") != -1)
		{
		    begin += v1.substring (0, v1.indexOf ("<--WebResults-->"));
		    end += v1.substring (v1.indexOf ("<--WebResults-->") + 16, v1.length ());
		    break;
		}
		else
		{
		    begin += v1;
		}
		v1 = in1.readLine ();
	    }
	    v1 = in1.readLine ();
	    while (v1 != null)
	    {
		end += v1;
		v1 = in1.readLine ();
	    }
	}


	catch (IOException error)
	{
	    System.err.println ("No exsisting meet templete file");
	}


	file meets = new file (publishdir, "index.htm");
	try
	{

	    //creating html page
	    meets.write (begin);
	    meets.write ("<div align=\"center\"><table border=\"0\" width=\"795px\" cellspacing=\"1\" cellpadding=\"2\">");
	    meets.write ("<tr><td width=\"370px\"><b>Meet</b></td>");
	    meets.write ("<td width=\"80px\"><b>Start Date</b></td>");
	    meets.write ("<td width=\"80px\"><b>End Date</b></td>");
	    meets.write ("<td width=\"50px\"><b>Course</b></td>");
	    meets.write ("<td><b> Location</b></td></tr><b>");
	}


	catch (Exception e)
	{
	    Manager.processWin.Meets.append ("\nError on wruiteing index: " + e);
	}

	/* String l = "";
	 if (!published.equals ("pub.txt"))
	 {
	     try
	     {
		 //Meets Lists
		 BufferedReader in = new BufferedReader (new FileReader (published));

		 String v = in.readLine ();
		 while (v != null)
		 {
		     l = l + v;
		     v = in.readLine ();
		 }
	     }
	     catch (FileNotFoundException f)
	     {

	     }
	     catch (IOException ex)
	     {

	     }
	 }*/

	try
	{
	    BufferedWriter out = new BufferedWriter (new FileWriter (publishdir + "published Meets.txt"));
	    if (published.equals (""))
	    {
		out.write (" ; ");
	    }
	    while ((meetsres.next () & process == true)) //loops to retrive the results of all the meets
	    {
		String resData[] = new String [7];
		for (int c = 1 ; c < 8 ; c++)
		{
		    resData [c - 1] = meetsres.getString (c);
		}

		String meetid = resData [6];
		if (published.indexOf (resData [1].trim ().replace (' ', '_') + "_" + resData [3].substring (0, 10)) == -1) //cheacks to see if it is a prevously compiled meet
		{
		    getClubs (meetid);
		    Manager.processWin.Meets.append (" ."); //makes the program look as if it is doing some thing.
		    getAthletes (meetid);
		    Manager.processWin.Meets.append (" .");
		    getEvents (meetid);
		    Manager.processWin.Meets.append (" .");
		    getResults (meetid);
		    Manager.processWin.Meets.append (" .");
		    Thread.sleep (20);
		    getResLocation (meetid);
		    Manager.processWin.Meets.append (" .");
		    Thread.sleep (20);
		    String link = resData [0];
		    String MName = resData [1].trim ();

		    if (HTML_Results.length () > 23 || HTML_Relay.length () > 25)
		    {

			Manager.processWin.Meets.append ("\nMeet: " + MName);
			//writes the meet out put file
			file meetres = new file (publishdir, MName.replace (' ', '_') + "_" + resData [3].substring (0, 10) + ".htm");
			meetres.writeln ("<head><title>" + MName + " " + resData [3].substring (0, 10) + " Results, Strand Aquatic's Swimming Club</title><link rel=\"stylesheet\" type=\"text/css\" href=\"../styles.css\"><script src=\"filters.js\" language=\"javascript\" type=\"text/javascript\"></script><script language=\"javascript\" type=\"text/javascript\"><!--");
			meetres.writeln ("window.status=\"Downloading.... List of Clubs\"; document.write(\"<frameset cols='170,*' framespacing='0' frameborder='0' scrolling='no' noresize><frame name='l' src='menu.htm' scrolling='no' noresize><frameset rows='120,*' framespacing='0' frameborder='0' noresize><frame name='h'  scrolling='no'><frame name='m'></frameset></frameset>\");");
			meetres.writeln (HTML_Clubs);
			meetres.writeln (HTML_Type);
			meetres.writeln (HTML_Events);
			meetres.writeln (HTML_Loc);
			meetres.writeln ("window.status = \"Proccessing... setting up Filters\";");
			meetres.writeln ("fil ();");
			meetres.writeln ("window.status = \"Dowmloading List of athletes\";");
			meetres.writeln (HTML_Athletes);
			meetres.writeln ("window.status = \"Dowmloading Results\";");
			meetres.writeln (HTML_Relay);
			meetres.writeln (HTML_Results);
			meetres.writeln ("setTimeout(\"init();\",1000);");
			meetres.writeln ("</script></head><body>Strand aquatic's makes heavy use of JavaScript!<br>You don't appear to have a java compatible browser!<br>Please visit one of the following sites depending<br>on your browser to update it.<br><a href=\"http://microsoft.com/windows/ie/\">Internet Explorer 6</a> or <a href=\"http://home.netscape.com/download/\">Netscape 6</a></body></html>");
			// meetres.write ();

			meetres.close ();


			//creates a link to meet

			meets.write ("<tr><td><a href=" + MName.replace (' ', '_') + "_" + resData [3].substring (0, 10) + ".htm>" + MName + "</a></td>");
			meets.write ("<td><b>" + resData [3].substring (0, 10) + "</b></td>");
			meets.write ("<td><b>" + resData [4].substring (0, 10) + "</b></td>");
			meets.write ("<td align=\"Center\"><b>" + resData [2] + "</b></td>");
			meets.write ("<td><b>" + resData [5] + "</b></td></tr>");
			out.write (MName.replace (' ', '_') + "_" + resData [3].substring (0, 10) + " ; " + MName + " ; " + resData [3].substring (0, 10) + " ; " + resData [4].substring (0, 10) + " ; " + resData [2] + " ; " + resData [5] + " ; ");
		    }
		}
	    }
	    if (published.length () > 3)
	    {
		out.write (published.substring (3, published.length ())); //writes out previous list of meets compiled
	    }
	    out.close ();
	    meetsres.close ();
	    DataRequest.close ();
	}


	catch (Exception e)
	{
	    Manager.processWin.Meets.append ("\nError on reading results: " + e);
	}


	//End of page write
	// if (!l1.equals (""))
	// {
	//     meets.write (l1.substring (l1.indexOf ("<td><b> Location</b></td></tr><b>") + 33, l1.indexOf ("</table></div><Br><Br></body></html>")));
	// }


	// meets.write ("</table></div><Br><Br></body></html>");

	//write out previously compiled meets
	if (addToIndex == true)
	{
	    StringTokenizer2 st2 = new StringTokenizer2 (published, " ; ");
	    while (st2.hasMoreTokens ())
	    {
		meets.write ("<tr><td><a href=" + st2.nextToken () + ".htm>" + st2.nextToken () + "</a></td>");
		meets.write ("<td><b>" + st2.nextToken () + "</b></td>");
		meets.write ("<td><b>" + st2.nextToken () + "</b></td>");
		meets.write ("<td align=\"Center\"><b>" + st2.nextToken () + "</b></td>");
		meets.write ("<td><b>" + st2.nextToken () + "</b></td></tr>");
	    }
	}

	meets.write ("</table></div>" + end);
	meets.close ();

	// runs the last closing features to compile
	try
	{
	    Manager.run.exec ("HTML.bat \"" + Manager.processWin.publish.getText () + "\"");
	    Manager.processWin.Meets.append ("\nFinshed Compiling into : " + Manager.processWin.publish.getText () + "\n");
	    int view = JOptionPane.showConfirmDialog (Manager.popup,
		    "Would you like to view the Results?", "Compiler",
		    JOptionPane.YES_NO_OPTION);
	    if (view == JOptionPane.YES_OPTION)
	    {
		Runtime.getRuntime ().exec ("C:/Program Files/Internet Explorer/iexplore.exe " + Manager.processWin.publish.getText () + "index.htm");

	    }
	    Manager.processWin.setVisible (false);
	    Manager.processWin.compile.setLabel ("Compile");
	}
	catch (IOException f)
	{
	    JOptionPane.showMessageDialog (Manager.popup, "Error could not excute HTML.bat or iexplore.exe", "Error", JOptionPane.ERROR_MESSAGE);
	    Manager.processWin.compile.setLabel ("Compile");
	}
	catch (Exception e)
	{
	    Manager.processWin.Meets.append ("\nError on reading results: " + e);
	}
    }


    public static void meets (String Ipublishdir, String Itemplete, String Ipublished, int Idays, boolean IaddToIndex)
    {
	publishdir = Ipublishdir;
	templete = Itemplete;
	published = Ipublished;
	days = Idays;
	addToIndex = IaddToIndex;
	Manager.compiler.start ();
    }


    static class file
    {
	BufferedWriter wr;
	file (String dir, String name)
	{
	    try
	    {
		File file = new File (dir, name);
		wr = new BufferedWriter (new FileWriter (file));

	    }
	    catch (Exception e)
	    {
	    }
	}


	void writeln (String str)
	{
	    try
	    {
		wr.write (str);
		wr.newLine ();
	    }
	    catch (Exception e)
	    {
	    }
	}

	void write (String str)
	{
	    try
	    {
		wr.write (str);
	    }
	    catch (Exception e)
	    {
	    }
	}


	void close ()
	{
	    try
	    {
		wr.close ();
	    }
	    catch (Exception e)
	    {
	    }
	}
    }
}


