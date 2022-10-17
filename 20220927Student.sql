drop database if exists studentDB;
create database if not exists studentDB;
drop table if exists student;

use StudentDB;

create table if not exists student (
	no char(6) not null primary key,
	name char(10) not null,
	kor int not null,
	eng int not null,
	math int not null,
	total int null,
	avr decimal(5,2) null,
	grade char(2) null,
	rate int null
);
-- 인덱스 설정 : name
create index idx_student_fistName on student(name);

insert student values ('123456','장건웅',100,100,100,300,0.0,'A',0);
insert student values ('234567','왕건웅',100,100,100,300,0.0,'A',0);


select * from student;
select * from student where name = '팡건웅';


update student set name = '방건웅' where no = '123456';

-- 4)정렬 : 학번,이름,총점 
select * from student order by no desc;
-- 5) 최대값, 최소값 구함.
select max(total) from student;
select min(total) from student;
select max(kor) from student;
-- 6) total = 300 인 사람의 정보를 출력하시오.(서브쿼리문)
select * from student where total = (select max(total) from student);
select * from student where total = (select min(total) from student);
select * from student where kor = (select max(kor) from student);


-- 프로시저 생성(합계, 평균, 등급) 계산하는 프로시저
drop procedure if exists procedure_update_student;

delimiter // -- insert 프로시저
create procedure procedure_insert_student(
	IN in_no char(6),
	IN in_name char(10),
	IN in_kor int,
	IN in_eng int,
	IN in_math int
)
begin
	-- 총점,평균,등급 변수선언
    DECLARE in_total int default 0;
	DECLARE in_avr double default 0.0;
	DECLARE in_grade varchar(2) default null;
	-- 총점계산
    SET in_total = in_kor + in_eng + in_math;
    SET in_avr = in_total / 3.0;
    SET in_grade =
		CASE
			WHEN in_avr >= 90.0	THEN 'A'
			WHEN in_avr >= 80.0	THEN 'B'
			WHEN in_avr >= 70.0	THEN 'C'
			WHEN in_avr >= 60.0	THEN 'D'
			ELSE 'F'
		END;
	-- 삽입 
    insert into student(no, name, kor, eng, math)
		values(in_no, in_name, in_kor, in_eng, in_math);
	-- 수정
    UPDATE student set total = in_total, avr = in_avr, grade = in_grade
		where no = in_no;
end //
delimiter ;


delimiter // -- update 프로시저
create procedure procedure_update_student(
	IN in_no char(6),
	IN in_name char(10),
	IN in_kor int,
	IN in_eng int,
	IN in_math int
)
begin
	-- 총점,평균,등급 변수선언
    DECLARE in_total int default 0;
	DECLARE in_avr double default 0.0;
	DECLARE in_grade varchar(2) default null;
	-- 총점계산
    SET in_total = in_kor + in_eng + in_math;
    SET in_avr = in_total / 3.0;
    SET in_grade =
		CASE
			WHEN in_avr >= 90.0	THEN 'A'
			WHEN in_avr >= 80.0	THEN 'B'
			WHEN in_avr >= 70.0	THEN 'C'
			WHEN in_avr >= 60.0	THEN 'D'
			ELSE 'F'
		END;
	-- 수정
    update student set kor = in_kor, eng = in_eng, math = in_math
		where no = in_no;
	-- 수정
    UPDATE student set total = in_total, avr = in_avr, grade = in_grade
		where no = in_no;
end //
delimiter ;

create table if not exists deleteStudent (
	no char(6) not null,
	name char(10) not null,
	kor int not null,
	eng int not null,
	math int not null,
	total int null,
	avr decimal(5,2) null,
	grade char(2) null,
	rate int null,
    deleteDate dateTime
);

create table if not exists updateStudent (
	no char(6) not null,
	name char(10) not null,
	kor int not null,
	eng int not null,
	math int not null,
	total int null,
	avr decimal(5,2) null,
	grade char(2) null,
	rate int null,
    deleteDate dateTime
);


drop table if exists deleteStudent;

-- 삭제 트리거 생성
delimiter // 
create trigger trg_deleteStudent
	before delete
    on student
    for each row

begin
INSERT INTO `deletestudent` values (old.no, old.name, old.kor, old.eng, old.math, 
	old.total, old.avr, old.grade, old.rate,now());
end //
delimiter ;

-- 수정 트리거 생성
delimiter // 
create trigger trg_updateStudent
	After update
    on student
    for each row

begin
INSERT INTO `updatestudent` values (old.no, old.name, old.kor, old.eng, old.math, 
	old.total, old.avr, old.grade, old.rate,now());
end //
delimiter ;

drop trigger trg_updateStudent;

select * from deleteStudent;
select * from updateStudent;

delete from student where name = '왕건웅';

-- drop table if exists student;
-- drop database if exists studentDB;
