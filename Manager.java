// Manager By Wesley Oliver Main Class
import java.awt.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.sql.*;
import javax.swing.*;
import java.io.*;
//import java.util.prefs.Preferences;
import java.beans.PropertyChangeEvent;
import java.util.*;
public class Manager extends Frame implements ActionListener
{
    static String loc = new File ("Manager.java").getAbsolutePath ();
    static final String installdir = loc.substring (0, loc.length () - 12);
    // static final String installdir = "C:\\Documents and Settings\\wezley\\Desktop\\Web Results V2.0\\";
    static Frame driver, popup, pro;
    static driver DrvSettings;
    static processing processWin;
    Toolkit toolkit;
    Image back;
    private static Connection DataBase;
    private static String resultsUrl = "", published = ""; //jdbc:odbc:
    private static String resultsPassword = "", resultsUser = "", templete = "default.htm", outputdir = installdir + "Compiled";
    static String Settings[] = new String [7];   //sorts settings,db results,db info,index templete
    static Runtime run = Runtime.getRuntime ();
    MenuBar main = new MenuBar ();
    Menu manage, Help;
    MenuItem ODBC, about, readme, exit, process;
    static WebResults compiler;
    public Manager ()
    {
	setTitle ("Web Results Manager");
	toolkit = Toolkit.getDefaultToolkit ();
	setIconImage (toolkit.getImage ("icon.gif"));
	back = toolkit.getImage ("pool.jpg");
	driver = new Frame ();
	popup = new Frame ();
	pro = new Frame ();
	DrvSettings = new driver (driver);
	processWin = new processing (pro);
	MediaTracker mediaTracker = new MediaTracker (this);
	mediaTracker.addImage (back, 0);
	try
	{
	    mediaTracker.waitForID (0);
	}
	catch (InterruptedException ie)
	{
	}

	setSize (toolkit.getScreenSize ().width, toolkit.getScreenSize ().height);
	addWindowListener (new WindowAdapter ()
	{
	    public void windowClosing (WindowEvent e)
	    {
		close ();
	    }
	    public void propertyChange (PropertyChangeEvent evt)
	    {
		repaint ();
	    }
	}
	);
	manage = new Menu ("Manager");

	process = new MenuItem ("Process");
	process.addActionListener (this);
	manage.add (process);

	ODBC = new MenuItem ("ODBC Manager");
	ODBC.addActionListener (this);
	manage.add (ODBC);

	exit = new MenuItem ("Exit");
	exit.addActionListener (this);
	manage.add ("-");
	manage.add (exit);
	Help = new Menu ("Help");
	readme = new MenuItem ("Help");
	readme.addActionListener (this);
	Help.add (readme);
	Help.add ("-");
	about = new MenuItem ("About");
	about.addActionListener (this);
	Help.add (about);

	main.setFont (new Font ("Helvetica", Font.BOLD, 12));
	main.add (manage);
	main.add (Help);
	setMenuBar (main);
	setVisible (true);
	//loads the previous settings
	try
	{
	    BufferedReader in = new BufferedReader (new FileReader ("Settings.set"));
	    for (int s = 0 ; s < 7 ; s++)
	    {
		String temp = in.readLine ();
		if (temp != null)
		{
		    Settings [s] = temp;
		}
		else
		{
		    Settings [s] = " ";

		}

	    }
	    if (!Settings [0].equals (" "))
	    {
		resultsUrl = Settings [0].trim ();
		resultsUser = Settings [1].trim ();
		resultsPassword = Settings [2].trim ();
		templete = Settings [3].trim ();
		outputdir = Settings [4].trim ();
		published = Settings [5].trim ();
		DrvSettings.setUrl (resultsUrl);
		if (templete.length () != 0)
		{
		    processWin.templete.setText (templete);
		    processWin.publish.setText (outputdir);
		    processWin.published.setText (published);
		}
		connect ();
	    }
	    else
	    {
		DrvSettings.setvisible (true);
	    }
	    in.close ();
	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (popup, "Error on Load Previous Settings,File Not Found\nSettings will Default", "Error", JOptionPane.ERROR_MESSAGE);
	    DrvSettings.setvisible (true);
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (popup, "Error on Load Previous Settings, General Error\nData may have become corrupt.\nSettings will Default", "Error", JOptionPane.ERROR_MESSAGE);
	    DrvSettings.setvisible (true);
	}
	catch (SQLException e)
	{
	    JOptionPane.showMessageDialog (popup, "Error on Connecting to ODBC Database: " + resultsUrl + ", General Error\nODBC link may not exsiste anymore.\n", "Error", JOptionPane.ERROR_MESSAGE);
	    DrvSettings.setvisible (true);
	}
    }


    public void actionPerformed (ActionEvent e)
    {
	if (e.getSource () == exit)
	{
	    close ();
	}
	else
	{
	    if (e.getSource () == ODBC)
	    {
		DrvSettings.setvisible (true);
	    }
	    else
	    {
		if (e.getSource () == process)
		{
		    processWin.setVisible (true);
		}
		else
		{
		    if (e.getSource () == about)
		    {
			new about ();
		    }
		    else
		    {
			try
			{
			    Manager.run.exec ("C:/Program Files/Internet Explorer/iexplore.exe " + Manager.installdir + "Documentation.htm");
			}
			catch (IOException f)
			{
			    JOptionPane.showMessageDialog (popup, "Error Opening documentation.htm", "Error", JOptionPane.ERROR_MESSAGE);

			}
		    }
		}
	    }
	}
    }


    public static void close ()
    {
	int option = JOptionPane.showConfirmDialog (popup,
		"Are you sure you want to exit?", "Exit",
		JOptionPane.YES_NO_OPTION);
	if (option == JOptionPane.YES_OPTION)
	{
	    try
	    {
		if (DataBase != null)
		{
		    DataBase.close ();
		}

	    }
	    catch (SQLException e)
	    {
		JOptionPane.showMessageDialog (popup, "Error on Cloasing Database Connection,\nwill now continue to exit", "Error", JOptionPane.ERROR_MESSAGE);
	    }

	    try
	    {
		BufferedWriter out = new BufferedWriter (new FileWriter ("Settings.Set"));
		for (int s = 0 ; s < 7 ; s++)
		{
		    out.write (" " + Settings [s].trim ());
		    out.newLine ();
		}
		out.close ();
	    }
	    catch (IOException f)
	    {
		JOptionPane.showMessageDialog (popup, "Error on Saving Settings,\nwill now continue to exit", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    System.exit (0);
	}
    }


    public void connect () throws SQLException
    {
	try
	{
	    Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
	    DataBase = DriverManager.getConnection ("jdbc:odbc:" + resultsUrl);
	    processWin.Meets.setText ("");
	}
	catch (ClassNotFoundException error)
	{
	    JOptionPane.showMessageDialog (popup, "Error Could not find the 'JdbcOdbc' Driver class\nYou don't have an odbc Driver Manager install\nThis is esentail to run program will now exit", "Error", JOptionPane.ERROR_MESSAGE);
	    System.exit (0);
	}

    }


    public void paint (Graphics graphics)
    {
	graphics.drawImage (back, 0, 50, getSize ().width, getSize ().height, null);
	graphics.setFont (new Font ("Helvetica", Font.BOLD, 14));
	graphics.setColor (Color.black);
	graphics.fillRect (getSize ().width - 179, getSize ().height - 154, 159, 104);
	graphics.setColor (Color.LIGHT_GRAY);
	graphics.fillRect (getSize ().width - 175, getSize ().height - 150, 155, 100);
	graphics.setColor (Color.black);
	graphics.drawString ("Designed By", getSize ().width - 145, getSize ().height - 130);
	graphics.drawString ("Wesley Oliver, RSA", getSize ().width - 165, getSize ().height - 95);
	graphics.drawString ("Version 2.0", getSize ().width - 145, getSize ().height - 60);
    }


    public static void main (String[] args)
    {
	new Manager ();
    }


    class driver extends Dialog implements ActionListener
    {
	private TextField results, info;
	private Button ok, cancel, odbc;
	public driver (Frame f)
	{
	    super (f, "Driver Connection", true);
	    setTitle ("Driver Connection");
	    setSize (400, 180);
	    setLocation ((toolkit.getScreenSize ().width - 400) / 2, (toolkit.getScreenSize ().height - 200) / 2);
	    setBackground (new Color (0, 104, 204));
	    addWindowListener (new WindowAdapter ()
	    {
		public void windowClosing (WindowEvent e)
		{
		    close ();
		}
	    }
	    );
	    setLayout (new BorderLayout ());
	    setFont (new Font ("Helvetica", Font.BOLD, 14));
	    Panel Top = new Panel ();
	    Top.setLayout (new GridLayout (3, 1, 0, 0));
	    Top.add (new Label ("Welcome to Database link Manager,"));

	    odbc = new Button ("ODBC Manager");
	    odbc.addActionListener (this);
	    Panel b = new Panel ();
	    b.setLayout (new BorderLayout ());
	    b.add (new Label ("Please spessify ODBC names"), BorderLayout.WEST);
	    b.add (odbc, BorderLayout.EAST);
	    Top.add (b);
	    add (Top, BorderLayout.NORTH);

	    Panel Center = new Panel ();
	    Center.setLayout (new GridLayout (2, 2, 5, 10));
	    add (Center, BorderLayout.CENTER);
	    Center.add (new Label ("Swim Results"));
	    results = new TextField ();
	    results.addActionListener (this);
	    Center.add (results);
	    // Center.add (new Label ("info"));
	    // info = new TextField ();
	    Center.add (new Label (""));
	    Center.add (new Label (""));
	    // Center.add (info);

	    Panel South = new Panel ();
	    South.setLayout (new GridLayout (1, 2, 10, 10));
	    ok = new Button ("Connect");
	    ok.addActionListener (this);
	    South.add (ok);
	    cancel = new Button ("Close");
	    cancel.addActionListener (this);
	    South.add (cancel);
	    add (South, BorderLayout.SOUTH);
	}

	void setUrl (String url)
	{
	    results.setText (url);
	}

	public void actionPerformed (ActionEvent e)
	{
	    if (e.getSource () == cancel)
	    {
		close ();
	    }
	    else
	    {
		if (e.getSource () == odbc)
		{
		    try
		    {

			Manager.run.exec ("C:/Windows/system32/odbcad32.exe");
		    }
		    catch (IOException f)
		    {
			JOptionPane.showMessageDialog (popup, "No ODBC connection wizied found", "Error", JOptionPane.ERROR_MESSAGE);

		    }
		}
		else
		{
		    if (e.getSource () == ok || e.getSource () == results)
		    {
			try
			{
			    if (Manager.DataBase != null)
			    {
				ok.setLabel ("Disconnecting..");
				Manager.DataBase.close ();
				setTitle ("Driver Connection - Disconected");
			    }

			}
			catch (SQLException d)
			{
			    JOptionPane.showMessageDialog (popup, "Error on Cloasing Database Connection,\nwill now continue to exit", "Error", JOptionPane.ERROR_MESSAGE);
			}
			ok.setLabel ("Connecting..");
			if (results.getText ().trim ().length () != 0)
			{
			    resultsUrl = results.getText ().trim ();
			    String user = JOptionPane.showInputDialog (popup, "Please enter the username for database", "Username", JOptionPane.QUESTION_MESSAGE).trim ();
			    if (user.length () != 0)
			    {
				String pass = JOptionPane.showInputDialog (popup, "Please enter the Password for database", "Password", JOptionPane.QUESTION_MESSAGE);
				resultsUser = user;
				resultsPassword = pass;
			    }
			    else
			    {
				Manager.resultsUser = "";
				Manager.resultsPassword = "";
			    }
			    
			    try
			    {
				connect ();
				Manager.Settings [0] = resultsUrl;
				// Manager.Settings [1] = resultsUser;
				// Manager.Settings [2] = resultsPassword;
			    }
			    catch (SQLException r)
			    {
				JOptionPane.showMessageDialog (popup, "Error on Connecting to Database,Check ODBC name\nor incorrect user or password", "Error Connecting", JOptionPane.ERROR_MESSAGE);
			    }
			    ok.setLabel ("Connect");
			    setTitle ("Driver Connection - Connected");
			    JOptionPane.showMessageDialog (popup, "Connected to Database Succesfully", "Database Connection", JOptionPane.INFORMATION_MESSAGE);
			    close ();
			}
			else
			{
			    JOptionPane.showMessageDialog (popup, "You have not set up the ODBC link", "Error Connecting", JOptionPane.ERROR_MESSAGE);

			}
		    }
		}
	    }
	}

	public void setvisible (boolean vis)
	{
	    if (Manager.DataBase != null)
	    {
		setTitle ("Driver Connection - Connected");
	    }
	    else
	    {
		setTitle ("Driver Connection - Disconected");
	    }
	    this.setVisible (vis);
	}

	private void close ()
	{
	    if (DataBase == null)
	    {
		Manager.close ();
	    }
	    else
	    {
		setVisible (false);
	    }
	}

    }


    class about extends Dialog
    {
	about ()
	{
	    super (popup, "About", true);
	    setSize (200, 150);
	    setLocation ((toolkit.getScreenSize ().width - 200) / 2, (toolkit.getScreenSize ().height - 150) / 2);
	    setBackground (Color.green);
	    addWindowListener (new WindowAdapter ()
	    {
		public void windowClosing (WindowEvent e)
		{
		    setVisible (false);
		    dispose ();
		}
	    }
	    );
	    setVisible (true);
	}

	public void paint (Graphics g)
	{
	    g.setFont (new Font ("Helvetica", Font.BOLD, 14));
	    g.drawString ("Designed By", 50, 50);
	    g.drawString ("Wesley Oliver, RSA", 35, 85);
	    g.drawString ("Version 2.0", 50, 120);
	}

    }


    class processing extends Dialog implements ActionListener
    {
	FileDialog fileDir;
	TextField templete, publish, published, days;
	Button tempDir, publishDir, publishedDir, compile;
	TextArea Meets;
	String publishedMeets = "";
	processing (Frame f)
	{
	    super (f, "processing", true);
	    setSize (620, 400);
	    setLocation ((toolkit.getScreenSize ().width - 620) / 2, (toolkit.getScreenSize ().height - 400) / 2);
	    setBackground (Color.blue);
	    setFont (new Font ("Helvetica", Font.BOLD, 12));
	    addWindowListener (new WindowAdapter ()
	    {
		public void windowClosing (WindowEvent e)
		{
		    setVisible (false);
		}
	    }
	    );
	    setLayout (new BorderLayout (4, 4));
	    Panel north = new Panel ();
	    north.setSize (620, 20);
	    north.setLayout (new GridLayout (4, 2, 4, 0));
	    north.add (new Label ("The list of Compiled meets will be placed where"));
	    north.add (new Label ("This is the Directory all the results and compiled"));
	    north.add (new Label ("'<--WebResults-->' is found, in the selected templete"));
	    north.add (new Label ("data will be place"));
	    Panel top = new Panel ();
	    top.setLayout (new BorderLayout (3, 3));
	    top.add (new Label ("Templete"), "West");
	    templete = new TextField (Manager.installdir + "default.htm", 25);
	    templete.setEditable (false);
	    top.add (templete, "Center");
	    tempDir = new Button ("Fil");
	    tempDir.addActionListener (this);
	    top.add (tempDir, "East");
	    north.add (top);

	    Panel bot = new Panel ();
	    bot.setLayout (new BorderLayout (3, 3));
	    bot.add (new Label ("Publish Dir"), "West");
	    publish = new TextField (Manager.installdir + "Compiled\\", 25);
	    publish.setEditable (false);
	    bot.add (publish, "Center");
	    publishDir = new Button ("Dir");
	    publishDir.addActionListener (this);
	    bot.add (publishDir, "East");
	    north.add (bot);
	    north.add (new Label ("Compiling info:"));
	    add (north, "North");

	    Meets = new TextArea ();
	    add (Meets, "Center");

	    Panel Lsouth = new Panel ();
	    Lsouth.setLayout (new BorderLayout (3, 3));
	    Lsouth.add (new Label ("Published Meets"), "West");
	    published = new TextField ("", 25);
	    published.setEditable (false);
	    Lsouth.add (published, "Center");
	    publishedDir = new Button ("Fil");
	    publishedDir.addActionListener (this);
	    Lsouth.add (publishedDir, "East");
	    Panel South = new Panel ();
	    South.setLayout (new GridLayout (1, 2, 5, 0));
	    South.add (Lsouth, "South");
	    Panel Rsouth = new Panel ();
	    Rsouth.setLayout (new BorderLayout (3, 3));
	    Rsouth.add (new Label ("From"), "West");
	    Panel Center = new Panel ();
	    Center.setLayout (new BorderLayout (3, 3));
	    days = new TextField ("0", 5);
	    Center.add (days, "West");
	    Center.add (new Label (" days ago, '0' is all"), "East");
	    Rsouth.add (Center, "Center");
	    compile = new Button ("Compile");
	    compile.addActionListener (this);
	    Rsouth.add (compile, "East");
	    South.add (Rsouth);
	    add (South, "South");

	}


	public void actionPerformed (ActionEvent e)
	{
	    if (e.getSource () == tempDir)
	    {
		fileDir = new FileDialog (new Frame (), "Select Templete"); //unknow resons non of the method to adjust the screen
		//position of the file dialog work
		fileDir.setLocation ((toolkit.getScreenSize ().width - 400) / 2, (toolkit.getScreenSize ().height - 200) / 2);
		fileDir.setMode (FileDialog.LOAD);
		fileDir.setVisible (true);
		if (fileDir.getDirectory () != null) // && fileDir.getDirectory ().length () != 0)
		{
		    if (valiedate_templete () == true)
		    {
			templete.setText (fileDir.getDirectory () + fileDir.getFile ());
			Manager.templete = templete.getText ();
			Settings [3] = Manager.templete;
		    }
		    else
		    {
			JOptionPane.showMessageDialog (popup, "This file doesn't contain '<--WebResults-->' in it!\n Can't use this File,Default Templete will be used", "Error with Templete", JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	    else
	    {
		if (e.getSource () == publishDir)
		{
		    fileDir = new FileDialog (new Frame (), "Select Publish Directory"); //unknow resons non of the method to adjust the screen
		    //position of the file dialog work
		    fileDir.setLocation ((toolkit.getScreenSize ().width - 400) / 2, (toolkit.getScreenSize ().height - 200) / 2);
		    fileDir.setMode (FileDialog.SAVE);
		    fileDir.setDirectory (publish.getText ());
		    fileDir.setFile ("output directory");
		    fileDir.setVisible (true);
		    if (fileDir.getDirectory () != null) // && fileDir.getDirectory ().length () != 0)
		    {
			publish.setText (fileDir.getDirectory ());
			Manager.outputdir = publish.getText ();
			Settings [4] = Manager.outputdir;
		    }
		}
		else
		{
		    if (e.getSource () == publishedDir)
		    {
			fileDir = new FileDialog (new Frame (), "Select Compield meet File"); //unknow resons non of the method to adjust the screen
			//position of the file dialog work
			fileDir.setLocation ((toolkit.getScreenSize ().width - 400) / 2, (toolkit.getScreenSize ().height - 200) / 2);
			fileDir.setMode (FileDialog.LOAD);
			fileDir.setVisible (true);
			if (fileDir.getDirectory () != null) // && fileDir.getDirectory ().length () != 0)
			{
			    try
			    {
				readMeets ();
				published.setText (fileDir.getDirectory () + fileDir.getFile ());
				Manager.published = published.getText ();
				Settings [5] = Manager.published;
			    }
			    catch (Exception f)
			    {
				JOptionPane.showMessageDialog (popup, "There was an error reading the file,please cheack file it may be invalied", "Error on File", JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		    else
		    {
			if (compile.getLabel ().equals ("Compile"))
			{
			    try
			    {
				boolean addToIndex = true;
				if (publishedMeets.length () > 1)
				{
				    int option = JOptionPane.showConfirmDialog (popup, "Would you like to have the previous list of meets compiled\n index in the current compile's meet list", "Compiler",
					    JOptionPane.YES_NO_OPTION);
				    if (option == JOptionPane.YES_OPTION)
				    {
					addToIndex = true;
				    }
				    else
				    {
					addToIndex = false;
				    }
				}
				processWin.Meets.setText ("");
				int daysint = Integer.parseInt (days.getText ());
				Manager.compiler = new WebResults (DataBase);
				compile.setLabel ("Stop");
				Manager.compiler.meets (publish.getText (), templete.getText (), publishedMeets, daysint, addToIndex);

			    }
			    catch (NumberFormatException num)
			    {
				JOptionPane.showMessageDialog (popup, "You suppield an invalid number of days", "Error Date", JOptionPane.ERROR_MESSAGE);
				days.setText ("0");
			    }
			}
			else
			{
			    Manager.compiler.stopCompile ();
			}
		    }
		}
	    }
	}

	void readMeets () throws Exception
	{
	    processWin.Meets.setText ("");
	    processWin.Meets.append ("The List of Meets previously compiled meets\n----------------------------------------------------------\n\n");
	    publishedMeets = "";

	    BufferedReader in = new BufferedReader (new FileReader (fileDir.getDirectory () + fileDir.getFile ()));
	    String temp = in.readLine ();
	    while (temp != null)
	    {
		publishedMeets += temp;
		temp = in.readLine ();
	    }
	    StringTokenizer2 st = new StringTokenizer2 (publishedMeets, " ; "); //stringtokenizer keep on reverting back to a space a the detlim
	    while (st.hasMoreTokens ())
	    {
		st.nextToken ();
		processWin.Meets.append (st.nextToken () + ", Started on " + st.nextToken () + "\n");
		st.nextToken ();  //skip the next bits of info;
		st.nextToken ();
		st.nextToken ();
	    }
	    st = null;

	}

	boolean valiedate_templete ()
	{
	    try
	    {
		BufferedReader in = new BufferedReader (new FileReader (fileDir.getDirectory () + fileDir.getFile ()));
		String temp = in.readLine ();
		while (temp != null)
		{
		    if (temp.indexOf ("<--WebResults-->") != -1)
		    {
			return true;
		    }
		    temp = in.readLine ();
		}
		return false;
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (popup, "There was an error reading the file,please cheack file", "Error on File", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	}

    }
}


