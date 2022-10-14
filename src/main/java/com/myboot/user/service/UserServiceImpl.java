package com.myboot.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myboot.user.dao.UserDAO;
import com.myboot.user.vo.UserVO;

@Service("userService")
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDAO;
//	로그인
	public UserVO login(UserVO userVO) throws Exception{
		return userDAO.loginById(userVO);
	}
	
	public List listUser() throws Exception {
		List userList = null;
		userList = userDAO.selectAllUserList();
		return userList;
	}

}
