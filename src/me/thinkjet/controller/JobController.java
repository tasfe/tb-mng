package me.thinkjet.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.DwzRender;
import com.jfinal.ext.route.ControllerBind;
import me.thinkjet.model.Area;
import me.thinkjet.model.Job;
import me.thinkjet.model.JobType;
import me.thinkjet.service.AreaService;

@ControllerBind(controllerKey = "/job", viewPath = "job")
public class JobController extends Controller {

	public void index() {
		System.out.println("job");
		int page = getParaToInt("pageNum", 1);
		long area = getParaToInt("area", 0);
		int numPerPage = getParaToInt("numPerPage", 20);
		int type = getParaToInt("type", 0);
		String q = getPara("q", "");
		String where = "where 1=1";
		System.out.println(where);
		if (area > 0) {
			String array = "(" + area;
			for (Area a : AreaService.getAllChildrens(area)) {
				array += "," + a.getLong("id");
			}
			array += ")";
			where += " and Job.area in " + array;
		}
		if (type > 0) {
			where += " and Job.type=" + type;
		}
		if (!q.equals("")) {
			where += " and Job.title LIKE '" + q + "'";
		}
		setAttr("job_list", Job.dao.paginate(page, numPerPage,
				"select Job.*,Firm.name as firm_name,Record.*,Type.name as type_name," +
						"Area.name as area_name,User.name as uname ",
				"from job Job left join firm Firm on Job.firm=Firm.id " +
						"left join job_record Record on Record.id=Job.id " +
						"left join job_type Type on Type.id=Job.type " +
						"left join area Area on Area.id=Job.area " +
						"left join users User on User.id=Job.author " +
						where + " order by Job.id desc"));
	}


	public void show() {
		setAttr("job", Job.dao.findFirst("select Job.*,Firm.name as firm_name,Record.*," +
				"Type.name as type_name,Area.name as area_name,User.name as uname," +
				"User.username,User.avatar from job Job " +
				"left join firm Firm on Job.firm=Firm.id " +
				"left join job_record Record on Record.id=Job.id " +
				"left join job_type Type on Type.id=Job.type " +
				"left join area Area on Area.id=Job.area " +
				"left join users User on User.id=Job.author " +
				"where Job.id=?", getParaToLong()));
	}

	/**
	 * 获取岗位分类
	 */
	public void type() {
		this.setAttr("type", JobType.dao.find("select * from job_type"));
		renderJson();
	}

	public void hot() {
		setAttr("hot", Job.dao.find("select * from job where id in " +
				"(select id from " +
				"(select id from job_record order by views desc limit 0,5) as r" +
				")"));
	}

	public void edit() {
		setAttr("job", Job.dao.findById(getPara()));
	}

	public void update() {
		if (getModel(Job.class).update()) {
			render(DwzRender.closeCurrentAndRefresh("job_edit"));
		} else {
			render(DwzRender.error());
		}
	}

	public void delete() {
		if (Job.dao.deleteById(getPara())) {
			render(DwzRender.success());
		} else {
			render(DwzRender.error());
		}
	}

	public void add() {

	}

	public void create() {
		if (getModel(Job.class).save()) {
			render(DwzRender.closeCurrentAndRefresh("job_add"));
		} else {
			render(DwzRender.error());
		}
	}
}
