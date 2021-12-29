CREATE TABLE reviewBbs (
  num int NOT NULL,
  id varchar(45) NOT NULL,
  name varchar(45) NOT NULL,
  subject varchar(100) NOT NULL,
  content varchar(2048) NOT NULL,
  regist_day varchar(45) DEFAULT NULL,
  hit int DEFAULT NULL,
  ip varchar(45) DEFAULT NULL,
  star varchar(45) DEFAULT NULL,
  PRIMARY KEY (num)
);

alter table reviewBbs add attachFile varchar2(200);



drop sequence reviewBbs_seq;

create sequence reviewBbs_seq start with 1 increment by 1;



select * from reviewBbs;