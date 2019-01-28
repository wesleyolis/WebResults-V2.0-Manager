//please read the terms and conditions of the www.strandaquatics.za.net site before proceeding
window.status = "Downloading.... Filter Functions";
//Varibles
res = "";
first = true;
showing = false;
tevents = 0;
LoadWin = null;
time = 0;
ares = new Array ();
evnt = new Array ();
numres = 0;
timeout = false;
run = false;

function size (s, t)
{for (i = s.toString ().length ; i < t ; i++){
s = s + "&nbsp;";}
return s;
}

function span (s, t)
{s = "<span style='Width:" + (t * 10) + ";'>" + s + "</span>";
return s;
}

//writes the filters in header
function fil ()
{
parent.h.document.writeln ("<html><head><title>Filters<\/title><\/head><body bgcolor='#4698D3'  topmargin='0' leftmargin='0'>");
parent.h.document.write ("<form name='F'><table border='0' width='640' cellspacing='0' cellpadding='0'><tr><td align='center'><table border='0' width='590' cellspacing='4' cellpadding='0'><tr><td colspan ='2'><b>Session </b><select onchange='parent.page()' name='sess' style='font-weight: 700;'><option value='-1' selected>All<\/option>");
ses = -1;
for (i = 0 ; i < (parent.events.length - 2) ; i = i + 7)
{if (ses != parent.events [i])
{parent.h.document.write ("<option value='" + parent.events [i] + "'>" + parent.events [i] + "</option>");
ses = parent.events [i];
}}
parent.h.document.write ("<\/select><\/td><td colspan='3' align='left'><b>Clubs&nbsp; <\/b><font size='1'><select size='1' name='Club' onchange='parent.page()' style='font-weight: 700;position:relative; width:300'><option selected>All Clubs<\/option>");
for (i = 0 ; i < (parent.clubs.length - 2) ; i = i + 2)
{parent.h.document.write ("<option value='" + parent.clubs [i + 1] + "'>" + parent.clubs [i] + "&nbsp;&nbsp;&nbsp;" + parent.clubs [i + 1] + "</option>");}
parent.h.document.write ("</select></font><b>&nbsp;&nbsp;<button name='Print' style='font-size: 10pt; font-weight: bold; width: 50px; height:25px' onclick='parent.m.focus();parent.m.window.print(2)'>Print</button></b></td></tr><tr><td width='36'><b>&nbsp;Age</b></td><td width='95'><select size='1' name='Age' style='font-weight: 700; width:95; height:21' onchange='parent.page()'><option selected>All</option>");
for (i = 6 ; i < 26 ; i++)
{parent.h.document.write ("<option>" + i + "<\/option>");}
parent.h.document.write ("</select></td><td width='132px'><b>Stroke&nbsp;&nbsp;&nbsp;</b><font size='1'><select size='1' onchange='parent.page()' name='stroke' style='font-weight: 700; position:relative'><option selected>All</option><option>Free</option><option>Back</option><option>Breast</option><option>Fly</option><option>Medley</option></select></font></td><td align='left' width='150px'><b>Distance </b><font size='1'><select size='1' onchange='parent.page()' name='dis' style='font-weight: 700; width:60; height:25'><option value='All'>All</option><option value='50'>50</option><option value='100'>100</option><option value='200'>200</option><option value='400'>400</option><option value='500'>500</option><option  value='800'>800</option><option value='1500'>1500</option></select></font></td><td align='left'><b><input type='checkbox' onclick='parent.page()' name='rel' value='1' checked>Include Relays</b></td></tr><tr><td width='36'><b>Show</b></td><td width='95'><b><select size='1' onchange='parent.page()' name='show' style='font-weight: 700; width:94; height:22'><option selected>Both Sex</option><option>Female</option><option>Male</option></select></b></td><td colspan='2'><b>Include&nbsp; <select size='1' name='top' onchange='if(parent.h.document.F.Age.selectedIndex!=0 || parent.h.document.F.Club.selectedIndex!=0){parent.page()}'  style='font-weight: 700'><option selected>0</option>");
for (i = 1 ; i < 11 ; i++){parent.h.document.write ("<option>" + i + "<\/option>");}
parent.h.document.write ("\n</select>&nbsp; top places in </b><font size='1'><select size='1' name='filter' onchange='if(parent.h.document.F.Age.selectedIndex!=0 || parent.h.document.F.Club.selectedIndex!=0){parent.page()}'  style='font-weight: 700; position:relative'><option>events</option><option selected>filter</option></select></font></td><td><b><input type='checkbox' onclick='parent.page()' name='fin' value='1' checked> Include Finals</b></td></tr><tr><td colspan='2'><b><button name='Jump' style='font-size: 10pt; font-weight: bold; width: 135px; height: 25px' onclick='parent.m.document.all.Events.style.pixelLeft=80;parent.m.window.scroll(0,0);'>Jump to Event &gt;&gt;</button></b></td><td colspan='2'><b>Include&nbsp; <select size='1' name='around' onchange='if(parent.h.document.F.Age.selectedIndex!=0 || parent.h.document.F.Club.selectedIndex!=0){parent.page()}'  style='font-weight: 700; width: 45; height: 22'><option selected>0</option><option>1</option><option>2</option><option>3</option><option>4</option></select>&nbsp; before and after filtered</b></td><td><b><input type='checkbox' onclick='parent.page()' name='pre' value='1' checked>Include Prelims</b></td></tr></table></tr></tr></table></form><\/body><\/html>");
    parent.h.document.close ();
}
function gen (s){if (s == "F"){s = "Female"}else{if (s == "M"){s = "Male"}else{if (s == "X"){s = "Mixed"}
else{s = "-";}}}return s;}function stk (s){if (s == 1){return "Freestyle";}if (s == 2){
return "Backstroke";}if (s == 3){return "Breaststroke";}if (s == 4){return "Butterfly";}if (s == 5){return "Medley";}}
function type2 (t)
{if (t == "I"){return "Individual";}else{return "Relay"}}
function chk (age)
{sel = (parent.h.document.F.Age.selectedIndex + 5);end = (age % 100);be = (age - end) / 100;if (age < 99 & sel <= age){return true;}
else{if (end == 99 & be < sel){return true;}else{if ((((be == end) & ((be - 1) < sel)) || ((be - 1) < sel)) & end >= sel){
return true;}else{return false;}}}}
window.status = "Downloading.... Filter Functions 25%";
function age (s)
{
    switch (s)
    {
	case 6:
	    s = "6 & Under";
	    break;
	case 8:
	    s = "8 & Under";
	    break;
	case 10:
	    s = "10 & Under";
	    break;
	case 1599:
	    s = "15 & Over";
	    break;
	case 99:
	    s = "Open  ";
	    break;
	case 708:
	    s = "7 - 8";
	    break;
	case 910:
	    s = "9 - 10";
	    break;
	case 1112:
	    s = "11 - 12";
	    break;
	case 1314:
	    s = "13 - 14";
	    break;
	case 1516:
	    s = "15 - 16";
	    break;
	case 1718:
	    s = "17 - 18";
	    break;
	case 808:
	    s = "8 - 8";
	    break;
	case 909:
	    s = "9 - 9";
	    break;
	case 1010:
	    s = "10 - 10";
	    break;
	case 1111:
	    s = "11 - 11";
	    break;
	case 1212:
	    s = "12 - 12";
	    break;
	case 1313:
	    s = "13 - 13";
	    break;
	case 1414:
	    s = "14 - 14";
	    break;
	case 1515:
	    s = "15 - 15";
	    break;
	case 1616:
	    s = "16 - 16";
	    break;
	case 1717:
	    s = "17 - 17";
	    break;
	case 1818:
	    s = "18 - 18";
	    break;
	default:
	    s = age2 (s);
	    break;
    }


    return s;
}

function age2 (s)
{
    s = s.toString ();
    if (s < 99)
    {
	s += " & Under";
    }


    else
    {
	if ((s % 100) == 99)
	{
	    s = (s.replace ("99", "")) + " & Over";
	}
	else
	{
	    s = Math.round (s / 100) + " - " + (s % 100);
	}
    }


    return s;
}

function init ()
{
    numres = ((parent.res.length - 2) / 3) + ((parent.relay.length - 2) / 9);
    athres ();
    page2 ();
}

function page ()
{
	if(timeout ==false)
	{
	timeout = true;
	run = true;
	setTimeout("wait()",2500);
	}else
	{
	run = false;
	setTimeout("wait()",2500);
	}
	
}

function wait()
{
	if(run)
	{
	page2();
	}
	else
	{
	run = true;
	}
}

function page2 ()
{

timeout = false;
run = false;
    time = 0;
    loadWin (0, 0, 250, 150, "Filter Status");
    LoadWin.document.write ("<p align='center'><font size='5'>Processing Filter<\/font><\/p><p align='center'><form name = 'per'>*&nbsp;&nbsp;&nbsp;&nbsp;<input type='text' name='p' size='1' style=\"font-size: 16pt; font-weight: bold; border-style: solid; border-width: 0; padding: 0; background-color:#4698D3; \">&nbsp;&nbsp;*</form></p><p align='center'><font size='5'>Always more advanced<\/font><\/p><body><html>");
    t = new Date; //timer start
    evntdet ();
    parent.m.document.clear ();
    parent.m.document.write ("<html><head><Title>Results</Title><style><!--a{text-decoration:none;color:#000000;}a:hover{color:#FFFFFF;}--></style></head><body bgcolor='#4698D3'  style='background-attachment:fixed' topmargin='0' leftmargin='0'><table border='0' width='600px' cellspacing='0' cellpadding='0'><tr><td align='left'>");
    jump ();
    //procees all events out start
    tmpres = "";
    pos = 0;
    rpos = 0;
    epos = 0;
    ent = 0;
    areres=false;
    for (e = 0 ; e < events.length - 2 ; e += 7, ent++)
    {

	if (evnt [ent] == true)
	{
	areres=false;
	    tmpres = "";
	    LoadWin.document.per.p.value = Math.round ((e / (events.length - 2)) * 100) + " %";
	    if (parent.events [e + 6] == "I")
	    {
		while (parent.loc [epos] == ent)
		{
		    if ((parent.h.document.F.fin.checked == true & parent.loc [epos + 1] == 0) || (parent.h.document.F.pre.checked == true & parent.loc [epos + 1] == 1) || parent.loc [epos + 1] == 2)
		    {
			tmpst = result2 (pos, parent.loc [epos + 2], 0);
			if(tmpst.length > 263)
			{
			areres=true;
			tmpres += "<div style='border-bottom-style: solid; border-bottom-width: 1px; padding-bottom: 1px'><p align='center'><b>" + parent.type [parent.loc [epos + 1]] + "</b></div></p>"+  tmpst;
			}
		    }
		    pos += (parent.loc [epos + 2] * 3);
		    epos += 3;

		}

	    }
	    else
	    {

		// if (parent.h.document.F.rel.checked == true & parent.events [e + 6] == "R") //process it if a relay
		// {
		    areres=true;
		//r += relay (pos, pos + (parent.events [e] * 21));
		// while (parent.loc [epos] == ent)
		// {
		// parent.m.document.write ("<div style='border-bottom-style: solid; border-bottom-width: 1px; padding-bottom: 1px'><p align='center'><b>" + parent.type [parent.loc [epos + 1]] + "</b></div></p>");
		// alert ("relay " + rpos + " : " + (parent.loc [epos + 2] * 9) + " : " + epos);
		tmpres = relay2 (rpos, parent.loc [epos + 2]);
		rpos += (parent.loc [epos + 2] * 9);
		epos += 3;

		// }
		// }
	    }


		parent.m.document.write ("<p align='center'><b><span style='background-color: #FFFFFF;Width:600;'><A name='e" + e + "'>" + size (parent.events [e + 1], 10) + size (gen (parent.events [e + 2]), 10) + size (age (parent.events [e + 3]), 18) + size (parent.events [e + 4], 7) + size (stk (parent.events [e + 5]), 18) + type2 (parent.events [e + 6]) + "</A></span></b></p>");
		if (areres==false)
		{
		    parent.m.document.write ("<p align='center'><b>No Results for Filter</b></p>");
		}
		else
		{
		    parent.m.document.write (tmpres);

		}
	    
	}
	else
	{
	    if (parent.events [e + 6] != "R")
	    {
		while (parent.loc [epos] == ent)
		{

		    pos += (parent.loc [epos + 2] * 3);
		    epos += 3;
		}
	    }
	    else
	    {
		rpos += (parent.loc [epos + 2] * 9);
		epos += 3;
	    }
	}
    }


    parent.m.document.write ("</p><br><br></td></tr><t/able></body></html>");
    Winclose ();
    parent.m.document.close ();
    parent.m.focus ();
    t2 = new Date;
    mm = (t2.getMilliseconds () - t.getMilliseconds ());
    ss = (t2.getSeconds () - t.getSeconds ());
    if (mm < 0)
    {
	ss--;
	mm = (1 - mm);
    }


    if (ss < 0)
    {
	ss = (60 + ss);
    }


    window.status = "Processing Finshed in " + ss + " Seconds & " + mm + " Milli-Seconds, of " + numres + " results";
}


window.status = "Downloading.... Filter Functions 50%";

function evntdet ()  //filters the events to determine a list to be writen out
{
    if (parent.h.document.F.show.selectedIndex == 1)
    {
	Sex = "F"
    }


    else
    {
	Sex = "M"
    }


    ok = false;
    for (e = 0, ev = 0, epos = 0 ; e < (parent.events.length - 2) ; e += 7, ev++)
    {

	if (parent.h.document.F.sess.selectedIndex == 0 || parent.h.document.F.sess.value == parent.events [e])
	{
	    ok = false;
	    if (parent.events [e + 6] == "I")
	    {
		while (parent.loc [epos] == ev)
		{
		    if ((parent.h.document.F.fin.checked == true & parent.loc [epos + 1] == 0) || (parent.h.document.F.pre.checked == true & parent.loc [epos + 1] == 1) || parent.loc [epos + 1] == 2)
		    {
			ok = true;
		    }
		    epos += 3;

		}
	    }
	    else
	    {
		epos += 3;
	    } //(parent.h.document.F.rel.checked == false & parent.events [e + 6] == "I")
	    if ((parent.h.document.F.show.selectedIndex == 0 || parent.events [e + 2] == Sex || parent.events [e + 2] == "X") & (parent.h.document.F.stroke.selectedIndex == 0 || parent.h.document.F.stroke.selectedIndex == parent.events [e + 5]) & (parent.h.document.F.dis.selectedIndex == 0 || parent.h.document.F.dis.value == parent.events [e + 4]) & ((parent.h.document.F.rel.checked == true & parent.events [e + 6] == "R") || ok == true) & (parent.h.document.F.Age.selectedIndex == 0 || chk (parent.events [e + 3]) == true) || (parent.h.document.F.filter.selectedIndex == 0 & parent.h.document.F.Age.selectedIndex == 0))
	    {
		evnt [ev] = true;
	    }
	    else
	    {
		evnt [ev] = false;
	    }
	}
	else
	{
	    evnt [ev] = false;
	    while (parent.loc [epos] == ev)
	    {
		epos += 3
	    }
	}
    }
}


function jump ()  // makes the jump 2 list
{
    b = "";
    parent.m.document.write ("<div style='position: absolute; width: 470px; z-index: 1; left: -510; top: 0px;' id='Events'><table cellpadding='0' cellspacing='0' width='461' height='200'><tr><td width='456'><table cellpadding='0' cellspacing='0' border='0' width='456' height='200'><tr><td><img alt='' width='0' height='1' src='images/MsSpacer.gif'></td><td><img alt='' width='0' height='1' src='images/MsSpacer.gif'></td><td height='1'><img alt='' width='0' height='1' src='images/MsSpacer.gif'></td></tr><tr><td><img alt='' src='images/MsoPnl_Cnr_tl_104BD.gif' width='15' height='19'></td><td bgcolor='#003399' nowrap><a herf='' onclick=\"document.all.Events.style.pixelLeft='-510'\"><b><font color='#FFFFFF'>" + size ("", 32) + "Click here on bar to close" + size ("", 32) + "</font></b></a></td><td height='19'><img alt='' src='images/MsoPnl_Cnr_tr_104BF.gif' width='15' height='19'></td></tr><tr><td bgcolor='#0066CC' valign='top' align='left' colspan='3' height='200'><p>");
    ses = 0;
    for (e = 0, ev = 0 ; e < (parent.events.length - 2) ; e += 7, ev++)
    {
	if (parent.h.document.F.sess.selectedIndex == 0 || parent.h.document.F.sess.value == parent.events [e])
	{
	    if (ses != parent.events [e])
	    {
		b = b + "</p><p align='center'><b>Session:" + parent.events [e] + "</b></p><p style='margin-left:15px'>";
		ses = parent.events [e];
	    }
	    if (evnt [ev] == true)
	    {
		b += "<A href='#e" + e + "' onclick=\"document.all.Events.style.pixelLeft='-510'\"><b>" + span (parent.events [e + 1], 6);
		b += span (gen (parent.events [e + 2]), 6);
		b += span (age (parent.events [e + 3]), 9) + span (parent.events [e + 4] + "m", 5) + span (stk (parent.events [e + 5]), 10) + type2 (parent.events [e + 6]) + "</b></A><br>";

	    }
	    parent.m.document.write (b);
	    b = "";
	}
    }


    parent.m.document.write ("</p></td></tr><tr><td width='15'><img alt=''src='images/MsoPnl_Cnr_bl_104C1.gif' width='15' height='19'></td><td nowrap bgcolor='#003399'width='426'><a herf='' onclick=\"document.all.Events.style.pixelLeft='-510'\"><b><fontcolor='#FFFFFF'>" + size ("", 32) + "Click here on bar to close" + size ("", 32) + "</font></b></a></td><td height='19' width='15'><img alt='' src='images/MsoPnl_Cnr_br_104C3.gif' width='15' height='19'></td></tr></table></td><td height='200' width='5'><img alt='' width='5' height='100%' src='images/MsoPnl_sh_b_104BC.gif'></td></tr><tr><td colspan='2' height='5'><img alt='' width='460' height='5' src='images/MsoPnl_sh_r_104BB.gif'></td></tr></table></div>");
}


window.status = "Downloading.... Filter Functions 75%";

function result2 (pos, num, e)
{
    d = "<div align='Left'><table border='0' cellspacing='0' cellpadding='0' width='600px'><tr><td width='30'></td><td width='180'></td><td width='180'></td><td width='90'></td><td width='30'></td><td width='30' align='left'></td><td align='right'></td></tr>";
    end = pos + (num * 3);
    begin = pos + (parent.h.document.F.top.selectedIndex * 3);
    if (parent.h.document.F.Age.selectedIndex > 0 || parent.h.document.F.Club.selectedIndex > 0 || (parent.h.document.F.show.selectedIndex > 0 & parent.events [e + 2] == "X"))
    {
	//alert ("Is");
	num = num - parent.h.document.F.top.selectedIndex;
	mch = new Array ();
	mchl = new Array ();
	if ((parent.h.document.F.Club.selectedIndex != 0 & parent.h.document.F.Age.selectedIndex != 0))
	{
	    for (p = begin, c = 10 ; p < end ; p += 3, c++)
	    {
		if ((parent.h.document.F.Club.selectedIndex * 2) == ((parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 2) & (parent.h.document.F.Age.selectedIndex + 5) == (parent.ath [(parent.res [p + 1] * 1) + 4]))
		{
		    match (c);
		}
	    }
	}
	else
	{
	    if (parent.h.document.F.Club.selectedIndex != 0)
	    {
		for (p = begin, c = 10 ; p < end ; p += 3, c++)
		{
		    if ((parent.h.document.F.Club.selectedIndex * 2) == ((parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 2))
		    {
			match (c);
		    }
		}
	    }
	    else
	    {
		for (p = begin, c = 10 ; p < end ; p += 3, c++)
		{
		    if ((parent.h.document.F.Age.selectedIndex + 5) == (parent.ath [(parent.res [p + 1] * 1) + 4]))
		    {
			match (c);
		    }
		}
	    }
	}
	if (parent.h.document.F.top.selectedIndex != 0)
	{
	    for (p = pos ; p < pos + (parent.h.document.F.top.selectedIndex * 3) & p < end & parent.res [p] <= parent.h.document.F.top.selectedIndex ; p += 3) //does the top X
	    {
		d += "<tr><td><b>" + parent.res [p] + "</b></td><td><b>" + (parent.ath [parent.res [p + 1]]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 1]) + "</b></td><td><b>" + (parent.clubs [(parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 1]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 3]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 4]) + "</b></td><td><b>" + parent.res [p + 2] + "</b></td></tr>";
	    }
	    d += "<tr><td colspan='7'><hr noshade style='border-style: dashed; border-width: 2px' color='#000000'></td></tr>";
	}


	if (parent.h.document.F.around.selectedIndex != 0)
	{
	    for (p = pos + (parent.h.document.F.top.selectedIndex * 3), c = 10 ; p < end ; p += 3, c++)  //prints the filted res
	    {
		if (mch [c] == true)
		{
		    if (mchl [c] != true)
		    {
			d += "<tr><td><b>" + parent.res [p] + "</b></td><td><b>" + (parent.ath [parent.res [p + 1]]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 1]) + "</b></td><td><b>" + (parent.clubs [(parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 1]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 3]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 4]) + "</b></td><td><b>" + parent.res [p + 2] + "</b></td></tr>";
		    }
		    else
{d += "<tr bgColor='6866FD'><td><b>" + parent.res [p] + "</b></td><td><A href='javascript:parent.disath(" + parent.res [p + 1] + ");'><b>" + (parent.ath [parent.res [p + 1]]) + "</b></a></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 1]) + "</b></td><td><b>" + (parent.clubs [(parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 1]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 3]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 4]) + "</b></td><td><b>" + parent.res [p + 2] + "</b></td></tr>";}}}}else{for (p = pos + (parent.h.document.F.top.selectedIndex * 3), c = 10 ; p < end ; p += 3, c++)          {if (mch [c] == true){d += "<tr><td><b>" + parent.res [p] + "</b></td><td><A href='javascript:parent.disath(" + parent.res [p + 1] + ");'><b>" + (parent.ath [parent.res [p + 1]]) + "</b></a></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 1]) + "</b></td><td><b>" + (parent.clubs [(parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 1]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 3]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 4]) + "</b></td><td><b>" + parent.res [p + 2] + "</b></td></tr>";}}}
}else{for (p = pos ; p < end ; p += 3){d += "<tr><td><b>" + parent.res [p] + "</b></td><td><A href='javascript:parent.disath(" + parent.res [p + 1] + ");'><b>" + (parent.ath [parent.res [p + 1]]) + "</b></a></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 1]) + "</b></td><td><b>" + (parent.clubs [(parent.ath [(parent.res [p + 1] * 1) + 2] * 1) + 1]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 3]) + "</b></td><td><b>" + (parent.ath [(parent.res [p + 1] * 1) + 4]) + "</b></td><td><b>" + parent.res [p + 2] + "</b></td></tr>";}}d += "</table></div>";return d;}
function match (c){mchl [c] = true;mch [c] = true;for (r = c - 1 ; r > (c - parent.h.document.F.around.selectedIndex) - 1 ; r--){mch [r] = true;if (mch [r - 1] == true){break;}}
for (r = c + 1 ; r < (c + parent.h.document.F.around.selectedIndex + 1) ; r++){mch [r] = true;if (mch [r + 1] == true){break;}}}
function athres (){for (r = 0 ; r < parent.ath.length ; r += 1){ares [r] = new Array ();ares [r] [0] = 0;}r = 0;
for (e = 0 ; e < (parent.loc.length -2); e += 3){if (parent.events [((loc [e] * 7) + 6)] == "I"){end = (r + (loc [e + 2] * 3));
for (; r < end ; r += 3){p = res [r + 1];ares [p] [(ares [p] [0] + 1)] = (loc [e] * 7);ares [p] [(ares [p] [0] + 2)] = r;ares [p] [(ares [p] [0] + 3)] = loc [e + 1];ares [p] [0] += 3;}}}}
function athressort (a)
{for (s = 1 ; s < ares [a] [0] ; s += 3){for (s2 = s + 3 ; s2 < ares [a] [0] + 1 ; s2 += 3)
{st = (parent.events [ares [a] [s] + 5] - parent.events [ares [a] [s2] + 5]);st2 = (parent.events [ares [a] [s] + 4] - parent.events [ares [a] [s2] + 4]);
if (st > 0 || (st == 0 & st2 > 0) || (st == 0 & st2 == 0 & (ares [a] [s + 2] - ares [a] [s2 + 2]) > 0))
{for (sw = 0 ; sw < 3 ; sw++){t = ares [a] [s2 + sw]; ares [a] [s2 + sw] = ares [a] [s + sw];
ares [a] [s + sw] = t;}}}}}function disjump (j)
{parent.m.document.location.href = "#e" + j;Winclose ();parent.m.focus ();}
function disath (ath){LoadWin = null;loadWin (10, 10, 420, 500, "Swimmer Results", true);
LoadWin.document.write ("<table border='0' width='380' cellspacing='0' cellpadding='0'><tr><td><b>Name: " + parent.ath [ath] + ", " + parent.ath [ath + 1] + "<br>Gender: " + gen (parent.ath [ath + 3]) + "&nbsp;&nbsp; Age: " + parent.ath [ath + 4] + "<br>Club: " + parent.clubs [parent.ath [ath + 2]] + "</b></td><td align='right' valign='top'>&nbsp;</td><td align='right' valign='top'><b><font size='4'><a href='javascript:parent.focus();parent.window.print(2)'>Print</a></font></b><br>&nbsp;</td></tr></table><br><table border='0' width='380' cellspacing='0'>");
athressort (ath);strk = 0;for (q = 1 ; q < ares [ath] [0] + 1 ; q += 3)
{if (strk != parent.events [ares [ath] [q] + 5]){
strk = parent.events [ares [ath] [q] + 5];
LoadWin.document.write ("<tr><td></td><td colspan = '2' align='left'><b>" + stk (parent.events [ares [ath] [q] + 5]) + "<b></td><td colspan='3'><Font Size='2'><b>pos</b></Font></td></tr>");}
if (evnt [(ares [ath] [q] / 7)] == true & ((parent.h.document.F.fin.checked == true & ares [ath] [q + 2] == 0) || (parent.h.document.F.pre.checked == true & ares [ath] [q + 2] == 1) || parent.loc [epos + 1] == 2))
{LoadWin.document.write ("<tr><td width ='50px' align ='center'><A href=\"javascript:opener.disjump(" + ares [ath] [q] + ");\" ><b>" + parent.events [ares [ath] [q] + 1] + "</b></A></td><td width ='50px'>" + parent.events [ares [ath] [q] + 4] + "m</td><td width ='60px'>" + parent.type [ares [ath] [q + 2]] + "</td><td width ='40px'>" + parent.res [ares [ath] [q + 1]] + "</td><td width ='80px'>" + parent.res [ares [ath] [q + 1] + 2] + "</td><td>" + age (parent.events [ares [ath] [q] + 3]) + "</td></tr>");}
else{LoadWin.document.write ("<tr><td width ='50px' align ='center'>" + parent.events [ares [ath] [q] + 1] + "</td><td width ='50px'>" + parent.events [ares [ath] [q] + 4] + "m</td><td width ='60px'>" + parent.type [ares [ath] [q + 2]] + "</td><td width ='40px'>" + parent.res [ares [ath] [q + 1]] + "</td><td width ='80px'>" + parent.res [ares [ath] [q + 1] + 2] + "</td><td>" + age (parent.events [ares [ath] [q] + 3]) + "</td></tr>");}}
LoadWin.document.write ("</table><br><br>");LoadWin.document.close ();
}function relay2 (posi, numn)
{endn = posi + (numn * 9);d = "";
for (i = posi ; i < endn ; i = i + 9){d += "<div align='Left'><table border='0' width='600' cellspacing='0' cellpadding='0'><tr bgColor='#99CCFF'><td><b>Position: " + parent.relay [i] + "</b></td><td width='250'><b>" + parent.relay [i + 1] + " - " + parent.relay [i + 2] + "</b></td><td width='60'><b>" + parent.relay [i + 3] + "</b></td><td width='60'><b>" + parent.relay [i + 4] + "</b></td></tr><tr><td><b>" + parent.ath [parent.relay [i + 5]] + "</b></td><td><b>" + parent.ath [parent.relay [i + 5] + 1] + "</b></td><td><b>" + gen (parent.ath [parent.relay [i + 5] + 3]) + "</b></td><td><b>" + parent.ath [parent.relay [i + 5] + 4] + "</b></td></tr><tr><td><b>" + parent.ath [parent.relay [i + 6]] + "</b></td><td><b>" + parent.ath [parent.relay [i + 6] + 1] + "</b></td><td><b>" + gen (parent.ath [parent.relay [i + 6] + 3]) + "</b></td><td><b>" + parent.ath [parent.relay [i + 6] + 4] + "</b></td></tr><tr><td><b>" + parent.ath [parent.relay [i + 7]] + "</b></td><td><b>" + parent.ath [parent.relay [i + 7] + 1] + "</b></td><td><b>" + gen (parent.ath [parent.relay [i + 7] + 3]) + "</b></td><td><b>" + parent.ath [parent.relay [i + 7] + 4] + "</b></td></tr><tr><td><b>" + parent.ath [parent.relay [i + 8]] + "</b></td><td><b>" + parent.ath [parent.relay [i + 8] + 1] + "</b></td><td><b>" + gen (parent.ath [parent.relay [i + 8] + 3]) + "</b></td><td><b>" + parent.ath [parent.relay [i + 8] + 4] + "</b></td></tr></table></div><br>";
}return d;}
function relay (pos, end)
{d = "";for (i = pos ; i < end ; i = i + 21){
d += "<div align='Left'><table border='0' width='600' cellspacing='0' cellpadding='0'><tr bgColor='#99CCFF'><td><b>Position: " + parent.results [i] + "</b></td><td width='250'><b>" + parent.results [i + 1] + " - " + parent.results [i + 2] + "</b></td><td width='60'><b>" + parent.results [i + 3] + "</b></td><td width='60'><b>" + parent.results [i + 4] + "</b></td></tr><tr><td><b>" + parent.results [i + 5] + "</b></td><td><b>" + parent.results [i + 6] + "</b></td><td><b>" + gen (parent.results [i + 7]) + "</b></td><td><b>" + parent.results [i + 8] + "</b></td></tr><tr><td><b>" + parent.results [i + 9] + "</b></td><td><b>" + parent.results [i + 10] + "</b></td><td><b>" + gen (parent.results [i + 11]) + "</b></td><td><b>" + parent.results [i + 12] + "</b></td></tr><tr><td><b>" + parent.results [i + 13] + "</b></td><td><b>" + parent.results [i + 14] + "</b></td><td><b>" + gen (parent.results [i + 15]) + "</b></td><td><b>" + parent.results [i + 16] + "</b></td></tr><tr><td><b>" + parent.results [i + 17] + "</b></td><td><b>" + parent.results [i + 18] + "</b></td><td><b>" + gen (parent.results [i + 19]) + "</b></td><td><b>" + parent.results [i + 20] + "</b></td></tr></table></div><br>";
}return d;}
function loadWin (l, m, w, h, t, s)
{if(s){sc = ",scrollbars=yes"}
else{sc = ",scrollbars=no"}
LoadWin = window.open ("", "", "width=" + w + ",height=" + h + ",Left=" + ((screen.Width / 2) - (w / 2)) + ",Top=" + ((screen.Height / 2) - (h / 2)) + sc);
LoadWin.document.write ("<html><head><Title>" + t + "</Title><style><!--a{text-decoration:none;color:#000000;}a:hover{color:#FFFFFF;}--></style></head><body bgcolor='#4698D3' topmargin='" + m + "' leftmargin='" + l + "'><body><html>");}
function Winclose ()
{LoadWin.close ();LoadWin = null;}
window.status = "Downloaded Filter Functions";
