<%@page import="za.co.tms.util.*" %> 
<!DOCTYPE html>
<html>
   <head>
      <meta charset="ISO-8859-1">
      <title>TMS Assessment</title>
      <script>var SUPER_GLOBAL_JS_HOST_NAME = "<%= SuperGlobal.HOST_NAME %>";</script>
      <script> var fleetActiveRanges = ''</script>
   </head>
   <body>
      <img id="imgCompanyLogo" src="../resources/tms_logo_2022_dark.png" style="display: block; margin-left: auto; margin-right: auto; padding-bottom: 20px; width: 350px; padding-top:100px">
      <div style="text-align: center;padding-top: 35px;">
         <label style="font-family: system-ui;font-weight: 600;">Fleet ID</label> </br><input type="text" id="txtidFleet">
      </div>
      <div style="text-align: center;padding-top: 35px;">
         <button style="width: 210px; height: 33px;" class="btn" type="button" onClick="getActiveRanges()"><span class="btn_getActiveRanges">Get Fleet Active Ranges</span></button>
      </div>
      <div style="text-align: center;padding-top: 35px;">
         <textarea id="txtActiveRanges" rows="15" cols="50" disabled>
         </textarea>
      </div>
      <script type="text/javascript" src="../resources/lib/jquery-2.2.0.min.js"></script>
      <script type="text/javascript" src="../resources/lib/jquery-ui.js"></script>
      
      <script type="text/javascript">
      
      //TODO: Unimplemented population methods
      function getActiveRanges() {
    	  var fleetID = document.getElementById("txtidFleet").value;
    	  $.ajax({
    		    url: SUPER_GLOBAL_JS_HOST_NAME + "/homeService/homeService/fleetActiveRanges" + "/" + fleetID,
    		    type: 'GET',
    		    headers: {
    		    	"Accept" : "application/json",
    		    	"Content-Type": "application/json"
  		        },
    		    success: function(data) {
    		    	var result = data.data;
    		    	var transferTime = '';
    		    	if(result!=undefined && result.length > 0) {
    		    		for(var i=0;i<=result.length-1;i++) {	
        		    		if(i == result.length-1) {
            		    		transferTime += "From" + " " + result[result.length-1].transferTime + " To" + " " + "CURRENT";
            		    	} else {
            		    		transferTime += "From" + " " + result[i].transferTime + " To" + " " +result[i+1].transferTime + "\n";
            		    	}
        		    	}
    		    		document.getElementById("txtActiveRanges").innerHTML = transferTime;
    		    	}
    		    	
    		    },
    		    error: function(jqxhr, status, errorMsg) {
    		    	document.getElementById("txtActiveRanges").innerHTML = "Failed To Get Fleet Active Ranges";
    		    }
    		});
      }
      
      </script>
   </body>
</html>