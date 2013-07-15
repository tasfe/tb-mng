package me.thinkjet.service;

import me.thinkjet.model.ActivityTags;
import me.thinkjet.model.Tags;

import java.util.List;

public class TagService {

	/**
	 * 向指定id的activity添加标签，如果标签库中存在则直接添加链接，否则新建新的标签，然后生成链接
	 *
	 * @param activity_id
	 * @param tag_name
	 */
	public static void addActivityTag(long activity_id, String tag_name) {
		ActivityTags.dao.set("activity_id", activity_id)
				.set("tag_id", getTagId(tag_name)).save();
	}

	/**
	 * 向指定id的activity添加多个标签，如果标签库中存在则直接添加链接，否则新建新的标签，然后生成链接
	 *
	 * @param activity_id
	 * @param tag_names
	 */
	public void addActivityTags(long activity_id, String[] tag_names) {

	}

	/**
	 * 获取指定的id的activity的标签集
	 *
	 * @param activity_id
	 * @return
	 */
	public static List<Tags> getActivityTags(long activity_id, boolean cache) {
		if (cache) {
			return Tags.dao
					.findByCache(
							"tags",
							"tag_news_" + activity_id,
							"select * from tags where id in (select tags_id from activity_tags where activity_id=?)",
							activity_id);
		} else {
			return Tags.dao
					.find("select * from tags where id in (select tag_id from activity_tags where activity_id=?)",
							activity_id);
		}
	}

	/**
	 * 获取指定的id的activity的标签集
	 *
	 * @param news_id
	 * @return
	 */
	public static List<Tags> getNewsTags(long news_id, boolean cache) {
		if (cache) {
			return Tags.dao
					.findByCache(
							"tags",
							"tag_news_" + news_id,
							"select * from tags where id in (select tag_id from news_tags where news_id=?)",
							news_id);
		} else {
			return Tags.dao
					.find("select * from tags where id in (select tag_id from news_tags where news_id=?)",
							news_id);
		}

	}

	/**
	 * 返回指定名字的tag的id，不存在则新建
	 *
	 * @param name
	 * @return
	 */
	private static long getTagId(String name) {
		Tags tag = Tags.dao.findFirst("select * from tags where name=?", name);
		if (tag == null) {
			tag = new Tags();
			tag.set("name", name).save();
		}
		return tag.getLong("id");
	}

	public static List<Tags> getHotTagsList() {
		return Tags.dao.findByCache("tag", "hot-tag",
				"select * from tags order by count desc limit 0,10");
	}
}
