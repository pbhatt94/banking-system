package com.wg.banking.service;

import java.util.Map;

import com.wg.banking.constants.StringConstants;
import com.wg.banking.constants.UserConstants;
import com.wg.banking.dao.LoginDAO;
import com.wg.banking.helper.PasswordUtil;
import com.wg.banking.model.User;

public class LoginService {


	public static User authenticateUser(String username, String password) {
		Map<String,String> mp = LoginDAO.authenticateUser(username, password);
		if(mp.isEmpty()) {
			System.out.println(StringConstants.INVALID_USERNAME);
			return null;	
		} 		
		
		String hashedPassword = mp.get(UserConstants.PASSWORD_COLUMN);
		boolean isPasswordCorrect = PasswordUtil.checkPassword(password, hashedPassword);
		if(isPasswordCorrect) {
			return getUserObject(mp);
		}
		return null;
	}
	
	
	private static User getUserObject(Map<String, String> mp) {
		User user = new User(); 
        user.setUserId(mp.get(UserConstants.USER_ID_COLUMN));
        user.setName(mp.get(UserConstants.NAME_COLUMN));
        user.setEmail(mp.get(UserConstants.EMAIL_COLUMN));
        user.setUsername(mp.get(UserConstants.USERNAME_COLUMN));
        user.setPassword(mp.get(UserConstants.PASSWORD_COLUMN));
        user.setAge(Integer.parseInt(mp.get(UserConstants.AGE_COLUMN)));
        user.setGender(User.Gender.valueOf(mp.get(UserConstants.GENDER_COLUMN)));
        user.setPhoneNo(mp.get(UserConstants.PHONE_NO_COLUMN));
        user.setAddress(mp.get(UserConstants.ADDRESS_COLUMN));
        user.setRole(User.Role.valueOf(mp.get(UserConstants.ROLE_COLUMN)));
        
        return user;
	}
}