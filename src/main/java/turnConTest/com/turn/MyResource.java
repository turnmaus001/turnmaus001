package turnConTest.com.turn;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.internal.JsonWithPaddingInterceptor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	public static final int STEP_TURN = 20;
	public static String username;
	public static String password;

	// String strDateFormat = "yy:MM:dd";
	DateTimeFormatter dtfL = DateTimeFormatter.ofPattern("yy:MM:dd");

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
		if ("admin".equals(username) && "2333".equals(password)) {
			return 1;
		} else if ("viewer".equals(username) && "123".equals(password)) {
			return 2;
		}
		return 3;
	}

	// http://localhost:8080/com.turn/api/addUser/abff
	@GET
	@Path("/addUser/{userName}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String addEmployee(@Context HttpHeaders httpheaders, @PathParam("userName") String userName) {
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 1) {
			if (EmployeeDAO.isSameName(userName)) {
				return "{\"error\": \"sameName\"}";
			}
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
			employee = EmployeeDAO.addEmployee(userName);
			return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), checkL, false);
		} else if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		}
		return "{\"error\": \"notLogin\"}";
	}

	// http://localhost:8080/com.turn/api/addGroup/{name}/{money}/{free}
	@GET
	@Path("/addGroup/{id}/{name}/{money}/{free}/{tipCash}/{tipCredit}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String addGroup(@Context HttpHeaders httpheaders, @PathParam("id") String id, @PathParam("name") String name,
			@PathParam("money") double money, @PathParam("free") String free, @PathParam("tipCash") double tipCash,
			@PathParam("tipCredit") double tipCredit) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		String token = httpheaders.getHeaderString("Authorization");
		int checkL = checkLogin(token);
		if (checkL == 2) {
			return "{\"error\": \"notAllow\"}";
		} else if (checkL == 3) {
			return "{\"error\": \"notLogin\"}";
		}
		Employee employee1 = EmployeeDAO.getEmployee(id);
		// check null
		int index = 1;
		for (int i = 0; i < employee1.getTurnListD().size(); i++) {
			int tmp = Integer.parseInt(employee1.getTurnListD().get(i).getId());
			if (tmp > index) {
				index = tmp;
			}
		}
		index++;
		employee1.getTurnListD().add(new WorkHis(name, money, "1".equals(free) ? true : false, Integer.toString(index),
				dtf.format(Instant.now().atZone(ZoneId.of("US/Central")).toLocalDateTime()), tipCash, tipCredit));
		if ("0".equals(free)) {
			employee1.setTotalTurn(employee1.getTotalTurn() + money);
		}
		employee1.setTotal(employee1.getTotal() + money);
		employee1.setCashTotal(employee1.getCashTotal() + tipCash);
		employee1.setCreditTotal(employee1.getCreditTotal() + tipCredit);
		employee1.setIsWorking(false);
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
	}

	@GET
	@Path("/upGroup/{id}/{groupid}/{name}/{money}/{free}/{tipCash}/{tipCredit}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateGroup(@Context HttpHeaders httpheaders, @PathParam("id") String id,
			@PathParam("groupid") String groudid, @PathParam("name") String name, @PathParam("money") double money,
			@PathParam("free") String free, @PathParam("tipCash") double tipCash,
			@PathParam("tipCredit") double tipCredit) {
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
		if ("0".equals(free) && !wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() + money - wk.getMoney());
		} else if ("0".equals(free) && wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() + money);
		} else if ("1".equals(free) && !wk.isTurn()) {
			employee1.setTotalTurn(employee1.getTotalTurn() - wk.getMoney());
		}
		employee1.setTotal(employee1.getTotal() + money - wk.getMoney());
		employee1.setCashTotal(employee1.getCashTotal() + tipCash - wk.getTipCash());
		employee1.setCreditTotal(employee1.getCreditTotal() + tipCredit - wk.getTipCredit());
		wk.setId(groudid);
		wk.setMoney(money);
		wk.setName(name);
		wk.setMoney(money);
		wk.setTurn(free.equals("1") ? true : false);
		wk.setTipCash(tipCash);
		wk.setTipCredit(tipCredit);
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
		employee1.setCashTotal(employee1.getCashTotal() - wk.getTipCash());
		employee1.setCreditTotal(employee1.getCreditTotal() - wk.getTipCredit());
		employee1.getTurnListD().remove(index);
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
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
		employee = EmployeeDAO.addEmployee(id, employee1);
		return buildJson(updatePosition(new ArrayList<Employee>(employee.values())), 1, false);
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

	/*
	 * @SuppressWarnings("unchecked") public static void main(String[] args) throws
	 * ParseException { String s =
	 * " {\"status\":true,\"detail\":[{\"id\" : \"2\",\"name\" : \"sdd\",\"sortOrder\" : \"2\",\"turn\" : \"0.0\",\"turnAll\" : \"0.0\",\"status\" : \"1\",\"working\" : \"0\",\"loginTime\" : \"10:54:24\",\"workHis\" : []},{\"id\" : \"1\",\"name\" : \"s\",\"sortOrder\" : \"2\",\"turn\" : \"5.0\",\"turnAll\" : \"7.0\",\"status\" : \"0\",\"working\" : \"0\",\"loginTime\" : \"10:28:13\",\"workHis\" : [{\"id\" : \"2\",\"name\" : \"a\",\"free\" : \"1\",\"money\" : \"2.0\"},{\"id\" : \"3\",\"name\" : \"c\",\"free\" : \"0\",\"money\" : \"5.0\"}]}]}"
	 * ; Object obj = new JSONParser().parse(s); JSONObject jo = (JSONObject) obj;
	 * JSONArray ja = (JSONArray) jo.get("detail"); ja.forEach(emp ->
	 * parseEmployeeObject((JSONObject) emp, "19:04:03"));
	 * 
	 * }
	 */

	@GET
	@Path("/dummy/{data}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getData(@Context HttpHeaders httpheaders, @PathParam("data") String id) {
		String token = httpheaders.getHeaderString("Authorization");
		String tmp = "";
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
		tmpe.setPosition(Integer.parseInt(employee1.get("sortOrder").toString()));
		tmpe.setTotal(Double.parseDouble(employee1.get("turnAll").toString()));
		tmpe.setTotalTurn(Double.parseDouble(employee1.get("turn").toString()));
		tmpe.setCashTotal(Double.parseDouble(employee1.get("totalCash").toString()));
		tmpe.setCreditTotal(Double.parseDouble(employee1.get("totalCredit").toString()));
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
				}else if (pair.getKey().equals("tipCash")) {
					tn.setTipCash(Double.parseDouble(pair.getValue().toString()));
				}else if (pair.getKey().equals("tipCredit")) {
					tn.setTipCredit(Double.parseDouble(pair.getValue().toString()));
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

	public static ArrayList<ArrayList<Employee>> updatePosition(ArrayList<Employee> employee) {
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

			int j = 1;
			boolean breakF = true;
			while (j <= (tmpFreeWorker.size() - 1)) {
				// int k = j;
				for (int i = j; i > 0; i--) {
					if (tmpFreeWorker.get(j).getTotalTurn() - STEP_TURN < tmpFreeWorker.get(i - 1).getTotalTurn()) {
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
			}
			arrOfArrEmployee.add(tmpFreeWorker);
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
		// System.out.println("\nEmployee Table Details:");
		String s = "{\"status\":";
		if (checkL == 1)
			s += true;
		else
			s += false;
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
				s += "\"sortOrder\" : \"" + employee.get(j).get(index).getPosition() + "\",";
				s += "\"turn\" : \"" + employee.get(j).get(index).getTotalTurn() + "\",";
				s += "\"turnAll\" : \"" + employee.get(j).get(index).getTotal() + "\",";
				s += "\"totalCash\" : \"" + employee.get(j).get(index).getCashTotal() + "\",";
				s += "\"totalCredit\" : \"" + employee.get(j).get(index).getCreditTotal() + "\",";
				s += "\"status\" : \"" + ((employee.get(j).get(index).isActive()) ? "1" : "0") + "\",";
				s += "\"working\" : \"" + ((employee.get(j).get(index).isIsWorking()) ? "1" : "0") + "\",";
				s += "\"loginTime\" : \"" + dtf.format(employee.get(j).get(index).getCheckInTime()) + "\",";
				s += "\"workHis\" : [";
				k = 0;
				for (WorkHis work : employee.get(j).get(index).getTurnListD()) {
					if (k == 0) {
						s += "{";
						k++;
					} else
						s += ",{";
					s += "\"id\" : \"" + work.getId() + "\",";
					s += "\"name\" : \"" + work.getName() + "\",";
					s += "\"free\" : \"" + ((work.isTurn()) ? "1" : "0") + "\",";
					s += "\"money\" : \"" + work.getMoney() + "\",";
					s += "\"tipCash\" : \"" + work.getTipCash() + "\",";
					s += "\"tipCredit\" : \"" + work.getTipCredit() + "\",";
					s += "\"workTime\" : \"" + (work.getWorkTime() == null ? "Unkown" : work.getWorkTime()) + "\"";
					s += "}";
				}
				s += "]";
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
}
