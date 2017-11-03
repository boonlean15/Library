CREATE TABLE `admin` ( 管理员表
  `aid` int(11) NOT NULL,  管理员id
  `username`  varchar(20) BINARY NOT NULL, 管理员用户名
  `name` varchar(20) DEFAULT NULL, 管理员姓名
  `pwd` varchar(64) DEFAULT NULL, 管理员密码
  `phone` varchar(20) DEFAULT NULL, 管理员联系电话
  `state` int(2) DEFAULT '1',  管理员状态
  PRIMARY KEY (`aid`)  主键
);
CREATE TABLE `authorization` (  权限表
  `aid` int(11) NOT NULL,  //管理员id
  `bookSet` int(2) DEFAULT '0', //图书设置权限
  `readerSet` int(2) DEFAULT '0',//读者设置权限
  `borrowSet` int(2) DEFAULT '0',//借阅设置权限
  `typeSet` int(2) DEFAULT '0',//图书分类设置权限
  `backSet` int(2) DEFAULT '0',//归还设置权限
  `forfeitSet` int(2) DEFAULT '0',//逾期设置权限
  `sysSet` int(2) DEFAULT '0',//系统设置权限
  `superSet` int(2) DEFAULT '0',//超级管理权限
  PRIMARY KEY (`aid`),主键
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`) 外键
);


CREATE TABLE `readertype` ( 读者类型表
  `readerTypeId` int(11) NOT NULL,//读者类型Id
  `readerTypeName` varchar(255) NOT NULL,//读者类型名称
  `maxNum` int(5) NOT NULL,//最大借书量
  `bday` int(5) NOT NULL,//可借天数
  `penalty` double NOT NULL,//每日罚金
  `renewDays` int(5) NOT NULL,//续借天数
  PRIMARY KEY (`readerTypeId`)主键
);

CREATE TABLE `reader` ( 读者表
  `readerId` varchar(255) NOT NULL,//自动编号 读者ID
  `pwd` varchar(64) NOT NULL,//密码
  `name` varchar(20) NOT NULL,//真实名称
  `paperNO` varchar(20) UNIQUE NOT NULL,//证件号码
  `phone` varchar(20) DEFAULT NULL,//联系方式
  `email` varchar(30) DEFAULT NULL,//邮箱
  `createTime` datetime DEFAULT NULL,//创建时间
  `readerTypeId` int(11) DEFAULT NULL,//读者类型(学生或者教师)
  `aid` int(11) DEFAULT NULL,//操作管理员
  PRIMARY KEY (`readerId`),
  CONSTRAINT  FOREIGN KEY (`readerTypeId`) REFERENCES `readertype` (`readerTypeId`) ON DELETE SET NULL,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
);

CREATE TABLE `booktype` (图书类型表
  `typeId` int(11) NOT NULL,//图书类型编号
  `typeName` varchar(20) NOT NULL,//图书类型名称
  PRIMARY KEY (`typeId`)
);

CREATE TABLE `book` (图书表
  `bookId` int(11) NOT NULL,//图书编号
  `bookName` varchar(20) NOT NULL,//图书名称
  `ISBN` varchar(20)  UNIQUE NOT NULL,//ISBN　国际标准书号
  `autho` varchar(20) DEFAULT  NULL,//作者名称
  `num` int(11) NOT NULL,//总数量
  `currentNum` int(5) NOT NULL,//在馆数量
  `press` varchar(20) DEFAULT NULL,//出版社
  `description` varchar(255) DEFAULT NULL,//简介
  `price` double NOT NULL,//价格
  `putdate` datetime DEFAULT NULL,//上架日期
  `typeId` int(11) DEFAULT NULL,//图书类型
  `aid` int(11) DEFAULT NULL,//操作管理员
  PRIMARY KEY (`bookId`),主键
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`),
  CONSTRAINT  FOREIGN KEY (`typeId`) REFERENCES `booktype` (`typeId`) ON DELETE SET NULL
) ;

CREATE TABLE `borrowinfo` (借阅信息表
  `borrowId` int(11) NOT NULL,//借阅编号
  `borrowDate` datetime DEFAULT NULL,//借阅日期
  `endDate` datetime DEFAULT NULL,//截止日期
  `overday` int(11) DEFAULT '0',//逾期天数
  `state` int(2) DEFAULT '0',//状态 (未归还=0,逾期未归还=1,归还=2,续借未归还=3,续借逾期未归还=4,续借归还=5)
  `readerId` varchar(255) DEFAULT NULL,//借阅读者
  `bookId` int(11) DEFAULT NULL,//借阅书籍
  `penalty` double NOT NULL,//每日罚金
  `aid` int(11) DEFAULT NULL,//操作员
  PRIMARY KEY (`borrowId`),主键
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`),
  CONSTRAINT  FOREIGN KEY (`readerId`) REFERENCES `reader` (`readerId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`bookId`) REFERENCES `book` (`bookId`) ON DELETE CASCADE
);

CREATE TABLE `backinfo` (归还信息表
  `borrowId` int(11) NOT NULL,//借阅编号
  `backDate` datetime DEFAULT NULL,//归还时间
  `aid` int(11) DEFAULT NULL,//操作员
  PRIMARY KEY (`borrowId`),
  CONSTRAINT  FOREIGN KEY (`borrowId`) REFERENCES `borrowinfo` (`borrowId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
);

CREATE TABLE `forfeitinfo` (罚款信息表
  `borrowId` int(11) NOT NULL,//借阅编号
  `forfeit` double DEFAULT NULL,//罚金金额
  `isPay` int(2) DEFAULT '0',//是否已经支付罚金
  `aid` int(11) DEFAULT NULL,//操作员
  PRIMARY KEY (`borrowId`),
  CONSTRAINT  FOREIGN KEY (`borrowId`) REFERENCES `borrowinfo` (`borrowId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
);



设计的实体和属性如下：
学生：学号、姓名、性别、班级、系部、专业、借阅图书 
管理员：工作证号、姓名、性别、联系方式 
图书：ISBN码、书名、作者、出版社、出版日期 
借阅：学号、ISBN码、借书日期、还书日期 
管理-学生：工作证号、学号、是否归还 
管理-图书：工作证号、ISBN码、是否在馆 