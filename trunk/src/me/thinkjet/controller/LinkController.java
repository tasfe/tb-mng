package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.DwzRender;
import com.jfinal.ext.route.ControllerBind;
import me.thinkjet.model.Job;
import me.thinkjet.model.Link;

@ControllerBind(controllerKey = "/link")
public class LinkController extends Controller {
	public void index() {
		Link link = Link.dao.findById(getPara());
		link.set("count", link.getInt("count") + 1).update();
		redirect(link.getStr("url"));
	}

	public void edit() {
		setAttr("link", Link.dao.findById(getPara()));
	}

	public void update() {
		if (getModel(Link.class).update()) {
			render(DwzRender.closeCurrentAndRefresh("job_edit"));
		} else {
			render(DwzRender.error());
		}
	}

	public void delete() {
		if (Link.dao.deleteById(getPara())) {
			render(DwzRender.success());
		} else {
			render(DwzRender.error());
		}
	}

	public void add() {

	}

	public void create() {
		if (getModel(Link.class).save()) {
			render(DwzRender.closeCurrentAndRefresh("link_add"));
		} else {
			render(DwzRender.error());
		}
	}
}
