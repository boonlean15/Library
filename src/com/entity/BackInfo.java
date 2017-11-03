package com.entity;

import java.util.Date;

public class BackInfo {
	private Integer borrowId;	//借阅编号
	private BorrowInfo borrowInfo;
	private Admin admin;	//操作员
	private Integer aid;
	

	private Date backDate;	//归还时间
	
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	

	public Integer getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(Integer borrowId) {
		this.borrowId = borrowId;
	}
	public BorrowInfo getBorrowInfo() {
		return borrowInfo;
	}
	public void setBorrowInfo(BorrowInfo borrowInfo) {
		this.borrowInfo = borrowInfo;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public Date getBackDate() {
		return backDate;
	}
	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	
	public BackInfo() {

	
	}

	
	
	
}
