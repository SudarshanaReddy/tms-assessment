package za.co.tms.obj;

public class FleetTransferLog {
	private Long id;
	private Long idOriginFleet;
	private Long idFleet;
	private Long idCompany;
	private String fleetAlias;
	private String transferTime;
	private String createdTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdOriginFleet() {
		return idOriginFleet;
	}
	public void setIdOriginFleet(Long idOriginFleet) {
		this.idOriginFleet = idOriginFleet;
	}
	public Long getIdFleet() {
		return idFleet;
	}
	public void setIdFleet(Long idFleet) {
		this.idFleet = idFleet;
	}
	public Long getIdCompany() {
		return idCompany;
	}
	public void setIdCompany(Long idCompany) {
		this.idCompany = idCompany;
	}
	public String getFleetAlias() {
		return fleetAlias;
	}
	public void setFleetAlias(String fleetAlias) {
		this.fleetAlias = fleetAlias;
	}
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
}
