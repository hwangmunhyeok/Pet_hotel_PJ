package com.myboot.user.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
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
			List userList = userService.listUsers();
			
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
	@Override
	@RequestMapping(value= "/listUsers.do", method = RequestMethod.GET)
	public ModelAndView listUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
	//public String listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//String viewName = (String)request.getAttribute("viewName");
		List usersList = userService.listUsers();
		//ModelAndView mav = new ModelAndView(viewName);
		ModelAndView mav = new ModelAndView("/user/listMembers");
		
		mav.addObject("usersList", usersList);
		return mav;
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
	
//	@RequestMapping("/userForm.do")
//	  public String userForm(Model model){
//		return "/userForm";
//	  }
	@RequestMapping(value= "/userForm.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView userForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		return mav;
		
	}
//	로그인 아이디 찾기 폼
	@RequestMapping(value = "/find_id_form.do")
	public ModelAndView find_id_form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		return mav;
	}
//	로그인 비밀번호 찾기 폼
	@RequestMapping(value = "/find_pw_form.do")
	public ModelAndView find_pw_form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		return mav;
	}
//	로그인 아이디 찾기
	@RequestMapping(value = "/find_id.do", method = RequestMethod.POST)
	public String find_id(HttpServletResponse response,@RequestParam("name") String name, @RequestParam("email") String email, Model md) throws Exception{
		
		UserVO userVO = new UserVO();
		userVO.setName(name);
		userVO.setEmail(email);
		
		md.addAttribute("id", userService.find_id(response, userVO));
		return "/find_id";
	}
//	로그인 비밀번호 찾기
	@RequestMapping(value = "/find_pw.do", method = RequestMethod.POST)
	public String find_pw(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("tel") String tel,Model md) throws Exception{
		UserVO userVO= new UserVO();
		userVO.setId(id);
		userVO.setTel(tel);
		
		md.addAttribute("pw", userService.find_pw(response, userVO));
		return "/find_pw";
	}
	
////	회원가입 회원추가
	@Override
	@RequestMapping(value = "/addUser.do", method = RequestMethod.POST)
	public ModelAndView addUser(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		request.setCharacterEncoding("utf-8");
		int result = 0;
		String id= request.getParameter("id");
		String pw= request.getParameter("pw");
		String name= request.getParameter("name");
		String email=request.getParameter("email");
		String mail2=request.getParameter("mail2");
		String tel=request.getParameter("tel");
		String tel_sub=request.getParameter("tel_sub");
		String message=request.getParameter("message");
		String birth=request.getParameter("birth");
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date formatDate = dtFormat.parse(birth);
		System.out.println(birth);
		
		
		UserVO userVO= new UserVO();
		userVO.setId(id);
		userVO.setPw(pw);
		userVO.setName(name);
		userVO.setEmail(email+mail2);
		userVO.setTel(tel);
		userVO.setTel_sub(tel_sub);
		userVO.setMessage(message);
		if(message==null || message==""){
			userVO.setMessage("N");
			System.out.println("N");
		}
		userVO.setBirth(formatDate);
		System.out.println(userVO.getId()+userVO.getPw()+userVO.getName()+userVO.getEmail()+userVO.getTel()+userVO.getTel_sub()+userVO.getMessage()+userVO.getBirth());
		result = userService.addUser(userVO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main.do");
		return mav;
		
	}
	


//	회원가입 id 중복 확인 기능
	 @ResponseBody // 값 변환을 위해 꼭 필요함
		@GetMapping("idCheck") // 아이디 중복확인을 위한 값으로 따로 매핑
		public int overlappedID(UserVO userVO) throws Exception{
			int result = userService.overlappedID(userVO); // 중복확인한 값을 int로 받음
			return result;
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
	
	// 한번 더 비밀번호 입력 폼
	@RequestMapping(value = "/pw_changeForm.do", method =  RequestMethod.GET)
	private ModelAndView Form(@RequestParam(value= "result", required=false) String result,
			                  @RequestParam(value= "action", required=false) String action,
			                  HttpServletRequest request, 
			                  HttpServletResponse response) throws Exception {
		if(request.getHeader("REFERER") == null) {
			request.setAttribute("stmsgcheck", "1");
			request.setAttribute("stmsg", "비정상적인 접근입니다!");
			ModelAndView mav = new ModelAndView("forward:/main.do");
			return mav;
		}
		
		String viewName = (String)request.getAttribute("viewName");
		HttpSession session = request.getSession();
		session.setAttribute("action", action); 
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("result",result);
		mav.setViewName(viewName);
		return mav;
	}
	
	// 한번 더 비밀번호 입력
	@Override
	@RequestMapping(value="/pw_change.do" , method = RequestMethod.POST)
	public ModelAndView pw_change(
            @RequestParam(value= "password", required=false) String password,
			RedirectAttributes rAttr, 
				HttpServletRequest request, HttpServletResponse response)  throws Exception {
		ModelAndView mav = new ModelAndView();
		
			HttpSession session = request.getSession();
			userVO = (UserVO) session.getAttribute("user");
			System.out.println(userVO);
			System.out.println(password);
			System.out.println(userVO.getPw());
			String userPw =userVO.getPw();
			//날짜 포맷    			
    		SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd");
    		
    		// String 타입을 Date 타입으로 변환
    		String strNowDate = newDtFormat.format(userVO.getBirth());
    		session.setAttribute("birth", strNowDate);
			
			if (userPw.equals(password)) {
				System.out.println("성공");
				
				mav.setViewName("redirect:/modMemberForm.do");	
			
			}else {
				rAttr.addAttribute("result","passwordFailed");
				mav.setViewName("redirect:/pw_changeForm.do");
				System.out.println("실패");
			}
		return mav;
			
	}
	
	// 회원 정보 수정				
    @Override
	@RequestMapping(value="/modMember.do" ,method = RequestMethod.POST)
	public ResponseEntity modMember(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception{
    		
    	int result = 0;
			Map<String,Object> user = new HashMap<String, Object>();
		
		   Enumeration enu=multipartRequest.getParameterNames();
		   
		   while(enu.hasMoreElements()){
		      String name=(String)enu.nextElement();
		      String value=multipartRequest.getParameter(name);
		      user.put(name,value);
		      
		}
		
		String birth=multipartRequest.getParameter("birth_string");
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date formatDate = dtFormat.parse(birth);
		user.put("birth",formatDate);
		
		if(user.get("message")==null || user.get("message")==""){
			user.put("message","N");
			System.out.println("N");
		}
		
		String imageFileName= upload(multipartRequest, (String) user.get("id"));
		
		if(imageFileName!=null&&imageFileName!="") {
			user.put("img_name", imageFileName);
		}else {
			user.put("img_name", (String) user.get("oldFileName"));
		}
		
		//============================================realPath 받아오기
		String realPath = multipartRequest.getSession().getServletContext().getRealPath("");
		String path = realPath+"resources\\user\\user_image";
		
		String message = null;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			result = userService.modMember(user);
			HttpSession session = multipartRequest.getSession();
			UserVO oldUserVO = (UserVO) session.getAttribute("user");
			UserVO newUserVO = new UserVO();
			newUserVO.setId((String) user.get("id"));
			newUserVO.setPw((String) user.get("pw"));
			
			userVO = userService.login(newUserVO);
					
			System.out.println(userVO);
			session.removeAttribute("user");
			session.setAttribute("user",userVO);
			
			if(imageFileName!=null && imageFileName.length()!=0) {
				
				File oldFile = new File(path+"\\"+imageFileName);
				if(oldFile.exists()) {
					oldFile.delete();
				}
				
				File srcFile = new File(path+ "\\" + "temp"+ "\\" + imageFileName);
				File destDir = new File(path);
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
	
			message = "<script>";
			message += " alert('정보 수정 했습니다.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/mypage/myPage.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			File srcFile = new File(path+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			message = " <script>";
			message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/modMemberForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	
	@RequestMapping(value = "/modMemberForm.do", method =  RequestMethod.GET)
	private ModelAndView modMemberForm(@RequestParam(value= "result", required=false) String result,
			                  @RequestParam(value= "action", required=false) String action,
			                  HttpServletRequest request, 
			                  HttpServletResponse response) throws Exception {
		if(request.getHeader("REFERER") == null) {
			request.setAttribute("stmsgcheck", "1");
			request.setAttribute("stmsg", "비정상적인 접근입니다!");
			ModelAndView mav = new ModelAndView("forward:/main.do");
			return mav;
		}
		String viewName = (String)request.getAttribute("viewName");
		HttpSession session = request.getSession();
		session.setAttribute("action", action); 
		
		ModelAndView mav= new ModelAndView();
		mav.addObject("result",result);
		mav.setViewName(viewName);
		return mav;
	}

	// 탈퇴하기

	@Override
	@RequestMapping(value="/retiring.do" ,method = RequestMethod.POST)
	public ModelAndView retiring(@RequestParam("id") String id, 
			           HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		session.setAttribute("isLogOn",false);
		userService.retiring(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main.do");
		return mav;
	}
	
//	프로필 이미지
	@Override
	@RequestMapping(value="/adduserprofil_pic.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity adduserprofil_pic(MultipartHttpServletRequest multipartRequest, 
	HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		
//		Map<String,Object> userMap = new HashMap<String, Object>();
//		Enumeration enu=multipartRequest.getParameterNames();
//		while(enu.hasMoreElements()){
//			String name=(String)enu.nextElement();
//			String value=multipartRequest.getParameter(name);
//			userMap.put(name,value);
//		}
//		String id_ = (String) userMap.get("id");
		
		String message;
		ResponseEntity resEnt=null;
		
		int result = 0;
		String id= multipartRequest.getParameter("id");
		String pw= multipartRequest.getParameter("pw");
		String name= multipartRequest.getParameter("name");
		String email=multipartRequest.getParameter("email");
		String mail2=multipartRequest.getParameter("mail2");
		String tel=multipartRequest.getParameter("tel");
		String tel_sub=multipartRequest.getParameter("tel_sub");
		String message_=multipartRequest.getParameter("message");
		String birth=multipartRequest.getParameter("birth");
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date formatDate = dtFormat.parse(birth);
		System.out.println(birth);
		
		UserVO userVO= new UserVO();
		userVO.setId(id);
		userVO.setPw(pw);
		userVO.setName(name);
		userVO.setEmail(email+mail2);
		userVO.setTel(tel);
		userVO.setTel_sub(tel_sub);
		userVO.setMessage(message_);
		if(message_==null || message_==""){
			userVO.setMessage("N");
			System.out.println("N");
		}
		userVO.setBirth(formatDate);
		System.out.println(userVO.getId()+userVO.getPw()+userVO.getName()+userVO.getEmail()+userVO.getTel()+userVO.getTel_sub()+userVO.getMessage()+userVO.getBirth());
		
		String imageFileName= upload(multipartRequest, id);
		userVO.setImg_name(imageFileName);
		
		//============================================realPath 받아오기
		String realPath = multipartRequest.getSession().getServletContext().getRealPath("");
		String path = realPath+"resources\\user\\user_image";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			result = userService.addUser(userVO);
			if(imageFileName!=null && imageFileName.length()!=0) {
				File srcFile = new File(path+ "\\" + "temp"+ "\\" + imageFileName);
				File destDir = new File(path);
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
	
			message = "<script>";
			message += " alert('회원가입을 축하합니다');";
			message += " location.href='"+multipartRequest.getContextPath()+"/main.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			File srcFile = new File(path+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			message = " <script>";
			message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/userForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	
	
	//한개 이미지 업로드하기
		private String upload(MultipartHttpServletRequest multipartRequest, String id) throws Exception{
			String imageFileName= null;
			Iterator<String> fileNames = multipartRequest.getFileNames();
			String fileType = multipartRequest.getMultipartContentType("imageFileName");
			String picFileName =null;
			
			
			//============================================realPath 받아오기
			String realPath = multipartRequest.getSession().getServletContext().getRealPath("");
			String path = realPath+"resources\\user\\user_image";
			
			while(fileNames.hasNext()){
				String fileName = fileNames.next();
							
				MultipartFile mFile = multipartRequest.getFile(fileName);
				imageFileName=mFile.getOriginalFilename();
				System.out.println(imageFileName);
				
				if(imageFileName==null || imageFileName=="") {
					return "";
				}
				
				String picfileType = imageFileName.substring(imageFileName.lastIndexOf("."));
				System.out.println(picfileType);
				
				picFileName = id + picfileType;
				System.out.println(picFileName);
				
				File file = new File(path +"\\"+"temp"+"\\" + fileName);
				if(mFile.getSize()!=0){ //File Null Check
					if(!file.exists()){ //경로상에 파일이 존재하지 않을 경우
						file.getParentFile().mkdirs();  //경로에 해당하는 디렉토리들을 생성
						mFile.transferTo(new File(path +"\\"+"temp"+ "\\"+picFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
					}
				}
			}
			return picFileName;
		}
		
}

