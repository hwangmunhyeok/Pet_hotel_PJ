package com.myboot.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myboot.user.service.UserService;
import com.myboot.user.vo.UserVO;

@Controller("userController")
public class UserControllerImpl implements UserController{
	@Autowired
	private UserService userService;
	@Autowired
	private UserVO userVO;
	
	@ResponseBody
	@RequestMapping("/user.do") 
	public String userMain(Model model){
		String a= "";
		try {
			List userList = userService.listUser();
			
			  int totalElements = userList.size();

			for(int i=0;i<userList.size();i++) {
				UserVO vo = (UserVO) userList.get(i);
				a += vo.getId()+" ";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return a;
	}
//	로그인창
	@Override
	@RequestMapping(value = "/login.do", method =  RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("user") UserVO user,
			                  RedirectAttributes  rAttr,
			                  HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		System.out.println(user);
		userVO = userService.login(user);
		if(userVO!= null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", userVO);
			session.setAttribute("isLogOn", true);
			
			String action = (String)session.getAttribute("action");
			session.removeAttribute("action");
			
			if(action!= null) {
				mav.setViewName("redirect:"+action);
			}else {
				mav.setViewName("redirect:/main.do");	
			}
			
		}else {
			rAttr.addAttribute("result","loginFailed");
			mav.setViewName("redirect:/loginForm.do");
		}
		
		
		return mav;
	}
//	로그아웃 기능추가
	@Override
	@RequestMapping(value = "/logout.do", method =  RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		session.setAttribute("isLogOn",false);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main.do");
		return mav;
	}	
//	회원가입 회원추가
	@Override
	@RequestMapping(value="/addUser.do" ,method = RequestMethod.POST)
	public ModelAndView addUser(@ModelAttribute("user") UserVO user,
			                  HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int result = 0;
		result = userService.addUser(user);
		ModelAndView mav = new ModelAndView("redirect:/listusers.do");
		return mav;
	}
	
	//myboot3에서 *form을 그대로 들고왔습니다.
	@RequestMapping(value = "/loginForm.do", method =  RequestMethod.GET) 
	  public ModelAndView form(@RequestParam(value= "result", required=false) String result,
					              @RequestParam(value= "action", required=false) String action,
					              HttpServletRequest request, 
					              HttpServletResponse response) throws Exception {
				String viewName = (String)request.getAttribute("viewName");
				HttpSession session = request.getSession();
				session.setAttribute("action", action); 
				
				ModelAndView mav = new ModelAndView();
				mav.addObject("result",result);
				mav.setViewName(viewName);
				return mav;
	}

	@Override
	@RequestMapping(value="/pw_change.do" ,method = RequestMethod.GET)
	public ModelAndView pw_change(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		HttpSession session=request.getSession();
		session=request.getSession();
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
    }
	
	@Override
	@RequestMapping(value="/modMember.do" ,method = RequestMethod.GET)
	public ModelAndView modMember(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		HttpSession session=request.getSession();
		session=request.getSession();
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
}

