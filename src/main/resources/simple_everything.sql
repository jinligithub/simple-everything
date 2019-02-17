-- 创建数据库
-- create database if not exists simple_everything;
--  不用创建数据库，因为在H2指定数据源时已经指定了一个数据库，
-- 因为h2时嵌入式的它会呈现在本地文件中所以这里不需要单独创建数据库。但是MySQL需要
-- 创建数据库表
drop table if exists file_index;
create table if not exists file_index(
  name varchar(256) not null comment'文件名称',
  path varchar(1024) not null comment'文件路径',
  depth int not null comment'文件路径深度',
  file_type varchar(32)  not null comment '文件类型'
);
