CREATE TABLE `admin` ( ����Ա��
  `aid` int(11) NOT NULL,  ����Աid
  `username`  varchar(20) BINARY NOT NULL, ����Ա�û���
  `name` varchar(20) DEFAULT NULL, ����Ա����
  `pwd` varchar(64) DEFAULT NULL, ����Ա����
  `phone` varchar(20) DEFAULT NULL, ����Ա��ϵ�绰
  `state` int(2) DEFAULT '1',  ����Ա״̬
  PRIMARY KEY (`aid`)  ����
);
CREATE TABLE `authorization` (  Ȩ�ޱ�
  `aid` int(11) NOT NULL,  //����Աid
  `bookSet` int(2) DEFAULT '0', //ͼ������Ȩ��
  `readerSet` int(2) DEFAULT '0',//��������Ȩ��
  `borrowSet` int(2) DEFAULT '0',//��������Ȩ��
  `typeSet` int(2) DEFAULT '0',//ͼ���������Ȩ��
  `backSet` int(2) DEFAULT '0',//�黹����Ȩ��
  `forfeitSet` int(2) DEFAULT '0',//��������Ȩ��
  `sysSet` int(2) DEFAULT '0',//ϵͳ����Ȩ��
  `superSet` int(2) DEFAULT '0',//��������Ȩ��
  PRIMARY KEY (`aid`),����
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`) ���
);


CREATE TABLE `readertype` ( �������ͱ�
  `readerTypeId` int(11) NOT NULL,//��������Id
  `readerTypeName` varchar(255) NOT NULL,//������������
  `maxNum` int(5) NOT NULL,//��������
  `bday` int(5) NOT NULL,//�ɽ�����
  `penalty` double NOT NULL,//ÿ�շ���
  `renewDays` int(5) NOT NULL,//��������
  PRIMARY KEY (`readerTypeId`)����
);

CREATE TABLE `reader` ( ���߱�
  `readerId` varchar(255) NOT NULL,//�Զ���� ����ID
  `pwd` varchar(64) NOT NULL,//����
  `name` varchar(20) NOT NULL,//��ʵ����
  `paperNO` varchar(20) UNIQUE NOT NULL,//֤������
  `phone` varchar(20) DEFAULT NULL,//��ϵ��ʽ
  `email` varchar(30) DEFAULT NULL,//����
  `createTime` datetime DEFAULT NULL,//����ʱ��
  `readerTypeId` int(11) DEFAULT NULL,//��������(ѧ�����߽�ʦ)
  `aid` int(11) DEFAULT NULL,//��������Ա
  PRIMARY KEY (`readerId`),
  CONSTRAINT  FOREIGN KEY (`readerTypeId`) REFERENCES `readertype` (`readerTypeId`) ON DELETE SET NULL,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
);

CREATE TABLE `booktype` (ͼ�����ͱ�
  `typeId` int(11) NOT NULL,//ͼ�����ͱ��
  `typeName` varchar(20) NOT NULL,//ͼ����������
  PRIMARY KEY (`typeId`)
);

CREATE TABLE `book` (ͼ���
  `bookId` int(11) NOT NULL,//ͼ����
  `bookName` varchar(20) NOT NULL,//ͼ������
  `ISBN` varchar(20)  UNIQUE NOT NULL,//ISBN�����ʱ�׼���
  `autho` varchar(20) DEFAULT  NULL,//��������
  `num` int(11) NOT NULL,//������
  `currentNum` int(5) NOT NULL,//�ڹ�����
  `press` varchar(20) DEFAULT NULL,//������
  `description` varchar(255) DEFAULT NULL,//���
  `price` double NOT NULL,//�۸�
  `putdate` datetime DEFAULT NULL,//�ϼ�����
  `typeId` int(11) DEFAULT NULL,//ͼ������
  `aid` int(11) DEFAULT NULL,//��������Ա
  PRIMARY KEY (`bookId`),����
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`),
  CONSTRAINT  FOREIGN KEY (`typeId`) REFERENCES `booktype` (`typeId`) ON DELETE SET NULL
) ;

CREATE TABLE `borrowinfo` (������Ϣ��
  `borrowId` int(11) NOT NULL,//���ı��
  `borrowDate` datetime DEFAULT NULL,//��������
  `endDate` datetime DEFAULT NULL,//��ֹ����
  `overday` int(11) DEFAULT '0',//��������
  `state` int(2) DEFAULT '0',//״̬ (δ�黹=0,����δ�黹=1,�黹=2,����δ�黹=3,��������δ�黹=4,����黹=5)
  `readerId` varchar(255) DEFAULT NULL,//���Ķ���
  `bookId` int(11) DEFAULT NULL,//�����鼮
  `penalty` double NOT NULL,//ÿ�շ���
  `aid` int(11) DEFAULT NULL,//����Ա
  PRIMARY KEY (`borrowId`),����
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`),
  CONSTRAINT  FOREIGN KEY (`readerId`) REFERENCES `reader` (`readerId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`bookId`) REFERENCES `book` (`bookId`) ON DELETE CASCADE
);

CREATE TABLE `backinfo` (�黹��Ϣ��
  `borrowId` int(11) NOT NULL,//���ı��
  `backDate` datetime DEFAULT NULL,//�黹ʱ��
  `aid` int(11) DEFAULT NULL,//����Ա
  PRIMARY KEY (`borrowId`),
  CONSTRAINT  FOREIGN KEY (`borrowId`) REFERENCES `borrowinfo` (`borrowId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
);

CREATE TABLE `forfeitinfo` (������Ϣ��
  `borrowId` int(11) NOT NULL,//���ı��
  `forfeit` double DEFAULT NULL,//������
  `isPay` int(2) DEFAULT '0',//�Ƿ��Ѿ�֧������
  `aid` int(11) DEFAULT NULL,//����Ա
  PRIMARY KEY (`borrowId`),
  CONSTRAINT  FOREIGN KEY (`borrowId`) REFERENCES `borrowinfo` (`borrowId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `admin` (`aid`)
);



��Ƶ�ʵ����������£���
ѧ������ѧ�š��������Ա𡢰༶��ϵ����רҵ������ͼ�骤 
����Ա��������֤�š��������Ա���ϵ��ʽ�� 
ͼ�飺��ISBN�롢���������ߡ������硢�������ڪ� 
���ģ���ѧ�š�ISBN�롢�������ڡ��������ڪ� 
����-ѧ����������֤�š�ѧ�š��Ƿ�黹�� 
����-ͼ�飺������֤�š�ISBN�롢�Ƿ��ڹݪ� 