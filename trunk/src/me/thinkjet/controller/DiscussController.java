package me.thinkjet.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.DwzRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import me.thinkjet.model.Discuss;
import me.thinkjet.model.Job;

@ControllerBind(controllerKey = "/discuss", viewPath = "discuss")
public class DiscussController extends Controller {
	@Before(CacheInterceptor.class)
	@CacheName("discuss")
	public void index() {
		this.setAttr(
				"discuss",
				Discuss.dao.paginateByCache("discuss",
						"index_" + this.getParaToInt(1, 1),
						this.getParaToInt(1, 1), 10, "select d.*, r.*, u.username, u.name",
						"from discuss d left join discuss_record r on d.id=r.discuss_id " +
								"left join users u on u.id=d.author"));
	}

	@Before(CacheInterceptor.class)
	@CacheName("discuss")
	public void show() {
		this.setAttr("discuss", Discuss.dao.findById(this.getParaToLong()));
	}

	public void edit() {
		setAttr("discuss", Discuss.dao.findById(getPara()));
	}

	public void update() {
		if (getModel(Discuss.class).update()) {
			render(DwzRender.closeCurrentAndRefresh("discuss_edit"));
		} else {
			render(DwzRender.error());
		}
	}

	public void delete() {
		if (Discuss.dao.deleteById(getPara())) {
			render(DwzRender.success());
		} else {
			render(DwzRender.error());
		}
	}

	public void add() {

	}

	public void create() {
		if (getModel(Job.class).save()) {
			render(DwzRender.closeCurrentAndRefresh("discuss_add"));
		} else {
			render(DwzRender.error());
		}
	}


}