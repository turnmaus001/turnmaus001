package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class ServiceListTV {
	private ArrayList<CustomerOWait> waiting;
	private ArrayList<CustomerOSer> service;
	private String refresh;
	private String salonName;
	/**
	 * @return the waiting
	 */
	public ArrayList<CustomerOWait> getWaiting() {
		return waiting;
	}
	/**
	 * @param waiting the waiting to set
	 */
	public void setWaiting(ArrayList<CustomerOWait> waiting) {
		this.waiting = waiting;
	}
	/**
	 * @return the service
	 */
	public ArrayList<CustomerOSer> getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(ArrayList<CustomerOSer> service) {
		this.service = service;
	}
	/**
	 * @return the refresh
	 */
	public String getRefresh() {
		return refresh;
	}
	/**
	 * @param refresh the refresh to set
	 */
	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}
	/**
	 * @return the salonName
	 */
	public String getSalonName() {
		return salonName;
	}
	/**
	 * @param salonName the salonName to set
	 */
	public void setSalonName(String salonName) {
		this.salonName = salonName;
	}
	
}
