package me.thinkjet.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.DwzRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import me.thinkjet.model.Firm;
import me.thinkjet.model.Job;
import me.thinkjet.model.News;

@ControllerBind(controllerKey = "/firm", viewPath = "firm")
public class FirmController extends Controller {
	public void index() {
		this.setAttr("firms", Firm.dao.paginate(1, 10, "select *", " from firm"));
	}

	public void edit() {
		setAttr("job", Job.dao.findById(getPara()));
	}

	public void update() {
		if (getModel(Firm.class).update()) {
			render(DwzRender.closeCurrentAndRefresh("firm_edit"));
		} else {
			render(DwzRender.error());
		}
	}

	public void delete() {
		if (Firm.dao.deleteById(getPara())) {
			render(DwzRender.success());
		} else {
			render(DwzRender.error());
		}
	}

	public void add() {

	}

	public void create() {
		if (getModel(Firm.class).save()) {
			render(DwzRender.closeCurrentAndRefresh("firm_add"));
		} else {
			render(DwzRender.error());
		}
	}

}
