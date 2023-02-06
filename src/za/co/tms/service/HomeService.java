package za.co.tms.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.tms.dao.FleetDAO;
import za.co.tms.dao.FleetTransferLogDAO;
import za.co.tms.obj.Fleet;
import za.co.tms.obj.FleetTransferLog;
import za.co.tms.util.DateTransformerUtil;

@Path(value = "/homeService")
public class HomeService {
	
	//Example get request service and DAO usage
	@Path("getAllFleets")
	@GET
	@Produces("application/json")
	public Response getAllFleets() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		
		List<Fleet> fleets = new FleetDAO().getAll(Fleet.class);
		if(fleets != null && fleets.size() > 0) {
			for(Fleet fleet: fleets) {
				jsonObject = new JSONObject();
				jsonObject.put("id", fleet.getId());
				jsonObject.put("alias", fleet.getAlias());
				jsonObject.put("idCompany", fleet.getIdCompany());
				jsonObject.put("make", fleet.getMake());
				jsonObject.put("model", fleet.getModel());
				jsonObject.put("yearModel", fleet.getYearModel());
				jsonObject.put("createdTime", fleet.getCreatedTime());
				
				jsonArray.put(jsonObject);            
			}
		} else {
			return Response.status(204).entity("-1").build();
		}
		
		jsonObject = new JSONObject();
		jsonObject.put("data", jsonArray);
		
		return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	//Depending on port and server setup, this is an example of the access URL: "http://localhost:8080/TMS_Assessment/command/home" which can be found under the SuperGlobal class
	//You can include as many sub methods as you would like
	//Please make use of the already included DAO functions provided
	@Path("/fleetActiveRanges/{fleetID}")
	@GET
	@Produces("application/json")
	public Response fleetActiveRanges(@PathParam("fleetID") String fleetID) throws JSONException, ParseException {
		System.out.println("Fleet ID:"+fleetID);
		
		List<FleetTransferLog> fleetTransferLog = new FleetTransferLogDAO().findByField(FleetTransferLog.class, "idFleet", fleetID);
		Map<Long, FleetTransferLog> fleetTransferLogMap = new HashMap<>();
		
		String fleetTableIdsQuery = "";

		if(fleetTransferLog != null) {
			for(int i=0;i<fleetTransferLog.size();i++) {
				if(i != fleetTransferLog.size()-1) {
					fleetTableIdsQuery += fleetTransferLog.get(i).getId() + ",";
				} else {
					fleetTableIdsQuery += fleetTransferLog.get(i).getId();
				}
				
				fleetTransferLogMap.put(fleetTransferLog.get(i).getId(), fleetTransferLog.get(i));          
			}
		}
		
		List<Fleet> fleets = queryFleetTable(fleetTableIdsQuery);
		
		JSONObject jsonObject = mapResponse(fleets,fleetTransferLogMap);
		
		return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	private List<Fleet> queryFleetTable(String fleetTableIdsQuery) {
		return new FleetDAO().searchMultipleValues(Fleet.class, "id", fleetTableIdsQuery);
		
	}
	
	private JSONObject mapResponse(List<Fleet> fleets, Map<Long, FleetTransferLog> fleetTransferLogMap) throws JSONException, ParseException {
		JSONObject jsonObject;
		JSONArray jsonArray = new JSONArray();
		
		for(Fleet f : fleets) {
			if(!f.getAlias().contains("TRFD")) {
				FleetTransferLog fltLog = new FleetTransferLog();
				jsonObject = new JSONObject();
				jsonObject.put("id", fleetTransferLogMap.get(f.getId()).getId());
				jsonObject.put("alias", fleetTransferLogMap.get(f.getId()).getFleetAlias());
				jsonObject.put("idOriginFleet", fleetTransferLogMap.get(f.getId()).getIdOriginFleet());
				jsonObject.put("idFleet", fleetTransferLogMap.get(f.getId()).getIdFleet());
				jsonObject.put("idCompany", fleetTransferLogMap.get(f.getId()).getIdCompany());
				jsonObject.put("transferTime", DateTransformerUtil.removeTimeStamp(fleetTransferLogMap.get(f.getId()).getTransferTime()));
				jsonObject.put("createdTime", fleetTransferLogMap.get(f.getId()).getCreatedTime());
				
				jsonArray.put(jsonObject);
			}
		}
		jsonObject = new JSONObject();
		jsonObject.put("data", jsonArray);
		
		return jsonObject;
	}
	
}
