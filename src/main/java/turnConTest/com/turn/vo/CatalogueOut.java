package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class CatalogueOut {
	private Long ser_id;
	private String name;
	ArrayList<ServiceOut> service;
	public CatalogueOut(long id, String name) {
		// TODO Auto-generated constructor stub
		this.ser_id = id;
		this.name = name;
	}
	/**
	 * @return the service
	 */
	public ArrayList<ServiceOut> getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(ArrayList<ServiceOut> service) {
		this.service = service;
	}

	/**
	 * @return the ser_id
	 */
	public Long getSer_id() {
		return ser_id;
	}
	/**
	 * @param ser_id the ser_id to set
	 */
	public void setSer_id(Long ser_id) {
		this.ser_id = ser_id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
