<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.group16.proj.*"%>
<%@page import="java.util.*"%>
<%@page import="javax.servlet.*"%>
<html>

<head>
  <title>Avoid Border Wait Times</title>
  <meta http-equiv="Content-Type" content="text/html; charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <style>
	/* Always set the map height explicitly to define the size of the div
	 * element that contains the map. */
	#map {
	  height: 20%;
	  min-height: 150px;
	}
	/* Optional: Makes the sample page fill the window. */
	html, body {
	  height: 100%;
	  margin: 0;
	  padding: 0;
	}
	.top-buffer { margin-top:15px; }
	.btnTable{
	 background:none;
	 border:none;
	 margin:0;
	 padding:0;
	}
	.btnTable:hover{
	 color:#a0a0a0;
	}
</style>
</head>
<body onload="init(coords);">
	<div class="container-fluid">
	<h1 class="text-center"><a href="/Group16_FinalProject" style="color:inherit; text-decoration: none;">Border Wait Times</a></h1>
	</div>
	<div id="map"></div>
	<script>
	var coords = <%= request.getAttribute("curCoord") %>;
	var pathCoords = <%= request.getAttribute("path") %>;
	
	var markers = [];
	var map;
	
	function initMap() {
		var map = new google.maps.Map(document.getElementById('map'), {
		  center: coords,
		  zoom: 15
		});
		
		var route = new google.maps.Polyline({
          path: pathCoords,
          geodesic: true,
          strokeColor: '#4285f4',
          strokeOpacity: 0.70,
          strokeWeight: 7
        });
		
		if ((route.getPath().getLength()-1) > 0) {
			var markerA = new google.maps.Marker({
			  map: map,
			  draggable: false,
			  label: 'A',
			  position: {lat: coords.lat, lng: coords.lng}
			});
			var markerB = new google.maps.Marker({
			  map: map,
			  draggable: false,
			  label: 'B',
			  animation: google.maps.Animation.DROP,
		  	  position: route.getPath().getAt(route.getPath().getLength()-1)
			});
			
		} else {
			var marker = new google.maps.Marker({
			  map: map,
			  draggable: false,
			  animation: google.maps.Animation.DROP,
			  position: {lat: coords.lat, lng: coords.lng}
			});
		}

		var bounds = new google.maps.LatLngBounds();
    	bounds.extend(route.getPath().getAt(0));
    	bounds.extend(route.getPath().getAt(route.getPath().getLength()-1));
    	map.fitBounds(bounds);
		
		route.setMap(map);
     }
	
	</script>
   	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA6QonQNKz1E5ggJZPG9-eb21SZAoYqmCs&callback=initMap"
   	async defer></script>
	<div class="container">
		<div class="row top-buffer">
			<form class="col-md-12 col-lg-12 form-group" method="go" action="results">
				<select class="form-control text-center" name="location"
					onchange="if(this.value != null) { this.form.submit(); }">
					
					<option>SELECT A BORDER CROSSING</option>
					<option value=ABBOTSFORD_HUNTINGDON>Abbotsford-Huntingdon</option>
					<option value=ALDERGROVE>Aldergrove</option>
					<option value=AMBASSADOR_BRIDGE>Ambassador Bridge</option>
					<option value=BLUE_WATER_BRIDGE>Blue Water Bridge</option>
					<option value=BOUNDARY_BAY>Boundary Bay</option>
					<option value=CORNWALL>Cornwall</option>
					<option value=COUTTS>Coutts</option>
					<option value=DOUGLAS_PEACE_ARCH>Douglas (Peace Arch)</option>
					<option value=EDMUNDSTON>Edmundston</option>
					<option value=EMERSON>Emerson</option>
					<option value=FORT_FRANCES_BRIDGE>Fort Frances Bridge</option>
					<option value=NORTH_PORTAL>North Portal</option>
					<option value=PACIFIC_HIGHWAY>Pacific Highway</option>
					<option value=PEACE_BRIDGE>Peace Bridge</option>
					<option value=PRESCOTT>Prescott</option>
					<option value=QUEENSTON_LEWISTON_BRIDGE>Queenston-Lewiston Bridge</option>
					<option value=RAINBOW_BRIDGE>Rainbow Bridge</option>
					<option value=SAULT_STE_MARIE>Sault Ste. Marie</option>
					<option value=ST_STEPHEN>St. Stephen</option>
					<option value=ST_STEPHEN_3RD_BRIDGE>St. Stephen 3rd Bridge</option>
					<option value=STANSTEAD>Stanstead</option>
					<option value=ST_ARMAND_PHILIPSBURG>St-Armand/Philipsburg</option>
					<option value=ST_BERNARD_DE_LACOLLE>St-Bernard-de-Lacolle</option>
					<option value=THOUSAND_ISLANDS_BRIDGE>Thousand Islands Bridge</option>
					<option value=WINDSOR_AND_DETROIT_TUNNEL>Windsor and Detroit Tunnel</option>
					<option value=WOODSTOCK_ROAD>Woodstock Road</option>
				</select>
			</form>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-xs-7 col-sm-7 col-md-8 col-lg-8" id="location">
				<h4>Crossing: <strong> <%=(String)request.getAttribute("curLocation")%> </strong></h4>
			</div>
			<div class="col-xs-5 col-sm-5 col-md-4 col-lg-4" id="dist_to_next">
				<%
				String activated = (String) request.getAttribute("btnActivated");
				if (activated.equals("false")) { 
				%>
				<form method="get" action="results" style="display: block;">
					<button name="find_nearby" value="true" type="submit" class="btn btn-primary btn-md btn-block"
					>Find Next Nearby Crossing</button>
				</form>
				<%	} else if (activated.equals("true")){	%>
				<h4 class="text-right" style="display: block;"><strong> <%= (String)request.getAttribute("nextCrossing") %></strong></h4>
				<h4 class="text-right" style="display: block;">Distance to: <strong> <%= (String)request.getAttribute("distCrossing") %> km </strong></h4>
				<form method="get" action="results" style="display: block;">
					<button name="showing_nearby" value="true" type="submit" class="btn btn-primary btn-md btn-block"
					>View times here</button>
				</form>
				<%	} else { %>
				<h4 class="text-right" style="display: block; ">No crossings within 100 km</h4>
				<%
					}
				%>
			</div>
		</div>
		<div class="row top-buffer">
			<form class="col-xs-7 col-sm-7 col-md-8 col-lg-8 form-group" method="get" action="results">
				<select class="form-control" name="date"
					onchange="this.form.submit();">
					<%
					try{
							String[][] posDays = (String[][]) request.getAttribute("dates"); 
							String curDate = (String) request.getAttribute("curDate");
							for (int i = 0; i < 21; i++) {
								if (posDays[i][1].equals(curDate)){ 
					%>
					<option value="<%=posDays[i][1]%>" selected="selected"><%=posDays[i][0]%></option>
					<%	}else{	%>
					<option value="<%=posDays[i][1]%>"><%=posDays[i][0]%></option>
					<%	} } } catch (Exception e){ } %>
				</select>
			</form>
			<form class="col-xs-5 col-sm-5 col-md-4 col-lg-4 form-group" method="get" action="results" style="display: block;">
				<button name="show_today" value="true" type="submit" class="btn btn-default btn-md btn-block"
				>View Wait Times For Today</button>
			</form>
		</div>
	</div>	
	<div class="container" >
		<form method="go" action="results">
			<table class="table table-hover">
				<thead>
					<tr>
						<% int sO = -1; try{ sO = (Integer) request.getAttribute("sortDir"); } catch (Exception e) {} %>
						<% if(sO == -1 || sO == 0) { %>
						<th><button name="sort" value="interval_descending" type="submit" class="btnTable btn-block text-right">Interval  <span class="glyphicon glyphicon-chevron-down"></span></button></th>
						<% } else if(sO == 1) { %>
						<th><button name="sort" value="interval_ascending" type="submit" class="btnTable btn-block text-right">Interval  <span class="glyphicon glyphicon-chevron-up"></span></button></th>
						<% } else { %>
						<th><button name="sort" value="interval_ascending" type="submit" class="btnTable btn-block text-right">Interval</button></th>
						<% } %>
						<th>Estimated Wait Times</th>
						<% if(sO == 2) { %>
						<th><button name="sort" value="wait_time_ascending" type="submit" class="btnTable btn-block text-left">Details  <span class="glyphicon glyphicon-chevron-down"></span></button></th>
						<% } else if(sO == 3) { %>
						<th><button name="sort" value="wait_time_descending" type="submit" class="btnTable btn-block text-left">Details  <span class="glyphicon glyphicon-chevron-up"></span></button></th>
						<% } else { %>
						<th><button name="sort" value="wait_time_descending" type="submit" class="btnTable btn-block text-left">Details</button></th>
						<% } %>
						
					</tr>
				</thead>
				<tbody>
					<%
					try{
						DisplayTime[] displayTimes = (DisplayTime[]) request.getAttribute("DisplayTimes");
						for (int i = 0; i < displayTimes.length; i++) {
							DisplayTime timeLine = displayTimes[i];
					%>
					<tr>
						<td class="text-right col-xs-2"><%=timeLine.forTimeString%> -</td>
						<td>
						<div class="progress" style="margin-bottom: 0 !important">
					  		<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" 
					  			style="width:<%=timeLine.wTimePerc%>%"></div>
						</div>
						</td>
						<td class="col-xs-2"><%=timeLine.wTimeString%></td>
					</tr>
					<% } } catch (Exception e){ } %>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>