package you;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.FreeMarkerRender;

import freemarker.template.TemplateModelException;

/**
 * API����ʽ����
 */
public class Config extends JFinalConfig {

	/**
	 * ���ó���
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
		FreeMarkerRender.setProperty("template_update_delay", "0");// ģ�����ʱ��,0��ʾÿ�ζ�����
		FreeMarkerRender.setProperty("whitespace_stripping", "true");// ȥ����β����ո�
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
	 * ����·��
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
	}

	/**
	 * ���ò��
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
		// ʹ��proxyFilters����Filter��
		dp.addFilter(new StatFilter());// ��Ӽ��
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);// ���ע�빥������
		me.add(dp);
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(dp,
				SimpleNameStyles.LOWER_UNDERLINE);
		me.add(atbp);
	}

	/**
	 * ����ȫ��������
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		//me.add(new AuthInterceptor());
	}

	/**
	 * ���ô����� ��˳����Action Handler ��Ĭ����������
	 */
	@Override
	public void configHandler(Handlers me) {
	}

	public static void main(String[] args) {
	}
}
