package turnConTest.com.turn.vo;

import java.util.ArrayList;

public class ChooseSerOut {
	private String msg;
	private String alert;
	
	ArrayList<TableConvert> tbcover;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the alert
	 */
	public String getAlert() {
		return alert;
	}

	/**
	 * @param alert the alert to set
	 */
	public void setAlert(String alert) {
		this.alert = alert;
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
}
