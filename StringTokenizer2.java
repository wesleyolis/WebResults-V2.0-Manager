// Wesley
public class StringTokenizer2
{
    private static String data, detlim;
    private static int pos = 0;
    StringTokenizer2 (String str, String det)
    {
	data = str;
	detlim = det;
	pos = 0;
    }


    static boolean hasMoreTokens ()
    {
	if (data.indexOf (detlim, pos+1) != -1)
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }


    static String nextToken ()
    {
	String str = null;
	int l = data.indexOf (detlim, pos+1);
	str = data.substring ((pos + detlim.length ()), l);
	pos = l;
	return str;
    }
} // StringTokenizer class
