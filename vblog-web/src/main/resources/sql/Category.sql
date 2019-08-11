-- auto Generated on 2019-08-11 19:24:33 
-- DROP TABLE IF EXISTS `category`; 
CREATE TABLE `category`(
    `name` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'name',
    `user_name` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'userName',
    `id` BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `is_del` SMALLINT (5) NOT NULL DEFAULT -1 COMMENT 'isDel',
    `create_time` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createTime',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '`category`';
