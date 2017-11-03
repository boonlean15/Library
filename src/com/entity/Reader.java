package com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 读者类
 * @author c
 *
 */
public class Reader implements Serializable{

	private String readerId;	//自动编号
	private String name;	//真实名称
	private String pwd;	//密码
	private String phone;	//联系方式
	private ReaderType readerType;	//读者类型(学生或者教师)
    private String email;	//邮箱
    private Admin admin;	//操作管理员
    private String paperNO;	//证件号码
	private Date createTime;	//创建时间
	private List<BorrowInfo> borrowInfos;	//该读者的借阅信息
	private Integer readerTypeId;
	private Integer aid;



	public Integer getReaderTypeId() {
		return readerTypeId;
	}

	public void setReaderTypeId(Integer readerTypeId) {
		this.readerTypeId = readerTypeId;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ReaderType getReaderType() {
		return readerType;
	}

	public void setReaderType(ReaderType readerType) {
		this.readerType = readerType;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public String getPaperNO() {
		return paperNO;
	}

	public void setPaperNO(String paperNO) {
		this.paperNO = paperNO;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<BorrowInfo> getBorrowInfos() {
		return borrowInfos;
	}

	public void setBorrowInfos(List<BorrowInfo> borrowInfos) {
		this.borrowInfos = borrowInfos;
	}




	public Reader() {

	
	
	}

	public Reader(String name, String pwd, String phone,
			ReaderType readerType, String email, Admin admin, String paperNO,
			Date createTime) {
		super();
		this.name = name;
		this.pwd = pwd;
		this.phone = phone;
		this.readerType = readerType;
		this.email = email;
		this.admin = admin;
		this.paperNO = paperNO;
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Reader [readerId=" + readerId + ", name=" + name + ", pwd=" + pwd + ", phone=" + phone + ", readerType="
				+ readerType + ", email=" + email + ", admin=" + admin + ", paperNO=" + paperNO + ", createTime="
				+ createTime + ", borrowInfos=" + borrowInfos + "]";
	}


	



	
	
	
	
	
}
