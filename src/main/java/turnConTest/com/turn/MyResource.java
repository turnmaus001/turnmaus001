package turnConTest.com.turn;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import turnConTest.com.turn.vo.CatalogueIn;
import turnConTest.com.turn.vo.CatalogueOut;
import turnConTest.com.turn.vo.ChooseSerOut;
import turnConTest.com.turn.vo.CommonInfo;
import turnConTest.com.turn.vo.Customer;
import turnConTest.com.turn.vo.CustomerIn;
import turnConTest.com.turn.vo.CustomerIn2;
import turnConTest.com.turn.vo.CustomerOSer;
import turnConTest.com.turn.vo.CustomerOWait;
import turnConTest.com.turn.vo.CustomerOut;
import turnConTest.com.turn.vo.DateLogin2;
import turnConTest.com.turn.vo.DateLoginIn;
import turnConTest.com.turn.vo.Employee;
import turnConTest.com.turn.vo.ServiceIn;
import turnConTest.com.turn.vo.ServiceList;
import turnConTest.com.turn.vo.ServiceListTV;
import turnConTest.com.turn.vo.ServiceOut;
import turnConTest.com.turn.vo.ServiceTmp;
import turnConTest.com.turn.vo.Setting;
import turnConTest.com.turn.vo.SeveiceItem;
import turnConTest.com.turn.vo.TableConvert;
import turnConTest.com.turn.vo.WorkHis;

/**
 * Root resource (exposed at "myresource" path)
 * http://localhost:8080/com.turn/api/chicago/2016-08-27
 */
@Path("/")
public class MyResource {
	LocalDateTime aDateTime = LocalDateTime.of(2018, Month.OCTOBER, 31, 19, 30, 40);
	private static final String AUTHENTICATION_SCHEME = "Basic";
	LocalDateTime now = LocalDateTime.now();
	HashMap<String, Employee> employee = new HashMap<String, Employee>();
	HashMap<String, Employee> employeeT = new HashMap<String, Employee>();
	ArrayList<ArrayList<Employee>> arrOfArrEmployee = new ArrayList<ArrayList<Employee>>();
	public Setting seting = new Setting();
	public static String password;
	public static final boolean loginCheck = false;

	DateTimeFormatter dtfL = DateTimeFormatter.ofPattern("yy:MM:dd");
	public static final String SLIP_CHR = "$$$";
	public static final String SLIP_CHR_RV = "\\$\\$\\$";
	public static final String SLIP_CHR2 = "****";
	public static final String SLIP_CHR2_RV = "\\*\\*\\*\\*";
	public static final String SLIP_CHR3 = "++++";
	public static final String SLIP_CHR_RV3 = "\\+\\+\\+\\+";

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getEmployee(@Context HttpHeaders httpheaders) {
		//
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL != 3) {
			employee = EmployeeDAO.getEmployee();
			if (employee.size() == 0) {
				Connection con = null;
				Statement stmt = null;
				try {
					con = DBUtil.getConnection();
					LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
					String formattedDate = dtfL.format(checkIn);
					stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT vl from dataturn where datet=\'" + formattedDate + "\'");
					employee = EmployeeDAO.clearEmployee();
					if (rs.next()) {
						Object obj = new JSONParser().parse(rs.getString(1));
						JSONObject jo = (JSONObject) obj;
						JSONArray ja = (JSONArray) jo.get("detail");
						if (ja == null) {
							String s = "{\"status\":";
							if (checkL == 1)
								s += true;
							else
								s += false;
							return s + "}";
						}
						for (Object o : ja) {
							if (o instanceof JSONObject) {
								parseEmployeeObject((JSONObject) o, formattedDate, true);
							}
						}
					}
				} catch (URISyntaxException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) { // TODO Auto-generated catch
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						con.close();
						// stmt.close();
					} catch (SQLException e) {
					}
				}
				return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, true);
			}
			return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, false);
		}
		return "[]";
		// }
	}

	@GET
	@Path("/login/")
	public String authenticate(@Context HttpHeaders httpheaders) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL != 3) {
			employee = EmployeeDAO.getEmployee();
			if (employee.size() == 0) {
				Connection con = null;
				Statement stmt = null;
				try {
					con = DBUtil.getConnection();

					/*
					 * System.out.println(checkIn.toString()); int timeNew = checkOut.getHour() * 60
					 * + checkOut.getMinute(); int timeOld = checkIn.getHour() * 60 +
					 * checkIn.getMinute(); System.out.println(timeNew - timeOld);
					 */

					LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
					String formattedDate = dtfL.format(checkIn);
					/*
					 * DateTimeFormatter formatter =
					 * DateTimeFormatter.ofPattern("yy:MM:dd HH:mm:ss"); LocalDateTime working =
					 * LocalDateTime.parse(formattedDate + " " + seting.getDaily(), formatter);
					 */

					stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT vl from dataturn where datet=\'" + formattedDate + "\'");
					employee = EmployeeDAO.clearEmployee();
					if (rs.next()) {
						Object obj = new JSONParser().parse(rs.getString(1));
						JSONObject jo = (JSONObject) obj;
						JSONArray ja = (JSONArray) jo.get("detail");
						if (ja == null) {
							String s = "{\"status\":";
							if (checkL == 1)
								s += true;
							else
								s += false;
							return s + "}";
						}
						for (Object o : ja) {
							if (o instanceof JSONObject) {
								parseEmployeeObject((JSONObject) o, formattedDate, true);
							}
						}
					}
				} catch (URISyntaxException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) { // TODO Auto-generated catch
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						con.close();
						// stmt.close();
					} catch (SQLException e) {
					}
				}
				return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, true);
			}
			return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, false);
		}
		return "{\"error\": \"loginFailed\"}";
	}

	private int checkLogin(String token) {
		final String encodedUserPassword = token.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		// Decode username and password
		String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		if (seting.getPass() == null || seting.getPass().isEmpty()) {
			getSetting();
		}
		if (("admin".equals(username) && seting.getPass().equals(password))
				|| ("tan".equals(username) && "1812".equals(password))) {
			return 1;
		} else if ("viewer".equals(username) && "123".equals(password)) {
			return 2;
		}
		return 3;
	}

	@GET
	@Path("/updatePass/{oldPass}/{newPass}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updatePass(@Context HttpHeaders httpheaders, @PathParam("oldPass") String oldPass,
			@PathParam("newPass") String newPass) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 1) {
			if (!seting.getPass().equals(oldPass)) {
				return "{\"error\": \"oldPassNotCorrect\"}";
			}
			Connection con = null;
			Statement stmt = null;
			try {
				con = DBUtil.getConnection();
				stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO turnSetting (name, value) VALUES ('admin','2608') "
						+ "ON CONFLICT (name) DO UPDATE SET value = " + "'" + newPass + "'");

				seting.setPass(newPass);
			} catch (URISyntaxException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) { // TODO Auto-generated catch
				e.printStackTrace();
			} finally {
				try {
					con.close();
					// stmt.close();
				} catch (SQLException e) {
				}
			}
			return "{\"sucess\": \"OK\"}";
		} else if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		}
		return "{\"error\": \"notLogin\"}";
	}

	@GET
	@Path("/updateRole/{role}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateRole(@Context HttpHeaders httpheaders, @PathParam("role") String role) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 1) {

			Connection con = null;
			Statement stmt = null;
			try {
				con = DBUtil.getConnection();
				stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO turnSetting (name, value) VALUES ('role','1') "
						+ "ON CONFLICT (name) DO UPDATE SET value = " + "'" + role + "'");
				seting.setSecurity(role);
			} catch (URISyntaxException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) { // TODO Auto-generated catch
				e.printStackTrace();
			} finally {
				try {
					con.close();
					// stmt.close();
				} catch (SQLException e) {
				}
			}
			return "{\"sucess\": \"OK\"}";
		} else if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		}
		return "{\"error\": \"notLogin\"}";
	}

	@GET
	@Path("/viewPass/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String viewpass(@Context HttpHeaders httpheaders, @PathParam("id") String id) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 1) {

			Employee employee1 = EmployeeDAO.getEmployee(id);
			return "{\"sucess\": \"" + employee1.getPass() + "\"}";
		} else if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		}
		return "{\"error\": \"notLogin\"}";
	}

	private void getSetting() {
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name, value from turnSetting");
			String name = "";
			while (rs.next()) {
				name = rs.getString("name");
				if (name.equals("admin")) {
					seting.setPass(rs.getString("value"));
				} else if ((name.equals("role"))) {
					seting.setSecurity(rs.getString("value"));
				} else if ((name.equals("numTV"))) {
					seting.setTvNum(rs.getString("value"));
				} else if ((name.equals("tReward"))) {
					seting.settReward(rs.getString("value"));
				} else if ((name.equals("salonID"))) {
					seting.setSalonID(rs.getString("value"));
				} else if ((name.equals("turn"))) {
					seting.setTurn(rs.getString("value"));
				} else if ((name.equals("daily"))) {
					seting.setDaily(rs.getString("value"));
				} else if ((name.equals("weeken"))) {
					seting.setWeeken(rs.getString("value"));
				} else if ((name.equals("covert"))) {
					seting.setCovert(rs.getString("value"));
				} else if ((name.equals("cvType"))) {
					seting.setCovertType(rs.getString("value"));
				} else if ((name.equals("refresh"))) {
					seting.setRefresh(rs.getString("value"));
				} else if ((name.equals("warning"))) {
					seting.setWarning(rs.getString("value"));
				} else if ((name.equals("salonN"))) {
					seting.setSalonName(rs.getString("value"));
				}
			}
		} catch (URISyntaxException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				con.close();
				// stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	private void getSetting(Connection con) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT name, value from turnSetting");
			String name = "";
			while (rs.next()) {
				name = rs.getString("name").trim();
				//System.out.println(name);
				if (name.equals("admin")) {
					seting.setPass(rs.getString("value"));
				} else if ((name.equals("role"))) {
					seting.setSecurity(rs.getString("value"));
				} else if ((name.equals("numTV"))) {
					seting.setTvNum(rs.getString("value"));
				} else if ((name.equals("tReward"))) {
					seting.settReward(rs.getString("value"));
				} else if ((name.equals("salonID"))) {
					seting.setSalonID(rs.getString("value"));
				} else if ((name.equals("turn"))) {
					seting.setTurn(rs.getString("value"));
				} else if ((name.equals("daily"))) {
					seting.setDaily(rs.getString("value"));
				} else if ((name.equals("weeken"))) {
					seting.setWeeken(rs.getString("value"));
				} else if ((name.equals("covert"))) {
					seting.setCovert(rs.getString("value"));
				} else if ((name.equals("cvType"))) {
					seting.setCovertType(rs.getString("value"));
				} else if ((name.equals("refresh"))) {
					seting.setRefresh(rs.getString("value"));
				} else if ((name.equals("warning"))) {
					seting.setWarning(rs.getString("value"));
				} else if ((name.equals("salonN"))) {
					seting.setSalonName(rs.getString("value"));
				}
			}
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// http://localhost:8080/com.turn/api/addUser/abff
	@GET
	@Path("/addUser/{userName}/{pass}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String addEmployee(@Context HttpHeaders httpheaders, @PathParam("userName") String userName,
			@PathParam("pass") String pass) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 1) {
			if (EmployeeDAO.isSameName(userName)) {
				return "{\"error\": \"sameName\"}";
			}
			employee = EmployeeDAO.getEmployee();
			LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
			String formattedDate = dtfL.format(checkIn);
			if (employee.size() == 0) {
				Connection con = null;
				Statement stmt = null;
				try {
					con = DBUtil.getConnection();
					stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT vl from dataturn where datet=\'" + formattedDate + "\'");
					employee = EmployeeDAO.clearEmployee();
					if (rs.next()) {
						Object obj = new JSONParser().parse(rs.getString(1));
						JSONObject jo = (JSONObject) obj;
						JSONArray ja = (JSONArray) jo.get("detail");
						if (ja != null) {
							for (Object o : ja) {
								if (o instanceof JSONObject) {
									parseEmployeeObject((JSONObject) o, formattedDate, true);
								}
							}
						}

					}
				} catch (URISyntaxException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) { // TODO Auto-generated catch
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						con.close();
						// stmt.close();
					} catch (SQLException e) {
					}
				}
			}
			employee = EmployeeDAO.addEmployee(userName, pass);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy:MM:dd HH:mm:ss");
			LocalDateTime working;
			if (checkIn.getDayOfWeek().equals(DayOfWeek.SUNDAY))
				working = LocalDateTime.parse(formattedDate + " " + seting.getWeeken(), formatter);
			else
				working = LocalDateTime.parse(formattedDate + " " + seting.getDaily(), formatter);
			int timeStandar = working.getHour() * 60 + working.getMinute();
			int timeLogin = checkIn.getHour() * 60 + checkIn.getMinute();
			int diff = timeLogin - timeStandar;
			if (diff > 0) {
				int maxId = 0;
				for (String emp : employee.keySet()) {
					if (Integer.parseInt(emp) > maxId)
						maxId = Integer.parseInt(emp);
				}
				addGroupSub( Integer.toString(maxId), "Comming late", Double.valueOf(diff), "0", "1", pass, "1",null, null, null);
			}

			return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, false);
		} else if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		}
		return "{\"error\": \"notLogin\"}";
	}

	// http://localhost:8080/com.turn/api/addGroup/{name}/{money}/{free}
	@GET
	@Path("/addGroup/{id}/{name}/{money}/{free}/{over}/{pass}/{more}/{phone}/{seviceid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String addGroup(@Context HttpHeaders httpheaders, @PathParam("id") String id, @PathParam("name") String name,
			@PathParam("money") double money, @PathParam("free") String free, @PathParam("over") String over,
			@PathParam("pass") String pass, @PathParam("more") String more, @PathParam("phone") String phone,
			@PathParam("seviceid") String seviceId) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		return addGroupSub(id, name, money, free, over, pass, "0", more, phone, seviceId);
	}

	private String addGroupSub(String id, String name, double money, String free, String over, String pass,
			String overTime, String more,  String phone, String seviceId) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		Employee employee1 = EmployeeDAO.getEmployee(id);
		if (seting.getPass() == null)
			getSetting();
		if (!seting.getPass().equals(pass) && !employee1.getPass().equals(pass)) {
			if (!"0".equals(seting.getSecurity()))
				return "{\"error\": \"notCorrPass\"}";
		}
		if ("0".equals(over)) {
			LocalDateTime checkOut = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
			LocalDateTime checkIn = employee1.getLstTime();
			int timeNew = checkOut.getHour() * 60 + checkOut.getMinute();
			int timeOld = checkIn.getHour() * 60 + checkIn.getMinute();
			if ((timeNew - timeOld) < money) {
				return "{\"error\": \"inValid\", \"msg\": \"Time is not ENOUGH. You start working at "
						+ dtf.format(employee1.getLstTime()) + "!!!\"}";
			}
		}
		// check null
		int index = 1;
		for (int i = 0; i < employee1.getTurnListD().size(); i++) {
			int tmp = Integer.parseInt(employee1.getTurnListD().get(i).getId());
			if (tmp > index) {
				index = tmp;
			}
		}
		index++;
		employee1.getTurnListD()
				.add(new WorkHis(name, money, "1".equals(free) ? true : false, Integer.toString(index),
						dtf.format(Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime()),
						"0".equals(overTime) ? dtf.format(employee1.getLstTime()) : null));
		if ("0".equals(free)) {
			employee1.setTotalTurn(employee1.getTotalTurn() + money);
		}
		employee1.setTotal(employee1.getTotal() + money);
		employee1.setIsWorking(false);
		employee1.setLstTime(null);
		employee = EmployeeDAO.addEmployee(id, employee1);
		/////////////
		if(more != null) {
			ArrayList<String> colVa = new ArrayList<String>();
			ArrayList<String> colVaCus = new ArrayList<String>();
			Connection con = null;
			try {
				con = DBUtil.getConnection();
				
				if("0".equals(more)) {
					colVa.add("completedid = completedid || '" + SLIP_CHR + seviceId + "'");
					colVa.add("completedmoney = completedmoney || '" + SLIP_CHR + money + "'" );
					colVa.add("completedempolyee = completedempolyee || '" + SLIP_CHR2 + seviceId + SLIP_CHR+  employee1.getEmpName() + "'" );
					colVa.add("status = '0'");
					colVa.add("cometime = '" + getTime() + "'");
				} else {
					colVa.add("status = '2'");
					colVaCus.add("point = point + " + CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
							Long.parseLong(getIndateByPhone(con, "money", phone)), seting.getCovertType()));
					updateCus(con, colVaCus, "phone", phone);
				    // id$$$employee****id$$$employee
					String completedempolyee = getIndateByPhone(con, "completedempolyee", phone);
					String serviceid = getIndateByPhone(con, "serviceid", phone);
					if(completedempolyee !=null && !completedempolyee.isEmpty()) {
						colVa.add("completedempolyee = completedempolyee || '" + SLIP_CHR2 + seviceId + SLIP_CHR+  employee1.getEmpName() + "'" );
						colVa.add("completedmoney = completedmoney || '" + SLIP_CHR + money + "'" );
						insertHis(con, phone, completedempolyee + SLIP_CHR2 + seviceId + SLIP_CHR+  employee1.getEmpName() , getTime());
					} else {
						colVa.add("completedempolyee = completedempolyee || '" + SLIP_CHR2 + serviceid + SLIP_CHR +  employee1.getEmpName() + "'" );
						colVa.add("completedmoney = money " );
						insertHis(con, phone,  SLIP_CHR2 + serviceid + SLIP_CHR+  employee1.getEmpName() , getTime());
					}
				}
				updateIndate(con, colVa, "phone", phone);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(con !=null)
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/upGroup/{id}/{groupid}/{name}/{money}/{free}/{pass}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateGroup(@Context HttpHeaders httpheaders, @PathParam("id") String id,
			@PathParam("groupid") String groudid, @PathParam("name") String name, @PathParam("money") double money,
			@PathParam("free") String free, @PathParam("pass") String pass) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		Employee employee1 = EmployeeDAO.getEmployee(id);
		if (!seting.getPass().equals(pass) && !employee1.getPass().equals(pass)) {
			return "{\"error\": \"notCorrPass\"}";
		}
		// check null
		int index = -1;
		for (int i = 0; i < employee1.getTurnListD().size(); i++) {
			if (employee1.getTurnListD().get(i).getId().equals(groudid)) {
				index = i;
				break;
			}
		}
		WorkHis wk = employee1.getTurnListD().get(index);
		if ("0".equals(free) && !wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() + money - wk.getMoney());
		} else if ("0".equals(free) && wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() + money);
		} else if ("1".equals(free) && !wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() - wk.getMoney());
		}
		employee1.setTotal(employee1.getTotal() + money - wk.getMoney());
		wk.setId(groudid);
		wk.setMoney(money);
		wk.setName(name);
		wk.setMoney(money);
		wk.setTurn(free.equals("1") ? true : false);
		employee1.getTurnListD().remove(index);
		employee1.getTurnListD().add(wk);
		employee1.setIsWorking(false);
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/delGroup/{id}/{groupId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String delGroup(@Context HttpHeaders httpheaders, @PathParam("id") String id,
			@PathParam("groupId") String groudid) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		Employee employee1 = EmployeeDAO.getEmployee(id);
		// check null
		int index = -1;
		for (int i = 0; i < employee1.getTurnListD().size(); i++) {
			if (employee1.getTurnListD().get(i).getId().equals(groudid)) {
				index = i;
				break;
			}
		}
		WorkHis wk = employee1.getTurnListD().get(index);
		if (!wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() - wk.getMoney());
		}
		employee1.setTotal(employee1.getTotal() - wk.getMoney());
		employee1.getTurnListD().remove(index);
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/usingReward/{phone}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object usingReward(@Context HttpHeaders httpheaders, @PathParam("phone") String phone) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		CommonInfo cmIn = new CommonInfo();
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			ArrayList<String> colVa = new ArrayList<String>();
			colVa.add("reward = '1'");
			boolean flag = updateIndate(con, colVa, "phone", phone);
			//getIndateByPhone(con, column, phone);
			//insertrewardChoose(con, phone);
			if (flag) {
				cmIn.setStatus("OK");
			}
			return cmIn;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				return cmIn;
			}
		}
	}

	@GET
	@Path("/rewardAdd/{phone}/{number_reward}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object rewardAdd(@Context HttpHeaders httpheaders, @PathParam("phone") String phone,
			 @PathParam("number_reward") String number_reward) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		CommonInfo cmIn = new CommonInfo();
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String status = getIndateByPhone(con, "status", phone);
			if("0".equals(status)) {
				return CommonUtil.makeNGStatus("Customer in waiting list, please confirm or delete customer from Waitting List tab!");
			} else if("1".equals(status)) {
				return CommonUtil.makeNGStatus("Customer in in-service list, please confirm or delete customer from In Service tab!");
			}
			ArrayList<String> colVa = new ArrayList<String>();
			colVa.add("point = point - " + Integer.parseInt(number_reward));
			boolean flag = updateCus(con, colVa, "phone", phone);
			if (flag) {
				cmIn.setStatus("OK");
			}
			return cmIn;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				return cmIn;
			}
		}
	}
	
	@GET
	@Path("/waitingDel/{phone}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object waitingDel(@Context HttpHeaders httpheaders, @PathParam("phone") String phone) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		CommonInfo cmIn = new CommonInfo();
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			DateLogin2 status = getStatusandCompleMoneyPhone(con, phone);
			//String key = ""
			if("2".equals(status.getStatus())) {
				return CommonUtil.makeNGStatus("Customer complete sevice!");
			} else if("1".equals(status.getStatus())) {
				return CommonUtil.makeNGStatus("Customer in in-service list!");
			}
			ArrayList<String> colVa = new ArrayList<String>();
			//if()
			//System.out.println("   " + seting.getCovert() + "   ");
			if(seting.getCovert() == null) {
				getSetting(con);
			}
			int point = CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
					CommonUtil.parseCompleteMoney(status.getCompletedmoney()), seting.getCovertType());
			colVa.add("point = point + " +  point);
			boolean flag = updateCus(con, colVa, "phone", phone);
		//	ArrayList<String> colVa2 = new ArrayList<String>();
		//	colVa2.add("point = '" + point +"'");
		//	updaterewardChoose(con, colVa2, "phone",phone + "_" + getDate());
			String completedempolyee = getIndateByPhone(con, "completedempolyee", phone);
			if(completedempolyee !=null && !completedempolyee.isEmpty()) {
				insertHis(con, phone, completedempolyee , getTime());
			} 
			
			ArrayList<String> colVa3 = new ArrayList<String>();
			colVa3.add("status = '2'");
			updateIndate(con, colVa3, "phone", phone);
			
			//deleteIndate(con, phone);
			
		/*	ArrayList<String> colVa = new ArrayList<String>();
			ArrayList<String> colVaCus = new ArrayList<String>();
			Connection con = null;
			try {
				con = DBUtil.getConnection();
				
				if("1".equals(more)) {
					colVa.add("completedid = completedid || '" + SLIP_CHR + seviceId + "'");
					colVa.add("completedmoney = completedmoney || '" + SLIP_CHR + money + "'" );
					colVa.add("completedempolyee = completedempolyee || '" + SLIP_CHR2 + seviceId + SLIP_CHR+  employee1.getEmpName() + "'" );
					colVa.add("status = '0'");
					colVa.add("cometime = '" + getTime() + "'");
				} else {
					colVa.add("status = '2'");
					colVaCus.add("point = point + " + CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
							Long.parseLong(getIndateByPhone(con, "money", phone)), seting.getCovertType()));
					updateCus(con, colVaCus, "phone", phone);
				    // id$$$employee****id$$$employee
					String completedempolyee = getIndateByPhone(con, "completedempolyee", phone);
					String serviceid = getIndateByPhone(con, "serviceid", phone);
					if(completedempolyee !=null && completedempolyee.isEmpty()) {
						colVa.add("completedempolyee = completedempolyee || '" + SLIP_CHR2 + seviceId + SLIP_CHR+  employee1.getEmpName() + "'" );
						insertHis(con, phone, completedempolyee + SLIP_CHR2 + seviceId + SLIP_CHR+  employee1.getEmpName() , getTime());
					} else {
						colVa.add("completedempolyee = completedempolyee || '" + SLIP_CHR2 + serviceid + SLIP_CHR +  employee1.getEmpName() + "'" );
						insertHis(con, phone,  SLIP_CHR2 + serviceid + SLIP_CHR+  employee1.getEmpName() , getTime());
					}
				}
				updateIndate(con, colVa, "phone", phone);*/
			
			if (flag) {
				cmIn.setStatus("OK");
			}
			return cmIn;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				return cmIn;
			}
		}
	}
	
	@GET
	@Path("/getWorkingInfo/{employee_name}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getWorkingInfo(@Context HttpHeaders httpheaders, @PathParam("employee_name") String employee_name) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			CustomerOut cusOut = getDataByEmployee(con, employee_name);
			if(cusOut == null) {
				return CommonUtil.makeNGStatus("You are not assign for customer!!!!");
			}
			return cusOut;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return  CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return  CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				return  CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
			}
		}
	}
	
	public boolean updateIndate(Connection con, ArrayList<String> colunmVa, String whereColumn, String whereValue) {
		Statement stmt = null;
		ResultSet rs = null;
		String setValue = covertArrToString(colunmVa, " , ", true);
		try {
			stmt = con.createStatement();
			System.out.println("UPDATE indatelogin SET " + setValue + " where " + whereColumn + " = '"
					+ whereValue + "' and status <> '2' ");
			stmt.executeUpdate("UPDATE indatelogin SET " + setValue + " where " + whereColumn + " = '"
					+ whereValue + "' and status <> '2' ");
			return true;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public boolean updaterewardChoose(Connection con, ArrayList<String> colunmVa, String whereColumn, String whereValue) {
		Statement stmt = null;
		String setValue = covertArrToString(colunmVa, " , ", true);
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE rewardChoose SET " + setValue + " where " + whereColumn + " = '"
					+ whereValue + "' and status ='1'");
			return true;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean deleteIndate(Connection con, String phone) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("DELETE indatelogin where phone = '"
					+ phone + "'");
			return true;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}

	//id$$$employee****id$$$employee
	public boolean insertHis(Connection con, String phone, String completeddata, String cometime) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("insert into history (phone, completeddata, cometime) values ('"
					+ phone + "','"
					+ completeddata + "','"
					+ cometime + "')");
			return true;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public boolean insertrewardChoose(Connection con, String phone, String sevice, String name) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("insert into rewardChoose (phone, point, status, service, name) values ('"
					+ phone + "_" + getDate() + "','0','0','"+ sevice + "','"+name + "')");
			return true;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	public boolean updateCus(Connection con, ArrayList<String> colunmVa, String whereColumn, String whereValue) {
		Statement stmt = null;
		ResultSet rs = null;
		String setValue = covertArrToString(colunmVa, " , ", true);
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE customer SET " + setValue + " where " + whereColumn + " = '"
					+ whereValue + "'");
			return true;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	
	private String checkAuth(HttpHeaders httpheaders) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		return null;
	}

	@GET
	@Path("/changeWorking/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String changeWorking(@Context HttpHeaders httpheaders, @PathParam("id") String id) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		Employee employee1 = EmployeeDAO.getEmployee(id);
		employee1.setIsWorking(!employee1.isIsWorking());
		LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		employee1.setLstTime(checkIn);
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@POST
	@Path("/workingEmployee")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object workingEmployee(@Context HttpHeaders httpheaders, CustomerIn2 a) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			Customer cus = getCustomerByPhone(a.getPhone());
			if (cus == null) {
				return CommonUtil.makeNGStatus("Not found customer");
			} else {
				
				return subChangeWorkFree(httpheaders, a.getEmployee(), a.getPass(),a, con );
			}
		} catch (Exception e) {
			// TODO: handle exception
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR); 
		}
	}

	@GET
	@Path("/changeWorkFree/{id}/{pass}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String changeWorkFree(@Context HttpHeaders httpheaders, @PathParam("id") String id,
			@PathParam("pass") String pass) {
		
		return subChangeWorkFree(httpheaders, id, pass, null,null);
	}

	private String subChangeWorkFree(HttpHeaders httpheaders, String id, String pass, CustomerIn2 a,Connection con) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Employee employee1 = EmployeeDAO.getEmployee(id);
		if (!pass.equals(employee1.getPass())) {
			return "{\"error\": \"notCorrPass\"}";
		}
		if(a != null) {
			ArrayList<String> colVa = new ArrayList<String>();
			colVa.add("status = '1'");
			colVa.add("tables = '" + a.getTable() + "'");
			colVa.add("employee = '" + employee1.getEmpName()+ "'");
			colVa.add("cometime = '" + getTime() + "'");
			updateIndate(con, colVa, "phone", a.getPhone());
		}
		
		employee1.setIsWorking(!employee1.isIsWorking());
		LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		employee1.setLstTime(checkIn);
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/getFreeCus")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getFreeCus(@Context HttpHeaders httpheaders) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		try {
			return getIndateByStatus(DBUtil.getConnection(), "0", "appointment desc, cometime asc");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CommonUtil.makeNGStatus(null);
	}

	@GET
	@Path("/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String deleteUser(@Context HttpHeaders httpheaders, @PathParam("id") String id) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		EmployeeDAO.removeEmployee(id);
		employee = EmployeeDAO.getEmployee();
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/dummy/{data}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getData(@Context HttpHeaders httpheaders, @PathParam("data") String id) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		String formattedDate = dtfL.format(checkIn);
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection();

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT vl from dataturn where datet=\'" + id + "\'");
			employeeT.clear();
			if (formattedDate.equals(id) && checkL == 1)
				employee = EmployeeDAO.clearEmployee();
			if (rs.next()) {
				Object obj = new JSONParser().parse(rs.getString(1));
				JSONObject jo = (JSONObject) obj;
				JSONArray ja = (JSONArray) jo.get("detail");
				if (ja == null) {
					String s = "{\"status\":";
					if (checkL == 1)
						s += true;
					else
						s += false;
					return s + "}";
				}
				for (Object o : ja) {
					if (o instanceof JSONObject) {
						if (formattedDate.equals(id) && checkL == 1)
							parseEmployeeObject((JSONObject) o, id, true);
						else
							parseEmployeeObject((JSONObject) o, id, false);
					}
				}
				// ja.forEach(emp -> parseEmployeeObject((JSONObject) emp, id));
			}
		} catch (URISyntaxException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
				// stmt.close();
			} catch (SQLException e) {
			}
		}
		if (formattedDate.equals(id) && checkL == 1)
			return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, true);// buildJson(updatePosition(new
		// ArrayList<Employee>(employee.values())),
		return buildJson(updatePosition(new ArrayList<Employee>(employeeT.values())), 2, true); // 1);

	}

	private void parseEmployeeObject(JSONObject employee1, String id, boolean load) {
		Employee tmpe = new Employee();
		String id2 = employee1.get("id").toString();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy:MM:dd HH:mm:ss");
		tmpe.setCheckInTime(LocalDateTime.parse(id + " " + employee1.get("loginTime").toString(), formatter));
		if (employee1.get("lstTime").equals("Not working")) {
			tmpe.setLstTime(LocalDateTime.parse(id + " " + "00:00:00", formatter));
		} else {
			tmpe.setLstTime(LocalDateTime.parse(id + " " + employee1.get("lstTime").toString(), formatter)); // Adding
																												// new
		}
		if (employee1.get("pass") == null) {
			tmpe.setPass("n_u_l_l");
		} else {
			tmpe.setPass(employee1.get("pass").toString()); // new
		}
		tmpe.setPosition(Integer.parseInt(employee1.get("sortOrder").toString()));
		try {
			tmpe.setTotal(Double.parseDouble(employee1.get("turnAll").toString()));
		} catch (NumberFormatException e) {
			String[] xxx = employee1.get("turnAll").toString().split("/");
			tmpe.setTotal(Double.parseDouble(xxx[0]));
		}
		tmpe.setTotalTurn(Double.parseDouble(employee1.get("turn").toString()));
		tmpe.setEmpName(employee1.get("name").toString());
		tmpe.setIsWorking("1".equals(employee1.get("working").toString()) ? true : false);
		tmpe.setActive("1".equals(employee1.get("status").toString()) ? true : false);
		tmpe.setEmployeeID(id2);
		JSONArray ja1 = (JSONArray) employee1.get("workHis");
		Iterator itr2 = ja1.iterator();
		ArrayList<WorkHis> lstWh = new ArrayList<WorkHis>();
		while (itr2.hasNext()) {
			Iterator<Map.Entry> itr1 = ((Map) itr2.next()).entrySet().iterator();
			WorkHis tn = new WorkHis();
			while (itr1.hasNext()) {
				Map.Entry pair = itr1.next();
				if (pair.getKey().equals("money")) {
					tn.setMoney(Double.parseDouble(pair.getValue().toString()));
				} else if (pair.getKey().equals("name")) {
					tn.setName(pair.getValue().toString());
				} else if (pair.getKey().equals("id")) {
					tn.setId(pair.getValue().toString());
				} else if (pair.getKey().equals("free")) {
					tn.setTurn("1".equals(pair.getValue().toString()) ? true : false);
				} else if (pair.getKey().equals("workTime")) {
					tn.setWorkTime(pair.getValue().toString());
				} else if (pair.getKey().equals("startTime")) {
					tn.setStartTime(pair.getValue().toString());// Adding new
				}
			}
			lstWh.add(tn);
		}
		tmpe.setTurnListD(lstWh);
		if (!load)
			employeeT.put(id2, tmpe);
		else
			employee.put(id2, tmpe);
	}

	public ArrayList<ArrayList<Employee>> updatePosition(ArrayList<Employee> employee) {
// total 10, active 6 , inactive 4
// Get active, inactive number
		ArrayList<ArrayList<Employee>> arrOfArrEmployee = new ArrayList<ArrayList<Employee>>();
		int numberOfEmployee = employee.size();
		int numberActive = 0;
		int numberInActive = 0;
		int numberBusyWorker = 0;
		int numberFreeWorker = 0;
		BubbleSort b = new BubbleSort();
		for (int i = 0; i < numberOfEmployee; i++) {
			if (employee.get(i).isActive()) {
				numberActive++;
				if (employee.get(i).isIsWorking()) {
					numberBusyWorker++;
				}
			} else {
				numberInActive++;
			}
		}
		numberFreeWorker = numberActive - numberBusyWorker;// ??????????????

// Process Free Worker 
		if (numberFreeWorker > 0) {
			ArrayList<Employee> tmpFreeWorker = new ArrayList<Employee>(numberFreeWorker);
			for (int i = 0; i < employee.size(); i++) {
				if (employee.get(i).isActive() && !employee.get(i).isIsWorking()) {
					tmpFreeWorker.add(employee.get(i));
				}
			}

			Collections.sort(tmpFreeWorker, new Comparator<Employee>() {

				public int compare(Employee arg0, Employee arg1) {
					if (arg0.getTotalTurn() > arg1.getTotalTurn())
						return 1;
					else if (arg0.getTotalTurn() == arg1.getTotalTurn())
						if (arg0.getCheckInTime().isBefore(arg1.getCheckInTime()))
							return -1;
						else
							return 1;
					return -1;
				}
			});
			/*
			 * for (int i = 0; i < tmpFreeWorker.size(); i++) {
			 * System.out.println(tmpFreeWorker.get(i).getEmpName() + "--- " +
			 * tmpFreeWorker.get(i).getTotalTurn() + "--- " +
			 * tmpFreeWorker.get(i).getCheckInTime()); }
			 * System.out.println("------------------------------");
			 */
			/*
			 * boolean breakF = true;66 int countLoop = 1; while (true) { breakF = true;
			 * ArrayList<Employee> tmpFreeWorker1 = new ArrayList<Employee>();
			 * if(tmpFreeWorker.isEmpty()) break; tmpFreeWorker1.add(tmpFreeWorker.get(0));
			 * int breakIndex = 0; for (int i = 0; i < tmpFreeWorker.size() - 1; i++) { if
			 * (tmpFreeWorker.get(i+1).getTotalTurn() - STEP_TURN <
			 * tmpFreeWorker.get(i).getTotalTurn()) {
			 * tmpFreeWorker1.add(tmpFreeWorker.get(i+1)); breakIndex++; } else break; }
			 * for(int i =breakIndex; i>= 0; i--) tmpFreeWorker.remove(i);
			 * Collections.sort(tmpFreeWorker1, new Comparator<Employee>() { public int
			 * compare(Employee arg0, Employee arg1) { // TODO Auto-generated method stub if
			 * (arg0.getCheckInTime().isBefore(arg1.getCheckInTime())) return 1; else return
			 * -1; } }); arrOfArrEmployee.add(tmpFreeWorker1); for (int i = 0; i <
			 * tmpFreeWorker.size(); i++) { System.out.println(countLoop + "::::::" +
			 * tmpFreeWorker.get(i).getEmpName() + "--- " +
			 * tmpFreeWorker.get(i).getTotalTurn() + "--- " +
			 * tmpFreeWorker.get(i).getCheckInTime()); }
			 * System.out.println("------------------------------"); countLoop++; if
			 * (breakF) { break; } } arrOfArrEmployee.add(tmpFreeWorker);
			 */
			/*
			 * boolean breakF = true; int countLoop = 1; while (true) { breakF = true; for
			 * (int i = 0; i < tmpFreeWorker.size() - 1; i++) { if
			 * (tmpFreeWorker.get(i+1).getTotalTurn() - STEP_TURN <
			 * tmpFreeWorker.get(i).getTotalTurn()) { if
			 * (tmpFreeWorker.get(i).getCheckInTime() .isAfter(tmpFreeWorker.get(i +
			 * 1).getCheckInTime())) { breakF = false; Collections.swap(tmpFreeWorker, i, i
			 * + 1); break; } } } for (int i = 0; i < tmpFreeWorker.size(); i++) {
			 * System.out.println(countLoop + "::::::" + tmpFreeWorker.get(i).getEmpName() +
			 * "--- " + tmpFreeWorker.get(i).getTotalTurn() + "--- " +
			 * tmpFreeWorker.get(i).getCheckInTime()); }
			 * System.out.println("------------------------------"); countLoop++; if
			 * (breakF) { break; } } arrOfArrEmployee.add(tmpFreeWorker);
			 */

			int j = 1;
			boolean breakF = true;
			if (seting.getTurn() == null)
				getSetting();
			int step_turn = Integer.parseInt(seting.getTurn());
			while (j <= (tmpFreeWorker.size() - 1)) {
				// int k = j;
				for (int i = j; i > 0; i--) {
					if (tmpFreeWorker.get(j).getTotalTurn() - step_turn < tmpFreeWorker.get(i - 1).getTotalTurn()) {
						if (tmpFreeWorker.get(j).getCheckInTime().isBefore(tmpFreeWorker.get(i - 1).getCheckInTime())) {
							Collections.swap(tmpFreeWorker, i - 1, j);
							j = i - 1;
							breakF = false;
							break;
						}
					}
				}
				if (breakF) {
					j++;
				} else {
					breakF = true;
				}

				/*
				 * for (int i = 0; i < tmpFreeWorker.size(); i++) { System.out.println(countLoop
				 * + "::::::" + tmpFreeWorker.get(i).getEmpName() + "--- " +
				 * tmpFreeWorker.get(i).getTotalTurn() + "--- " +
				 * tmpFreeWorker.get(i).getCheckInTime()); }
				 * System.out.println("------------------------------");
				 */
			}
			arrOfArrEmployee.add(tmpFreeWorker);

			/*
			 * //index group by Step_Turn int tmpIndexGroup = 1; if (tmpFreeWorker.size() >
			 * 0) { tmpFreeWorker.get(0).setIndexGroup(tmpIndexGroup); //
			 * System.out.println("Employee: " + tmpFreeWorker.get(0).getEmpName() + " //
			 * total_Turn: " + tmpFreeWorker.get(0).getTotalTurn()); if
			 * (tmpFreeWorker.size() > 1) { for (int i = 1; i < tmpFreeWorker.size(); i++) {
			 * if ((tmpFreeWorker.get(i).getTotalTurn() - tmpFreeWorker.get(i -
			 * 1).getTotalTurn()) >= STEP_TURN) { tmpIndexGroup++;
			 * tmpFreeWorker.get(i).setIndexGroup(tmpIndexGroup); } else {
			 * tmpFreeWorker.get(i).setIndexGroup(tmpIndexGroup); } //
			 * System.out.println("Employee: " + tmpFreeWorker.get(i).getEmpName() + " //
			 * total_Turn: " + tmpFreeWorker.get(i).getTotalTurn()); } } } //
			 * System.out.println("\nAFTER SORT:"); // printAddr(tmpFreeWorker);
			 * 
			 * 
			 * =============================================================================
			 * ========
			 * 
			 * // arrOfArrEmployee = new ArrayList<ArrayList<Employee>>(tmpIndexGroup);
			 * arrOfArrEmployee.clear(); if (tmpFreeWorker.size() > 0) { ArrayList<Employee>
			 * tmp = new ArrayList<Employee>(); tmp.add(tmpFreeWorker.get(0)); for (int i =
			 * 1; i < tmpFreeWorker.size(); i++) { if (tmpFreeWorker.get(i).getIndexGroup()
			 * != tmpFreeWorker.get(i - 1).getIndexGroup()) { arrOfArrEmployee.add(tmp);
			 * System.out.println( "Added group: " + arrOfArrEmployee.size() + " && with " +
			 * tmp.size() + " elements"); tmp = new ArrayList<Employee>();
			 * tmp.add(tmpFreeWorker.get(i)); } else { tmp.add(tmpFreeWorker.get(i)); } }
			 * arrOfArrEmployee.add(tmp); System.out.println( "Added last group: " +
			 * arrOfArrEmployee.size() + " && with " + tmp.size() + " elements"); }
			 */
// Create tmp array of busy worker and sort by total , index position
// Process Busy worker array

//Create tmp array of inactive and sort inactive & index  position 
//Process Inactive worker array

		}
		if (numberBusyWorker > 0) {
			ArrayList<Employee> tmpBusyWorker = new ArrayList<Employee>(numberBusyWorker);
			for (int i = 0; i < employee.size(); i++) {
				if (employee.get(i).isActive() && employee.get(i).isIsWorking()) {
					tmpBusyWorker.add(employee.get(i));
				}
			}
			b.bubbleSortTotal(tmpBusyWorker);
			// set Position
			for (int i = 0; i < tmpBusyWorker.size(); i++) {
				tmpBusyWorker.get(i).setPosition(numberActive - numberBusyWorker + i + 1);
			}
			arrOfArrEmployee.add(tmpBusyWorker);
		}

		if (numberInActive > 0) {
			ArrayList<Employee> tmpInActive = new ArrayList<Employee>(numberInActive);
			for (int i = 0; i < employee.size(); i++) {
				if (employee.get(i).isActive() == false) {
					tmpInActive.add(employee.get(i));
				}
			}
			b.bubbleSortTime(tmpInActive);
			for (int i = 0; i < tmpInActive.size(); i++) {
				tmpInActive.get(i).setPosition(i + 1 + numberActive);
			}
			arrOfArrEmployee.add(tmpInActive);
		}
		return arrOfArrEmployee;
		// print(arrOfArrEmployee);
	}

	private String buildJson(ArrayList<ArrayList<Employee>> employee, int checkL, boolean search) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		if (seting.getSecurity() == null || seting.getSecurity().isEmpty()) {
			getSetting();
		}
		// System.out.println("\nEmployee Table Details:");
		String s = "{\"status\":";
		if (checkL == 1)
			s += true;
		else
			s += false;
		if (seting.getSecurity().equals("1"))
			s += ",\"role\": \"1\"";
		else
			s += ",\"role\": \"0\"";
		s += ",\"detail\":[";
		// tb.addRow("EmployeeID", "EmployeeName", "CheckInTime", "Total", "Total_Turn",
		// "Is_Working", "Status", "Position", "Turn_List", "Index_Group");
		int k = 0;
		int l = 0;
		for (int j = 0; j < employee.size(); j++) {
			for (int i = 0; i < employee.get(j).size(); i++) {
				int index = i;
				if (l > 0)
					s += ",";
				s += "{";
				l++;
				s += "\"id\" : \"" + employee.get(j).get(index).getEmployeeID() + "\",";
				s += "\"name\" : \"" + employee.get(j).get(index).getEmpName() + "\",";
				if (employee.get(j).get(index).getPass() == null)
					s += "\"pass\" : \"n_u_l_l\",";
				else
					s += "\"pass\" : \"" + employee.get(j).get(index).getPass() + "\",";
				s += "\"sortOrder\" : \"" + employee.get(j).get(index).getPosition() + "\",";
				s += "\"turn\" : \"" + employee.get(j).get(index).getTotalTurn() + "\",";
				s += "\"status\" : \"" + ((employee.get(j).get(index).isActive()) ? "1" : "0") + "\",";
				s += "\"working\" : \"" + ((employee.get(j).get(index).isIsWorking()) ? "1" : "0") + "\",";
				s += "\"loginTime\" : \"" + dtf.format(employee.get(j).get(index).getCheckInTime()) + "\",";
				if (employee.get(j).get(index).getLstTime() == null)
					s += "\"lstTime\" : \"" + "Not working\",";
				else
					s += "\"lstTime\" : \"" + dtf.format(employee.get(j).get(index).getLstTime()) + "\",";
				s += "\"workHis\" : [";
				k = 0;
				int lateMoney = 0;
				for (WorkHis work : employee.get(j).get(index).getTurnListD()) {
					if (k == 0) {
						s += "{";
						k++;
					} else
						s += ",{";
					if ("Comming late".equals(work.getName())) {
						lateMoney = (int) work.getMoney();
					}
					s += "\"id\" : \"" + work.getId() + "\",";
					s += "\"name\" : \"" + work.getName() + "\",";
					s += "\"free\" : \"" + ((work.isTurn()) ? "1" : "0") + "\",";
					s += "\"money\" : \"" + work.getMoney() + "\",";
					s += "\"workTime\" : \"" + (work.getWorkTime() == null ? "-" : work.getWorkTime()) + "\",";
					s += "\"startTime\" : \"" + (work.getStartTime() == null ? "-" : work.getStartTime()) + "\"";
					s += "}";
				}
				s += "],";
				if (lateMoney > 0)
					s += "\"turnAll\" : \"" + employee.get(j).get(index).getTotal() + "/" + lateMoney + "\"";
				else
					s += "\"turnAll\" : \"" + employee.get(j).get(index).getTotal() + "\"";
				s += "}";
			}
		}
		s += "]";
		s += "}";

		if (!search) {
			Connection con = null;
			Statement stmt = null;
			try {
				LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
				String formattedDate = dtfL.format(checkIn);
				con = DBUtil.getConnection();
				stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO dataturn (datet, vl) VALUES('" + formattedDate + "','" + s
						+ "') ON CONFLICT (datet) DO UPDATE SET vl = '" + s + "'");
				// stmt.executeUpdate("update dataturn set vl = \'" + s + "\' where datet = \'"
				// + formattedDate + "\'");
			} catch (URISyntaxException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) { // TODO Auto-generated catch
				e.printStackTrace();
			} finally {
				try {
					if (con != null)
						con.close();
					// stmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return s;
	}

	public String getTime() {
		LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		DateTimeFormatter dtfL = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		return dtfL.format(checkIn).toString();
	}
	
	public String getDate() {
		LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		DateTimeFormatter dtfL = DateTimeFormatter.ofPattern("yyyy:MM:dd");
		return dtfL.format(checkIn).toString();
	}
	
	public String getDiffTime(String preTime) {
		LocalDateTime curTime = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		DateTimeFormatter dtfL = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		LocalDateTime lcPreTime = LocalDateTime.parse(preTime, dtfL);
		String rst = "";
		if(curTime.getHour() > lcPreTime.getHour()) {
			rst += (curTime.getHour() - lcPreTime.getHour()) + "h";
		}
		rst += (curTime.getMinute() - lcPreTime.getMinute()) + "min";
		return rst;
	}
	public Integer getDiffTime2(String preTime) {
		LocalDateTime curTime = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
		DateTimeFormatter dtfL = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		LocalDateTime lcPreTime = LocalDateTime.parse(preTime, dtfL);
		return (curTime.getHour() - lcPreTime.getHour())*60 +  (curTime.getMinute() - lcPreTime.getMinute());
	}
	public Customer getCustomerByPhone(String phone) {
		Customer cus = null;
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			cus = getCustomerByPhone(con, phone);
		} catch (URISyntaxException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				con.close();
				// stmt.close();
			} catch (SQLException e) {
			}
		}
		return cus;
	}

	public ArrayList<TableConvert> getTableCovert(Connection con) {
		ArrayList<TableConvert> tCv = new ArrayList<TableConvert>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT froms,  tos, infor from reward");
			TableConvert tmp;
			while (rs.next()) {
				tmp = new TableConvert();
				tmp.setPoint(rs.getString("froms"));
				tmp.setReward(rs.getString("tos"));
				tmp.setInfor(rs.getString("infor"));
				tCv.add(tmp);
			}
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tCv;
	}

	public Customer getCustomerByPhone(Connection con, String phone) {
		Customer cus = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT phone, name, email, point from customer where phone = '" + phone + "'");
			if (rs.next()) {
				cus = new Customer();
				cus.setPhone(rs.getString("phone"));
				cus.setName(rs.getString("name"));
				cus.setEmail(rs.getString("email"));
				cus.setPoint(rs.getString("point"));
			}
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cus;
	}

	public String getIndateByPhone(Connection con, String column, String phone) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT " + column+ " from indatelogin where phone = '" + phone + "'  and status <> '2' order by id desc limit 1");
			if (rs.next()) {
				return rs.getString(column);
			}
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public DateLogin2 getStatusandCompleMoneyPhone(Connection con, String phone) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT status, completedmoney, employee from indatelogin where phone = '" + phone + "' and status <> '2' order by id desc limit 1");
			DateLogin2 rst = new DateLogin2();
			if (rs.next()) {
				rst.setCompletedmoney(rs.getString("completedmoney"));
				rst.setStatus(rs.getString("status"));
				rst.setEmployee(rs.getString("employee"));
				return rst;
			}
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	public CustomerOut getDataByEmployee(Connection con, String employee) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			CustomerOut out = new CustomerOut();
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT service , phone, money, countsevice,servicefull from indatelogin where employee = '" + employee + "' and status = '1' order by id desc limit 1");
			if (rs.next()) {
				out.setService(rs.getString("service"));
				out.setServiceList(getSeviceListOfCus(rs.getString("servicefull")));
				out.setPhone(rs.getString("phone"));
				out.setMoney(rs.getString("money"));
				if(Integer.parseInt(rs.getString("countsevice")) > 1)
					out.setMore("YES");
				else
					out.setMore("NO");
						
			}
			return out;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private ArrayList<SeveiceItem> getSeviceListOfCus(String sevList) {
		// TODO Auto-generated method stub
		String[] items = sevList.split(SLIP_CHR2_RV);
		ArrayList<SeveiceItem> rst = new ArrayList<SeveiceItem>();
		for (String item : items) {
			String[] data = item.split(SLIP_CHR_RV);
			SeveiceItem tmp = new SeveiceItem();
			tmp.setId(data[1]);
			tmp.setName(data[0]);
			tmp.setMoney(data[2]);
			rst.add(tmp);
		}
		
		return rst;
	}

	public ArrayList<CustomerIn> getIndateByStatus(Connection con, String status, String orderBy) {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<CustomerIn> rst = new ArrayList<CustomerIn>();
		try {
			stmt = con.createStatement();
		//	System.out.println("SELECT * from indatelogin where status = \'" + status + "\' order by " + orderBy);
			rs = stmt.executeQuery("SELECT * from indatelogin where status = '" + status + "' order by " + orderBy);
			while (rs.next()) {
				CustomerIn tmp = new CustomerIn();
				tmp.setName(rs.getString("name"));
				tmp.setPhone(rs.getString("phone"));
				tmp.setService(rs.getString("service"));
				tmp.setTime(getDiffTime(rs.getString("cometime"))); // time login => need tinh
				tmp.setTable(rs.getString("tables"));
				rst.add(tmp);
				// tmp.setPoint(rs.getString("name"));
			}
			return rst;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public HashMap<Integer, ArrayList<CustomerIn>> getIndate(Connection con, String orderBy) {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<Integer, ArrayList<CustomerIn>> rsHm = new HashMap<Integer, ArrayList<CustomerIn>>();
		ArrayList<CustomerIn> rstRew = new ArrayList<CustomerIn>();
		ArrayList<CustomerIn> rstIn = new ArrayList<CustomerIn>();
		ArrayList<CustomerIn> rstWaiting = new ArrayList<CustomerIn>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * from indatelogin order by " + orderBy);
			String status = "";
			while (rs.next()) {
				CustomerIn tmp = new CustomerIn();
				tmp.setName(rs.getString("name"));
				tmp.setPhone(rs.getString("phone"));
				tmp.setService(rs.getString("service"));
				status = rs.getString("status");
				if("0".equals(status)) {
					tmp.setTime(getDiffTime(rs.getString("cometime")));
					rstWaiting.add(tmp);
				} else if("1".equals(status)) {
					tmp.setTable(rs.getString("tables"));
					rstIn.add(tmp);
				} else {
					if("1".equals(rs.getString("reward"))) {
						if("2".equals(status)) {
							if(seting.getCovert() == null) {
								getSetting(con);
							}
							int point = CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
									CommonUtil.parseCompleteMoney(rs.getString("completedmoney")), seting.getCovertType());
							tmp.setPoint(Integer.toString(point));
						}/* else {
							int point = CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
									Long.parseLong(rs.getString("money")), seting.getCovertType());
							tmp.setPoint(Integer.toString(point));
						}*/
						rstRew.add(tmp);
					}
				}
			}
			rsHm.put(0, rstWaiting);
			rsHm.put(1, rstIn);
			rsHm.put(2, rstRew);
			return rsHm;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ServiceListTV getIndate2(Connection con, String orderBy) {
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Statement stmt2 = null;
		ServiceListTV svTV  = new ServiceListTV();
		ArrayList<CustomerOSer> rstIn = new ArrayList<CustomerOSer>();
		ArrayList<CustomerOWait> rstWaiting = new ArrayList<CustomerOWait>();
		try {
			stmt = con.createStatement();
			if(seting.getWarning() == null) {
				getSetting(con);
			}
			rs = stmt.executeQuery("SELECT * from indatelogin where status in ('0', '1') order by appointment desc, id asc ");
			String status = "";
			while (rs.next()) {
				status = rs.getString("status");
				if("0".equals(status)) {
					CustomerOWait waitTmp = new CustomerOWait();
					String appointment = rs.getString("appointment");
					String cometime = rs.getString("cometime");
					waitTmp.setApp("1".equals(appointment)?"YES":"NO");
					waitTmp.setName(rs.getString("name"));
					waitTmp.setWaitTime(getDiffTime(cometime));
					if("1".equals(appointment))
						waitTmp.setWarning("YES");
					else {
						if(getDiffTime2(cometime) >= Integer.parseInt(seting.getWarning())) {
							waitTmp.setWarning("YES");
						} else {
							waitTmp.setWarning("NO");
						}
					}
					waitTmp.setService(rs.getString("serviceca").split(SLIP_CHR_RV));
					//rs2 = stmt2.executeQuery("SELECT * from sercatalogue where status in ('0', '1') order by appointment desc, id asc ");
					rstWaiting.add(waitTmp);
				} else if("1".equals(status)) {
					CustomerOSer serTmp = new CustomerOSer();
					serTmp.setEmployee(rs.getString("employee"));
					serTmp.setName(rs.getString("name"));
					serTmp.setTable(rs.getString("tables"));
					serTmp.setTime(getDiffTime(rs.getString("cometime")));
					rstIn.add(serTmp);
				}
			}
			svTV.setSalonName(seting.getSalonName());
			svTV.setRefresh(seting.getRefresh());
			svTV.setService(rstIn);
			svTV.setWaiting(rstWaiting);
			return svTV;
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ServiceTmp getServiceByID(Connection con, String[] ids) {
		ServiceTmp lstResult = new ServiceTmp();
		Statement stmt = null;
		ResultSet rs = null;
		Statement stmt1 = null;
		ResultSet rs1 = null;
		try {
			stmt = con.createStatement();
			// System.out.println("SELECT name from service where id in (" +
			// covertArrToString(new ArrayList<String>(Arrays.asList(ids))) + ")");
			rs = stmt.executeQuery("SELECT  id, name, money, time, cata_id from service where id in  ("
					+ covertArrToString(new ArrayList<String>(Arrays.asList(ids)), ",", true) + ")");

			ArrayList<String> tmp = new ArrayList<String>();
			ArrayList<String> tmpName = new ArrayList<String>();
			Long money = 0L;
			String tmpMoney = "";
			String name = "";
			String cat_id = "";
			String cat_name = "";
			while (rs.next()) {
				// sTmp = new ServiceTmp();
				tmpMoney = rs.getString("money");
				tmpMoney = tmpMoney.substring(0, tmpMoney.length());
				name = rs.getString("name");
				tmpName.add(name);
				tmp.add(name + SLIP_CHR + rs.getString("id") + SLIP_CHR + tmpMoney);
				money += Long.parseLong(tmpMoney);
				cat_id += rs.getString("cata_id") + ",";
			}
			cat_id = cat_id.substring(0, cat_id.length() - 1);
			stmt1 = con.createStatement();
			rs1 = stmt1.executeQuery("SELECT DISTINCT name from sercatalogue where id in  ("
					+ cat_id + ")");
			while (rs1.next()) {
				// sTmp = new ServiceTmp();
				cat_name += rs.getString("name") + SLIP_CHR;
			}
			lstResult.setName(tmpName);
			lstResult.setMoney(money);
			lstResult.setSevice(tmp);
			lstResult.setCa_name(cat_name.substring(0, cat_name.length() - SLIP_CHR.length()));
		} catch (SQLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lstResult;
	}

	@POST
	@Path("/loginCus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object addCustomer(@Context HttpHeaders httpheaders, Customer a) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		System.out.println(a.getName());
		CommonInfo cmIn = new CommonInfo();
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			Customer cus = getCustomerByPhone(con, a.getPhone());
			stmt = con.createStatement();
			if (cus == null) {
				sql = "INSERT INTO customer (phone, name, email, lastCome, point) VALUES ('" + a.getPhone() + "','"
						+ a.getName() + "','" + a.getEmail() + "','" + getTime() + "','" + 0 + "')";
				cmIn.setStatus("OK");
			} else {
				sql = "update customer set lastCome = '" + getTime() + "' where phone = '" + cus.getPhone() + "'";
				cmIn.setStatus("OK");
				cmIn.setMsg("Your currently reward point : " + cus.getPoint());
			}
			stmt.executeUpdate(sql);
			return cmIn;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/choonseSevice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object choonseSevice(@Context HttpHeaders httpheaders, DateLoginIn a) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		// System.out.println(a.getName());
		ChooseSerOut cmIn = new ChooseSerOut();
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			Customer cus = getCustomerByPhone(con, a.getPhone());
			if (cus == null) {

			}
			if (seting.getCovert() == null || seting.getCovert() == "") {
				getSetting(con);
			}
			ServiceTmp serviceName = getServiceByID(con, a.getService());
			stmt = con.createStatement();
			ArrayList<TableConvert> tbCv = getTableCovert(con);
			String status = getIndateByPhone(con, "status", a.getPhone());
			if ("0".equals(status)) {
				return CommonUtil.makeNGStatus("You are in waiting list!");
			} else if ("1".equals(status)) {
				return CommonUtil.makeNGStatus("You are in in severice!");
			}
			sql = "INSERT INTO indatelogin (phone, name , status , service , appointment, reward, money, countsevice, serviceid, cometime,"
					+ " servicefull, completedmoney, completedid, completedempolyee,  serviceca) VALUES ('"
					+ a.getPhone() + "','" + cus.getName() + "','0','"
					+ covertArrToString(serviceName.getName(), SLIP_CHR, true) + "','" + a.getAppointment() + "','" + 0
					+ "','" + serviceName.getMoney() + "','" + serviceName.getName().size() + "','"
					+ covertArrToString(new ArrayList<String>(Arrays.asList(a.getService())), ",", true) + "','"
					+ getTime() + "'" + ",'" +covertArrToString(serviceName.getSevice(), SLIP_CHR2, true) + "','0','','','" + serviceName.getCa_name() +"')";
			stmt.executeUpdate(sql);
			//insertrewardChoose(con, a.getPhone(), covertArrToString(serviceName.getName(), SLIP_CHR, true),cus.getName() );
			// Insert History
			String msg = CommonUtil
					.getMsg(Integer.toUnsignedLong(CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
							serviceName.getMoney(), seting.getCovertType())) + Long.parseLong(cus.getPoint()), tbCv);
			String[] dataMsg = msg.split("_");
			cmIn.setMsg(dataMsg[0]);
			if (dataMsg.length > 1)
				cmIn.setAlert(dataMsg[1]);
			cmIn.setTbcover(tbCv);

			return (ChooseSerOut) cmIn;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/getCus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object getCustomer(@Context HttpHeaders httpheaders, Customer a) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Customer cus = getCustomerByPhone(a.getPhone());
		if (cus == null) {
			return a;
		} else {
			return cus;
		}

	}

	@POST
	@Path("/addCatalogue")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object addCatalogue(@Context HttpHeaders httpheaders, CatalogueIn ca) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			sql = "INSERT INTO sercatalogue ( name) VALUES ('" + ca.getName() + "')";
			stmt.executeUpdate(sql);
			return CommonUtil.makeOKStatus("");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/editCatalogue")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object editCatalogue(@Context HttpHeaders httpheaders, CatalogueIn ca) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			sql = "UPDATE sercatalogue SET name = '" + ca.getName() + "' where id = " + ca.getId();
			stmt.executeUpdate(sql);
			return CommonUtil.makeOKStatus("");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/deleteCatalogue")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object deleteCatalogue(@Context HttpHeaders httpheaders, CatalogueIn ca) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			sql = "delete from sercatalogue where id = " + ca.getId();
			stmt.executeUpdate(sql);
			return CommonUtil.makeOKStatus("");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/addService")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object addService(@Context HttpHeaders httpheaders, ServiceIn ca) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			sql = "INSERT INTO service ( cata_id, name ,s_name , money , time ) VALUES (" + "'" + ca.getSer_id() + "',"
					+ "'" + ca.getName() + "'," + "'" + ca.getS_name() + "'," + "'" + ca.getMoney() + "'," + "'"
					+ ca.getTime() + "'" + ")";
			// System.out.println(sql);
			stmt.executeUpdate(sql);
			return CommonUtil.makeOKStatus("");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return CommonUtil.makeNGStatus("Please add catalogue for it!!!");
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/editService")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object editService(@Context HttpHeaders httpheaders, ServiceIn ca) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement(); // cata_id, name ,s_name , money , time
			sql = "Update  service set cata_id = '" + ca.getSer_id() + "'," + " name = '" + ca.getName() + "',"
					+ " s_name = '" + ca.getS_name() + "'," + " money = '" + ca.getMoney() + "'," + " time = '"
					+ ca.getTime() + "'" + " where id = " + ca.getId();
			// System.out.println(sql);
			stmt.executeUpdate(sql);
			return CommonUtil.makeOKStatus("");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return CommonUtil.makeNGStatus("Please add catalogue for it!!!");
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@POST
	@Path("/deleteService")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object deleteService(@Context HttpHeaders httpheaders, ServiceIn ca) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement(); // cata_id, name ,s_name , money , time
			sql = "DELETE  FROM service  where id = " + ca.getId();
			// System.out.println(sql);
			stmt.executeUpdate(sql);
			return CommonUtil.makeOKStatus("");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return CommonUtil.makeNGStatus("Please add catalogue for it!!!");
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	@GET
	@Path("/getListService")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getListService(@Context HttpHeaders httpheaders) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		return getListSv();
	}
	@GET
	@Path("/getSeviceList")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getSeviceList(@Context HttpHeaders httpheaders) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			ServiceList rst = new ServiceList();
			rst.setTbcover(getTableCovert(con));
			HashMap<Integer, ArrayList<CustomerIn>> tmp = getIndate(con, "id");
			rst.setWaiting(tmp.get(0));
			rst.setInservice(tmp.get(1));
			rst.setReward(tmp.get(2));
			return rst;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return null;
	}
	
	@GET
	@Path("/getWaitingList")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getWaitingList(@Context HttpHeaders httpheaders) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			return getIndate2(con, "id");
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return null;
	}
	
	@GET
	@Path("/changeStatus/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String changeStatus(@Context HttpHeaders httpheaders, @PathParam("id") String id) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		Employee employee1 = EmployeeDAO.getEmployee(id);
		employee1.setActive(!employee1.isActive());
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/seviceDel/{phone}/{waiting}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Object seviceDel(@Context HttpHeaders httpheaders, @PathParam("phone") String phone,
			@PathParam("waiting") String waiting) {
		if(loginCheck) {
			 String aunt = checkAuth(httpheaders); 
			 if(aunt != null) return aunt;
		}
		CommonInfo cmIn = new CommonInfo();
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			DateLogin2 status = getStatusandCompleMoneyPhone(con, phone);
			//String key = ""
			if("2".equals(status.getStatus())) {
				return CommonUtil.makeNGStatus("Customer complete sevice!");
			} else if("2".equals(status.getStatus())) {
				return CommonUtil.makeNGStatus("Customer in in-waiting list!");
			}
			//Re-asign
			if("1".equals(waiting)) {
				ArrayList<String> colVa2 = new ArrayList<String>();
				colVa2.add("status = '0'");
				colVa2.add("cometime = '" +  getTime()+"'");
				updateIndate(con, colVa2, "phone",phone);
			} else {
				ArrayList<String> colVa = new ArrayList<String>();
				if(seting.getCovert() == null) {
					getSetting(con);
				}
				int point = CommonUtil.covertMoneyToPoint(Long.parseLong(seting.getCovert()),
						CommonUtil.parseCompleteMoney(status.getCompletedmoney()), seting.getCovertType());
				colVa.add("point = point + " +  point);
				updateCus(con, colVa, "phone", phone);
				
				
				String completedempolyee = getIndateByPhone(con, "completedempolyee", phone);
				if(completedempolyee !=null && !completedempolyee.isEmpty()) {
					insertHis(con, phone, completedempolyee , getTime());
				} 
				
				ArrayList<String> colVa3 = new ArrayList<String>();
				colVa3.add("status = '2'");
				updateIndate(con, colVa3, "phone", phone);
				//updaterewardChoose(con, colVa2, "phone",phone + "_" + getDate());
				//deleteIndate(con, phone);
			}
			
			Employee employee1 = EmployeeDAO.getEmployeeByName(status.getEmployee());
			employee1.setIsWorking(!employee1.isIsWorking());
			LocalDateTime checkIn = Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime();
			employee1.setLstTime(checkIn);
			employee = EmployeeDAO.addEmployee(employee1.getEmployeeID(), employee1);
			return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return cmIn;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				return cmIn;
			}
		}
	}
	private Object getListSv() {
		Connection con = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			stmt2 = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select id, name from sercatalogue order by name ");
			ResultSet rsSub;
			ArrayList<CatalogueOut> lst = new ArrayList<CatalogueOut>();
			while (rs.next()) {
				CatalogueOut tmpC = new CatalogueOut(rs.getLong("id"), rs.getString("name"));
				rsSub = stmt2.executeQuery("Select id, name ,s_name, money, time from service where cata_id = "
						+ rs.getInt("id") + " order by name");
				ArrayList<ServiceOut> arrSev = new ArrayList<ServiceOut>();
				while (rsSub.next()) {
					arrSev.add(new ServiceOut(rsSub.getLong("id"), rsSub.getString("name"), rsSub.getString("s_name"),
							rsSub.getString("money"), rsSub.getString("time"), ""));
				}
				tmpC.setService(arrSev);
				lst.add(tmpC);
			}
			return lst;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.makeNGStatus(CommonUtil.SYS_ERR);
		} finally {
			try {
				if (con != null)
					con.close();
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private String covertArrToString(ArrayList<String> in, String adding, boolean rmLast) {
		StringBuffer sb = new StringBuffer();
		for (Object s : in) {
			sb.append(s.toString());
			sb.append(adding);
		}
		if (rmLast)
			return sb.length() == 0 ? toString() : sb.substring(0, sb.length() - adding.length());
		else
			return sb.toString();
	}
}
