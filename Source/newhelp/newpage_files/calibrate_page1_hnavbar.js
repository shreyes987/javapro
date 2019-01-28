//©Xara Ltd
if(typeof(loc)=="undefined"||loc==""){var loc="";if(document.body&&document.body.innerHTML){var tt=document.body.innerHTML;var ml=tt.match(/["']([^'"]*)calibrate_page1_hnavbar.js["']/i);if(ml && ml.length > 1) loc=ml[1];}}

var bd=0
document.write("<style type=\"text/css\">");
document.write("\n<!--\n");
document.write(".calibrate_page1_hnavbar_menu {z-index:999;border-color:#000000;border-style:solid;border-width:"+bd+"px 0px "+bd+"px 0px;background-color:#494be3;position:absolute;left:0px;top:0px;visibility:hidden;}");
document.write(".calibrate_page1_hnavbar_plain, a.calibrate_page1_hnavbar_plain:link, a.calibrate_page1_hnavbar_plain:visited{text-align:left;background-color:#494be3;color:#ffffff;text-decoration:none;border-color:#000000;border-style:solid;border-width:0px "+bd+"px 0px "+bd+"px;padding:2px 0px 2px 0px;cursor:hand;display:block;font-size:8pt;font-family:Arial, Helvetica, sans-serif;font-weight:bold;}");
document.write("a.calibrate_page1_hnavbar_plain:hover, a.calibrate_page1_hnavbar_plain:active{background-color:#a4a5f1;color:#000000;text-decoration:none;border-color:#000000;border-style:solid;border-width:0px "+bd+"px 0px "+bd+"px;padding:2px 0px 2px 0px;cursor:hand;display:block;font-size:8pt;font-family:Arial, Helvetica, sans-serif;font-weight:bold;}");
document.write("\n-->\n");
document.write("</style>");

var fc=0x000000;
var bc=0xa4a5f1;
if(typeof(frames)=="undefined"){var frames=0;}

startMainMenu("calibrate_page1_hnavbar_left.gif",20,50,2,0,0)
mainMenuItem("calibrate_page1_hnavbar_b1",".gif",20,120,"general.html","questions","General" ,2,2,"");
mainMenuItem("calibrate_page1_hnavbar_b2",".gif",20,120,"reducer.html","questions","Reducer",2,2,"");
mainMenuItem("calibrate_page1_hnavbar_b3",".gif",20,120,"cd.html","questions","Circuit Designer",2,2,"");

endMainMenu("calibrate_page1_hnavbar_right.gif",20,50)




loc="";
