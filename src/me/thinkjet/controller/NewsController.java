package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import me.thinkjet.model.Job;
import me.thinkjet.model.JobRecord;
import me.thinkjet.model.News;
import me.thinkjet.service.TagService;
import me.thinkjet.service.UserService;

@ControllerBind(controllerKey = "/news", viewPath = "/news")
public class NewsController extends Controller {
	public void index() {
		Page<News> news = News.dao.paginate(this.getParaToInt("page", 1), 20,
				"select *", "from news");
		for (News n : news.getList()) {
			n.put("tags", TagService.getNewsTags(n.getLong("id"), false));
			n.put("author_name",
					new UserService().findUserByUId(n.getLong("author"))
							.getStr("name"));
		}
		setAttr("news", news);
	}

	public void index1() {
		this.setAttr("news", News.dao.paginateByCache("news",
				"index_" + this.getParaToInt(1, 1),
				this.getParaToInt(1, 1), 10, "select n.*, r.*, u.username, u.name",
				"from news n left join news_record r on n.id=r.news_id " +
						"left join users u on u.id=n.author"));
	}

	public void hot() {
		setAttr("hot", News.dao.findByCache("news", "news-hot",
				"select * from news where id in " +
						"(select news_id from " +
						"(select news_id from news_record order by views desc limit 0,5) as r" +
						")"));
	}

	public void add() {

	}

	public void edit() {

	}

	public void create() {
		News a = getModel(News.class);
//		a.set("author", AuthManager.getSessionUser(this).getUser().get("id"));
		a.save();
		this.redirect("index");
	}

	public void delete() {
		JobRecord.dao.deleteById(this.getPara("id"));
		Job.dao.deleteById(this.getPara("id"));
		render("index.html");
	}


}
