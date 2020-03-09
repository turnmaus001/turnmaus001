package turnConTest.com.turn.vo;

public class ServiceIn {

	private Long ser_id;
	private Long id;
	private String name;
	private String s_name;
	private String money;
	private String time;
	private String note;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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

	/**
	 * @return the s_name
	 */
	public String getS_name() {
		return s_name;
	}

	/**
	 * @param s_name the s_name to set
	 */
	public void setS_name(String s_name) {
		this.s_name = s_name;
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

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public ServiceIn(Long id, String name, String s_name, String money, String time, String note) {
		super();
		this.id = id;
		this.name = name;
		this.s_name = s_name;
		this.money = money;
		this.time = time;
		this.note = note;
	}

	public ServiceIn() {
		super();
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

}
