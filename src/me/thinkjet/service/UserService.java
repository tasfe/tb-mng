package me.thinkjet.service;

import java.util.Date;
import java.util.List;

import me.thinkjet.model.UserInfo;
import me.thinkjet.model.UserRole;
import me.thinkjet.model.Users;
import me.thinkjet.utils.encoder.PasswordEncoder;

import org.apache.commons.lang3.StringUtils;

public class UserService {
	/**
	 * 
	 * @param loginName
	 *            username or password
	 * @return
	 */
	public Users getUser(String loginName) {
		return StringUtils.indexOf(loginName, '@') > 0 ? this
				.findUserByEmail(loginName) : this
				.findUserByUsername(loginName);
	}

	public String getEncodedPassword(String password, String username) {
		return PasswordEncoder.encode(password, username);
	}

	public void createUser(Users user) {
		user.set(
				"password",
				this.getEncodedPassword(user.getStr("password"),
						user.getStr("username"))).save();
	}

	public void modifyself(Users user) {

	}

	public void resetPassword(Users user) {
		user.set(
				"password",
				this.getEncodedPassword(user.getStr("password"),
						user.getStr("username"))).update();
	}

	public void resetPassword(long uid,String new_password){
		System.out.println(new_password);
		Users u = findUserByUId(uid);
		u.set(
				"password",
				this.getEncodedPassword(new_password,
						u.getStr("username"))).update();
	}

	public Users findUserByUsername(String username) {
		return Users.dao.findFirst("select * from users where username = ?",
				username);
	}

	public Users findUserByEmail(String email) {
		return Users.dao
				.findFirst("select * from users where email = ?", email);
	}

	public Users findUserByUId(Long uid) {
		return Users.dao.findById(uid);
	}

	public void UpdateUserStatus(Users u) {
		u.set("lastlogin", new Date()).update();
	}

	public boolean checkExist(String name) {
		return this.getUser(name) != null;
	}

	public static List<UserRole> getRoles(long user_id){
		System.out.println(user_id);
		return UserRole.dao.find("select role from user_role where user_id=?",user_id);
	}

	public static UserInfo getUserInfo(long user_id){
		return UserInfo.dao.findFirst("select * from user_info where uid=?",user_id);
	}

}
