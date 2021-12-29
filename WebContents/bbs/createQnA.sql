drop table qna;

create table qna(
 num number primary key,      -- 글 번호
 writer varchar2(30) not null,-- 작성자
 category varchar2(30) not null, -- 카테고리
 subject varchar2(100) not null, -- 제목
 content varchar2(2048) not null, -- 내용
 readcount number default 0,      -- 조회수
 password varchar2(20) not null,  -- 비밀번호
 reg_date date default sysdate,   -- 등록일자
 ip varchar2(30) not null,        -- ip주소
 ref number not null,             -- 답변글 그룹
 re_step  number not null,        -- ref내의 순서
 re_level number not null         -- 들여쓰기 
);


create sequence qna_seq start with 1 increment by 1;

select * from user_objects
where object_type='SEQUENCE';

select * from qna order by num desc;

insert into qna(num,writer,subject,content, password,ip,ref,re_step,re_level)
select qna_seq.nextval,writer,subject,content,password,ip,qna_seq.currval,0,0 from qna;


			    
			    