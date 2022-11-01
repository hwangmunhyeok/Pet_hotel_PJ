package com.myboot.admin.reservation.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myboot.admin.reservation.dao.AdminResDAO;


@Service("adminresService")
@Transactional(propagation = Propagation.REQUIRED)
public class AdminResServiceImpl implements AdminResService{
	@Autowired
	private AdminResDAO adminresDAO;

	@Override
	public List adminAllResList() throws Exception{
		List AdminAllResList = null;
		AdminAllResList = adminresDAO.adminResList();
		return AdminAllResList;
	}
	
	@Override
	public List searchResList(Map searchOption) throws Exception{
		List AdminAllResList = null;
		AdminAllResList = adminresDAO.searchadminResList(searchOption);
		return AdminAllResList;
	}
	
	@Override
	public List adminPetList() throws Exception{
		List AdminPetList = null;
		AdminPetList = adminresDAO.adminPetList();
		return AdminPetList;
	}
	
	@Override
	public int adminTotalResNum() throws Exception{
		int adminTotalResNum = 0;
		adminTotalResNum = adminresDAO.adminTotalResNum();
		return adminTotalResNum;
	}
	
	@Override
	public void updateResList(Map reservationMap) throws Exception{
		adminresDAO.updateAdminReservation(reservationMap);
	}
	
	@Override
	public void updatePetList(Map petserviceMap) throws Exception{
		adminresDAO.updateAdminPetservice(petserviceMap);
	}
	
	@Override
	public void deletePetList(String petNum) throws Exception{
		adminresDAO.deleteAdminPetList(petNum);
	}
	
	@Override
	public void updateResState(String reserNum) throws Exception{
		adminresDAO.updateAdminResState(reserNum);
	}

	
}
