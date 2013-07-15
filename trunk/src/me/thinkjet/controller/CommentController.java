package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import me.thinkjet.model.Comment;

/**
 * CommonController
 */
@ControllerBind(controllerKey = "/admin/comment", viewPath = "/admin/comment")
public class CommentController extends Controller {
	public void index() {
		int page = this.getParaToInt(0, 1);
		this.setAttr("comment_list", Comment.dao.paginate(page, 20, "select c.*,u.avatar as avatar",
				"from comment c left join users u "
						+ "on c.comment_uname=u.username order by c.id asc"));
	}

	public void delete(){
		Comment.dao.deleteById(this.getPara());
		this.redirect(this.getRequest().getHeader("Referer"));
	}
}
