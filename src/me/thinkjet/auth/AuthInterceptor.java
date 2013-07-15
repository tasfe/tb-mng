package me.thinkjet.auth;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * @author johnny_zyc
 * @ClassName AuthInterceptor
 * @Modified 2013-4-13 下午12:34:51
 */
public class AuthInterceptor implements Interceptor {

	final public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();

	}

}
