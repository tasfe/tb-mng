package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import me.thinkjet.model.Users;
import me.thinkjet.service.TagService;
import me.thinkjet.service.UserService;

/**
 * UserController
 */
@ControllerBind(controllerKey = "/admin/users", viewPath = "admin/users")
public class UserController extends Controller {
	public void index() {
		Page<Users> users = Users.dao.paginate(this.getParaToInt("page", 1), 20,
				"select *", "from users");
		for (Users a : users.getList()) {
			a.put("roles", UserService.getRoles(a.getLong("id")));
			a.put("info", UserService.getUserInfo(a.getLong("id")));
		}
		setAttr("users", users);
	}

	public void add() {

	}

	public void edit() {
	}

	public void resetPassword() {
		if(this.getRequest().getMethod().equalsIgnoreCase("get")){
			setAttr("uid",this.getPara());
			render("reset_password.ftl");
			return;
		}else if(this.getRequest().getMethod().equalsIgnoreCase("post")){
			new UserService().resetPassword(this.getParaToLong(),this.getPara("password"));
			this.redirect("/admin/users");
		}
	}

	public void disabled(){

	}

	public void enabled(){

	}
}
