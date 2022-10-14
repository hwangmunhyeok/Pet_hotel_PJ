package com.myboot.user.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myboot.user.vo.UserVO;

public interface UserController {
	public ModelAndView login(@ModelAttribute("user") UserVO user,
            RedirectAttributes  rAttr,
            HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView modMember(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView pw_change(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public String userMain(Model model);
}
