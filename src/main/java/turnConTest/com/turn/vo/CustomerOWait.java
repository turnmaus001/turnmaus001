package turnConTest.com.turn.vo;

public class CustomerOWait {

	private String name;
	private String waitTime;
	private String app;
	private String warning;
	private String[] service; 
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
	/**
	 * @return the service
	 */
	public String[] getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String[] service) {
		this.service = service;
	}
	/**
	 * @return the waitTime
	 */
	public String getWaitTime() {
		return waitTime;
	}
	/**
	 * @param waitTime the waitTime to set
	 */
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	/**
	 * @return the app
	 */
	public String getApp() {
		return app;
	}
	/**
	 * @param app the app to set
	 */
	public void setApp(String app) {
		this.app = app;
	}
	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}
	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}
	
}
