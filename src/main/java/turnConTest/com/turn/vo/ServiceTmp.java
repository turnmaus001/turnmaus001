package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class ServiceTmp {
	private ArrayList<String> sevice;
	private ArrayList<String> name;
	private String ca_name;
	/**
	 * @return the name
	 */
	public ArrayList<String> getName() {
		return name;
	}
	/**
	 * @return the sevice
	 */
	public ArrayList<String> getSevice() {
		return sevice;
	}
	/**
	 * @param sevice the sevice to set
	 */
	public void setSevice(ArrayList<String> sevice) {
		this.sevice = sevice;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(ArrayList<String> name) {
		this.name = name;
	}
	/**
	 * @return the money
	 */
	public Long getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(Long money) {
		this.money = money;
	}
	/**
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Long time) {
		this.time = time;
	}
	private Long money;
	private Long time;
	/**
	 * @return the ca_name
	 */
	public String getCa_name() {
		return ca_name;
	}
	/**
	 * @param ca_name the ca_name to set
	 */
	public void setCa_name(String ca_name) {
		this.ca_name = ca_name;
	}
}
