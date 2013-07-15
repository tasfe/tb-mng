package me.thinkjet.controller;

import me.thinkjet.model.Activity;
import me.thinkjet.model.ActivityPhoto;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;

@ControllerBind(controllerKey = "/admin/activity_photo", viewPath = "admin/activity_photo")
public class ActivityPhotoController extends Controller {

	@Before(CacheInterceptor.class)
	@CacheName("photo")
	public void index() {
		int page = this.getParaToInt("page", 1);
		int aid = this.getParaToInt("a_id");
		setAttr("plist", Activity.dao.paginateByCache("photo", "photo-" + page,
				page, 20, "select p.*,a.title as title",
				"from activity_photo p left join activity a on"
						+ " p.activity_id = a.id where a.id = " + aid
						+ " order by p.id desc"));
	}

	public void create() {
		System.out.println("create");
		ActivityPhoto p = this.getModel(ActivityPhoto.class);
		p.save();
		redirect("/admin/upload?a_id=" + p.get("activity_id"));
	}

	public void update() {
		ActivityPhoto p = getModel(ActivityPhoto.class);
		p.update();
		render("show.html");
	}

	public void edit() {
		setAttr("photo", ActivityPhoto.dao.findById(getPara("id")));
	}

	public void delete() {
		ActivityPhoto.dao.deleteById(this.getAttr("id"));
		this.render("index/html");
	}

}
