package me.thinkjet;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.FreeMarkerRender;
import freemarker.template.TemplateModelException;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("config.properties");
		me.setEncoding("utf-8");
		me.setDevMode(getPropertyToBoolean("devMode", true));
		me.setBaseViewPath("/WEB-INF/views");
		me.setError404View("/WEB-INF/views/404.ftl");
		me.setError500View("/WEB-INF/views/500.ftl");
		me.setFreeMarkerViewExtension(".ftl");
		FreeMarkerRender.setProperty("template_update_delay", "0");// 模板更新时间,0表示每次都加载
		FreeMarkerRender.setProperty("whitespace_stripping", "true");// 去除首尾多余空格
		FreeMarkerRender.setProperty("date_format", "yyyy-MM-dd");
		FreeMarkerRender.setProperty("time_format", "HH:mm:ss");
		FreeMarkerRender.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
		FreeMarkerRender.setProperty("default_encoding", "UTF-8");
		try {
			FreeMarkerRender.getConfiguration().setSharedVariable("side_url","http://www.siyanjing.net");
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password").trim());
		dp.set(getPropertyToInt("initialSize", 10),
				getPropertyToInt("minIdle", 10),
				getPropertyToInt("maxActive", 100));
		dp.setMaxWait(this.getPropertyToInt("maxWait", 60000));
		dp.setTimeBetweenEvictionRunsMillis(this.getPropertyToInt(
				"timeBetweenEvictionRunsMillis", 60000));
		dp.setMinEvictableIdleTimeMillis(this.getPropertyToInt(
				"minEvictableIdleTimeMillis", 300000));
		// 使用proxyFilters配置Filter，
		dp.addFilter(new StatFilter());// 添加监控
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);// 添加注入攻击防御
		me.add(dp);
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(dp,
				SimpleNameStyles.LOWER_UNDERLINE);
		me.add(atbp);
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		//me.add(new AuthInterceptor());
	}

	/**
	 * 配置处理器 按顺序处理，Action Handler 将默认在最后调用
	 */
	@Override
	public void configHandler(Handlers me) {
	}

	public static void main(String[] args) {
	}
}
