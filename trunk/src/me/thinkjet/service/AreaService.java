package me.thinkjet.service;

import me.thinkjet.model.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-30
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public class AreaService {
	/**
	 * 返回制定id下的所有下一级子地址id
	 *
	 * @param parent
	 * @return
	 */
	public static List<Area> getChildrens(long parent) {
		if (parent > 0) {
			return Area.dao.find("select * from area where parent=?", parent);
		} else {
			return null;
		}
	}

	/**
	 * 返回制定id下的所有二级和三级子地址id
	 *
	 * @param parent
	 * @return
	 */
	public static List<Area> getAllChildrens(long parent) {
		List<Area> areas = new ArrayList<Area>();
		getAllChildrens(parent, areas);
		return areas;
	}

	private static void getAllChildrens(long parent, List<Area> lists) {
		if (parent > 0) {
			List<Area> temp;
			if ((temp = getChildrens(parent)) != null) {
				for (Area a : temp) {
					getAllChildrens(a.getLong("id"), lists);
				}
				lists.addAll(temp);
			}
		}
	}
}
