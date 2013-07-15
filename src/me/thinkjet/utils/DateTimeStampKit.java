package me.thinkjet.utils;

import com.jfinal.plugin.activerecord.Model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-6
 * Time: 下午5:19
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeStampKit {
	private final static String DATESTAMPNAME = "datetimestamp";

	/**
	 * 生成时间戳，名为DATESTAMPNAME
	 */
	public static void generalDateTimeStamp(List<? extends Model> list, String column_name) {
		for (Model m : list) {
			m.put(DATESTAMPNAME, getDateTimeStamp(m.get(column_name)));
		}
	}

	public static void generalDateTimeStamp(Model model, String column_name) {
		model.put(DATESTAMPNAME, getDateTimeStamp(model.get(column_name)));
	}

	public static String getDateTimeStamp(Object datetime) {
		if (datetime == null) return "";
		long time = 0L;
		if (datetime instanceof Timestamp) {
			time = ((Timestamp) datetime).getTime();
		}
		if (datetime instanceof java.sql.Date) {
			time = ((java.sql.Date) datetime).getTime();
		}
		if (datetime instanceof String) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				time = df.parse((String) datetime).getTime();
			} catch (ParseException e) {
				return "";
			}
		}
		long interval = new Date().getTime() - time;
		if (interval <= 0 || interval > 48 * 3600 * 1000) {
			return getNormalDate(time);
		} else {
			return getPrettyDateTime(interval);
		}
	}

	private static String getPrettyDateTime(long interval) {
		if (interval > 24 * 3600 * 1000) {
			return "昨天";
		}
		if (interval > 3600 * 1000) {
			return (int) (interval / (1000 * 3600)) + "小时前";
		}
		return (int) (interval / (1000 * 60)) + "分钟前";
	}

	private static String getNormalDate(Long time) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date(time));
	}
}
