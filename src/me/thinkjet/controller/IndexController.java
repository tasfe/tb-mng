package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/", viewPath = "")
public class IndexController extends Controller {
	public void index() {
	}
}