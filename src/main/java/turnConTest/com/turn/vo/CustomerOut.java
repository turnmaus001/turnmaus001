package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class CustomerOut {

	private String service;
	private String phone;
	private String money;
	private String more;
	private ArrayList<SeveiceItem> serviceList;
	/**
	 * @return the serviceList
	 */
	public ArrayList<SeveiceItem> getServiceList() {
		return serviceList;
	}
	/**
	 * @param serviceList the serviceList to set
	 */
	public void setServiceList(ArrayList<SeveiceItem> serviceList) {
		this.serviceList = serviceList;
	}
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @return the more
	 */
	public String getMore() {
		return more;
	}
	/**
	 * @param more the more to set
	 */
	public void setMore(String more) {
		this.more = more;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the money
	 */
	public String getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(String money) {
		this.money = money;
	}

	
}
