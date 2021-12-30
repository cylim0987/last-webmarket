drop table product;

CREATE TABLE `webmarketdb`.`product` (
  `p_id` int NOT NULL,
  `p_name` VARCHAR(45) NULL DEFAULT NULL,
  `p_price` INT NULL DEFAULT NULL,
  `p_category` VARCHAR(45) NULL DEFAULT NULL,
  `p_description` LONGTEXT NULL DEFAULT NULL,
  `p_filename` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`p_id`)
  );

select * from product;
  
insert into product(
p_id,
p_name,
p_price,
p_category,
p_description,
p_filename)
values(1,'반 목폴라 티셔츠',17200,'Top','여성 스판 이너 반 목폴라 긴팔 티셔츠 여자 상의','P0001.jpg');