package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class ServiceTmp {
	private ArrayList<String> name;
	/**
	 * @return the name
	 */
	public ArrayList<String> getName() {
		return name;
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
}
