package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class ServiceList {
	private ArrayList<CustomerIn> reward;
	private ArrayList<TableConvert> tbcover;
	private ArrayList<CustomerIn> waiting;
	/**
	 * @return the reward
	 */
	public ArrayList<CustomerIn> getReward() {
		return reward;
	}
	/**
	 * @param reward the reward to set
	 */
	public void setReward(ArrayList<CustomerIn> reward) {
		this.reward = reward;
	}
	/**
	 * @return the tbcover
	 */
	public ArrayList<TableConvert> getTbcover() {
		return tbcover;
	}
	/**
	 * @param tbcover the tbcover to set
	 */
	public void setTbcover(ArrayList<TableConvert> tbcover) {
		this.tbcover = tbcover;
	}
	/**
	 * @return the waiting
	 */
	public ArrayList<CustomerIn> getWaiting() {
		return waiting;
	}
	/**
	 * @param waiting the waiting to set
	 */
	public void setWaiting(ArrayList<CustomerIn> waiting) {
		this.waiting = waiting;
	}
	/**
	 * @return the inservice
	 */
	public ArrayList<CustomerIn> getInservice() {
		return inservice;
	}
	/**
	 * @param inservice the inservice to set
	 */
	public void setInservice(ArrayList<CustomerIn> inservice) {
		this.inservice = inservice;
	}
	private ArrayList<CustomerIn> inservice;
}
