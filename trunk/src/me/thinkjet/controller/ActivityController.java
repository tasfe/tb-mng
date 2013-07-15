package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.DwzRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import me.thinkjet.model.Activity;
import me.thinkjet.model.Job;
import me.thinkjet.service.TagService;
import me.thinkjet.service.UserService;

@ControllerBind(controllerKey = "/activity", viewPath = "activity")
public class ActivityController extends Controller {

	public void index() {
		Page<Activity> activities = Activity.dao.paginate(this.getParaToInt("page", 1), 20,
				"select *", "from activity");
		for (Activity a : activities.getList()) {
			a.put("tags", TagService.getActivityTags(a.getLong("id"), false));
			a.put("author_name",
					new UserService().findUserByUId(a.getLong("author"))
							.getStr("name"));
		}
		setAttr("activitylist", activities);
	}

	public void status(){
		Activity a = Activity.dao.findById(this.getParaToLong("id"));
		a.set("status",this.getParaToInt("status")).update();
		renderText("状态已更新");
	}

	public void upload() {
		this.setAttr("a_id", this.getPara("a_id"));
		render("upload.ftl");
	}

	public void edit() {
		setAttr("activity", Activity.dao.findById(getPara()));
	}

	public void update() {
		if (getModel(Activity.class).update()) {
			render(DwzRender.closeCurrentAndRefresh("activity_edit"));
		} else {
			render(DwzRender.error());
		}
	}

	public void delete() {
		if (Activity.dao.deleteById(getPara())) {
			render(DwzRender.success());
		} else {
			render(DwzRender.error());
		}
	}

	public void add() {

	}

	public void create() {
		if (getModel(Activity.class).save()) {
			render(DwzRender.closeCurrentAndRefresh("activity_add"));
		} else {
			render(DwzRender.error());
		}
	}
}
