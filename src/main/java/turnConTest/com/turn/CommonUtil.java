package turnConTest.com.turn;

import java.util.ArrayList;
import java.util.Collections;

import javax.ws.rs.core.Response;

import turnConTest.com.turn.vo.CommonInfo;
import turnConTest.com.turn.vo.TableConvert;

public class CommonUtil {
	public static final String SYS_ERR = "System Error!!!";
	public static String getMsg(Long point, ArrayList<TableConvert> tbC) {
		String result = "";
		String tmp = "";
		ArrayList<Long> cvPoint = new ArrayList<Long>();
		for (TableConvert tableConvert : tbC) {
			tmp = tableConvert.getPoint();
			cvPoint.add(Long.parseLong(tmp));
		}
		cvPoint.sort(null);
	//	Collections.reverse(cvPoint);
		boolean fagNext = false;
		Long prePoint = 0L;
		for (Long rwPoint : cvPoint) {
			if((point < rwPoint) && !fagNext) {
				result = "You have " + point + " point. Earn " + (rwPoint - point) + " point more to get redeem!";
				break;
			} else if((point == rwPoint) && !fagNext) {
				result = "You have " + point + " point. Please choose reward for it!";
				result += "_Do you want reward?";
				break;
			} else if((point <= rwPoint) && fagNext) {
				result = "You have " + point + " point. Please choose reward for " +  prePoint + " point or earn " + (rwPoint - point) + " point more to get best redeem!";
				result += "_Do you want reward?";
				break;
			}
			fagNext = true;
			prePoint = rwPoint;
		}
		return result==""?("You have " + point + " point. Please choose reward for it!" + "_Do you want reward?"):result;
	}
	public static int covertMoneyToPoint(Long point, Long money, String type) {
		if("2".equals(type)) {
			return Math.round(money/point);
		} else if("1".equals(type)) {
			return (int) Math.ceil(money/point);
		} else {
			return (int) Math.floor(money/point);
		} 
	}
	public static Response makeNGStatus(String msg) {
		CommonInfo info = new CommonInfo();
		info.setMsg(msg);
		return Response.status(422).entity(info).build();
	}
	public static CommonInfo makeOKStatus(String msg) {
		CommonInfo info = new CommonInfo();
		info.setStatus("OK");
		info.setMsg(msg);
		return info;
	}
	public static Long parseCompleteMoney(String com) {
		if(com == null || com.isEmpty())
			return 0L;
		String[] tmp = com.split("\\$\\$\\$");
		Long rs = 0L;
		for (String v : tmp) {
			rs += Long.parseLong(v);
		}
		return rs;
	}
}
