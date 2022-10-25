package com.myboot.review.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.myboot.review.vo.ReviewVO;

@Controller
public  interface ReviewService {

	

	

	public int addNewReview(Map reviewMap, String fileName)throws Exception;



	public Map reviewDetail_1(Map pagingMap) throws Exception;



	public void removeReview(int reviewNO)throws Exception;



		public Map reviewDetail_2(Map pagingMap) throws Exception;
		public Map reviewDetail_3(Map pagingMap) throws Exception;

	}

	

